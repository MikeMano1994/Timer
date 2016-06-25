package com.tryking.timer.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;
import com.tryking.timer.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        initSystemInfo();
    }

    /*
    初始化系统信息
     */
    private void initSystemInfo() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date nowDate = new Date(System.currentTimeMillis());
        String nowDateStr = dateFormat.format(nowDate);
        String currentDate = nowDateStr.substring(0, 4) + nowDateStr.substring(5, 7) + nowDateStr.substring(8, 10);
        String saveDate = (String) SPUtils.get(getApplicationContext(), "currentDate", "");
        Logger.e(currentDate + "cur:::save:" + saveDate);
        //如果不是同一天的话就要把所有的数据清除
        if (!saveDate.equals(currentDate)) {
            SPUtils.put(getApplicationContext(), "startTimes", "");
            SPUtils.put(getApplicationContext(), "endTimes", "");
            SPUtils.put(getApplicationContext(), "eventTypes", "");
            SPUtils.put(getApplicationContext(), "currentDate", currentDate);
        }
    }

    /*
    初始化第三方
     */
    private void initThird() {
        Fresco.initialize(getApplicationContext());
    }
}
