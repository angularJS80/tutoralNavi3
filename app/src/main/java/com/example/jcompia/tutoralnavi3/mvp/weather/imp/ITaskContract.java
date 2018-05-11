package com.example.jcompia.tutoralnavi3.mvp.weather.imp;

import com.example.jcompia.tutoralnavi3.mvp.comm.imp.IBasePresenter;
import com.example.jcompia.tutoralnavi3.mvp.comm.imp.IBaseView;

public interface ITaskContract  {
    interface Presenter extends IBasePresenter {
        String getWeather();

        String getWeatherRx();
    }

    /* 뷰때어내보기

    interface View extends IBaseView<Presenter> {
        void showWeather(String weather);
    }*/
}
