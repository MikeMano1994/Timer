package com.tryking.timer.base;

import android.app.Application;

/**
 * Created by Tryking on 2016/5/25.
 */
public class TimerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        init();
        initThird();
    }

    private void init() {

    }

    /*
    初始化第三方
     */
    private void initThird() {

    }
}
