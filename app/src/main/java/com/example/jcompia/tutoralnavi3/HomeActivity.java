package com.example.jcompia.tutoralnavi3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jcompia.tutoralnavi3.govweather.ApiConnector;
import com.example.jcompia.tutoralnavi3.govweather.AsyncTaskAPIConnector;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends MainActivity {
    private static final String TAG = "HomeActivity";
    HashMap<String, Integer> notifyIdMap = new HashMap();
    private int threadNumber = 10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Toast.makeText(HomeActivity.this, "HomeActivity!",     Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_home, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.getMenu().getItem(1).setChecked(true);

        Log.d(TAG, "##Run helloworld"  );
        ApiConnector.helloworld(HomeActivity.this);
       AsyncTaskAPIConnector asyncTaskAPIConnector = new AsyncTaskAPIConnector(this, (TextView) findViewById(R.id.wv));
        final String serviceKey= "hOoqoTjEflU73a4GVB%2FWraajQopg6BxoSZQ6Ie6OMIBG%2FaUoktc7ep2jDZhsJVFHI62DzbqG7pnPbdPauLuM7g%3D%3D";
        final String fromTmFc = "20171203";
        final String toTmFc = "20171203";
        final int numOfRows = 1;
        final int pageNo = 1;
        final int pageSize = 1;
        final int startPage = 1;
        final String stnId = "108";
        final String _type ="json";

        Map<String, String> map = new HashMap<>();
        map.put("serviceKey", serviceKey);
        map.put("fromTmFc", fromTmFc);
        map.put("toTmFc", toTmFc);
        map.put("numOfRows", numOfRows+"");
        map.put("pageSize", pageSize+"");
        map.put("pageNo", pageNo+"");
        map.put("startPage", startPage+"");
        map.put("stnId", stnId);
        map.put("_type", _type);
        asyncTaskAPIConnector.execute(map);
        reciveNoti();


    }

    protected void reciveNoti(){
        int recivenotificationId = 0;
        if(getIntent().getExtras().get("notificationId")!=null){
            recivenotificationId = (Integer) getIntent().getExtras().get("notificationId");
            NotificationManager nm =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(recivenotificationId);

            ( (TextView) findViewById(R.id.wv)).setText("recivenotificationId : "+recivenotificationId);
        }else{
            doNoti();
        }



    }


    protected void doNoti(){

        NotificationCompat.Builder mBuilder = notiBuilder("기본알림", "기본알림 내용");

        /*mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE);*/

      /*  Intent resultIntent = new Intent(this, HomeActivity.class);
        mBuilder.setContentIntent(
                getSpeckPendingIntent(resultIntent, MainActivity.class)
        )
                .setAutoCancel(true);*/

        noti(mBuilder, 1);
        //notiThread(mBuilder);
    }



    // 쓰래드를 이용해야 하는 알림의 공통함수
    private void notiThread(final NotificationCompat.Builder mBuilder) {
        final NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 쓰래드로 해야 중복 알림없이 1개로 발생
        // When the loop is finished, updates the notification

        // 쓰래드로 통해 실행 됨으로 notiThread 함수는 즉시 종료 된고 쓰래드는 수행된다.
        Thread t1 = new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        int id =getNotiId(Thread.currentThread().getId());

                        for (int incr = 0; incr <= 100; incr+=5) {
                            mBuilder.setProgress(100, incr, false);
                            mNotifyManager.notify(id,mBuilder.build());
                            if(incr==100){
                                mNotifyManager.cancel(id);
                            }
                            try {
                                // Sleep for 5 seconds
                                Thread.sleep(1*1000);
                                Log.d("Therad","Thread name: "+Thread.currentThread().getId());
                            } catch (InterruptedException e) {
                                Log.d("Error", "sleep failure");
                            }
                        }

                    }
                }
        );
    }


    protected NotificationCompat.Builder notiBuilder(String title,String text){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher) // 알림창에 표기 아이콘
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true);
        return mBuilder;
    }

    private void noti(NotificationCompat.Builder notifyBuilder, int notifyID) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(
                notifyID,
                notifyBuilder.build());
    }



    private PendingIntent getSpeckPendingIntent(Intent resultIntent, Class cls) {
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        return notifyPendingIntent;
    }


    // 확장알림내에 표현해야 되는 박스데이터
    protected NotificationCompat.InboxStyle getInBoxContent(){
        NotificationCompat.InboxStyle inBoxStyle =
                new NotificationCompat.InboxStyle();


        String[] events = new String[6];
        inBoxStyle.setBigContentTitle("Event tracker details:");



        for (int i=0; i < events.length; i++) {
            events[i]="숫자"+i;
            inBoxStyle.addLine(events[i]);
        }



        return inBoxStyle;
    }

    // 실행중인 쓰래드의 아이디와
    protected int getNotiId(Long notifyIdLong){
        String notifyId = Long.toString(notifyIdLong);

        if(notifyIdMap.get(notifyId)==null){
            threadNumber++;
            notifyIdMap.put(notifyId,threadNumber);
        }
        return notifyIdMap.get(notifyId);
    }

    protected int getNotiId(int notifyInt){
        String notifyId = Integer.toString(notifyInt);

        if(notifyIdMap.get(notifyId)==null){
            threadNumber++;
            notifyIdMap.put(notifyId,threadNumber);
        }
        return notifyIdMap.get(notifyId);
    }
}
