package com.example.jcompia.tutoralnavi3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;



public class GoogleSignActivity extends MainActivity {
    public static Context mContext;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    //private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    View.OnClickListener signInButtonOnClickListener;
    View.OnClickListener signOutButtonOnClickListener;
    View.OnClickListener disconnectButtonOnClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        Toast.makeText(GoogleSignActivity.this, "GoogleSignActivity!",     Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        defineView();

        //메인엑티비티에에서 구글 클라이언트를 가져온다.
        mGoogleApiClient = GoogleApplication.getInstance().getGoogleApiClient();//super.getGoogleApiClient();


    }

    public void defineView(){
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_google_sign, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.getMenu().getItem(1).setChecked(true);

        // Views
        // mStatusTextView = (TextView) findViewById(R.id.status);
        createBtnListener();
        defineBtnListener();


    }

    private void createBtnListener() {
        signInButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        };

        signOutButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();

            }
        };

        disconnectButtonOnClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        };

    }

    private void defineBtnListener() {
        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(signInButtonOnClickListener);
        findViewById(R.id.sign_out_button).setOnClickListener(signOutButtonOnClickListener);
        findViewById(R.id.disconnect_button).setOnClickListener(disconnectButtonOnClickListener);
    }


    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(GoogleSignActivity.this, "handleSignInResult:" +requestCode,     Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    public void handleSignInResult(GoogleSignInResult result) {
        Toast.makeText(GoogleSignActivity.this, "handleSignInResult:" + result.isSuccess(),     Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(GoogleSignActivity.this, "handleSignInResult getEmail:" + acct.getEmail()+"getAccount:" + acct.getAccount(),     Toast.LENGTH_SHORT).show();


            // 오류가 나면 또 여기를 보겠지 ? 밑에 키스토어 생성은 기본적으로 해두고

            //$keytool -genkey -v -keystore tutu1234.keystore -alias tutu1234 -keyalg RSA -keysize 2048 -validity 18250


            //디버그 키스토어의 sha1 결과를 firebase 콘솔에 접근하여 톱니바퀴를 루른다음 지문추가 해야 한다
            //keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

            //
            //C:\Program Files\Android\Android Studio\jre\bin>keytool -genkey -v -keystore C:\Users\사용자명\.android\debug.keystore -alias 별칭 -keyalg RSA -keysize 2048 -validity 18250
            //C:\Program Files\Android\Android Studio\jre\bin>keytool -list -v -keystore C:\Users\사용자명\.android\debug.keystore -alias 별칭 -storepass 비밀번호 -keypass 비밀번호

           // mStatusTextView.setText(acct.getDisplayName());
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    public void signIn() {
         Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    public void showProgressDialog() {
        super.showProgressDialog(mContext);
        /*if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();*/
    }

    public void hideProgressDialog() {

        super.hideProgressDialog();
        /*if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }*/
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        signIncheck();
    }

    public void signIncheck(){
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Toast.makeText(GoogleSignActivity.this, "Got cached sign-in",     Toast.LENGTH_SHORT).show();

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

}














