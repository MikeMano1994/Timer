package com.tryking.EasyList.base;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.SPUtils;
import com.umeng.socialize.PlatformConfig;

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
        //Fresco不用了
//        Fresco.initialize(getApplicationContext());
        //SMSSDK一初始化就报错，应该是和别的三方冲突，和Fresco也冲突。（貌似只有5.1版本的手机才有错）
//        SMSSDK.initSDK(getApplicationContext(),Constants.THIRD_MOB_APP_KEY,Constants.THIRD_MOB_APP_SECRET);
        initUMeng();
    }

    /*
    初始化友盟社会化分享
     */
    private void initUMeng() {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo(Constants.THIRD_SINA_APP_KEY, Constants.THIRD_SINA_APP_SECRET);
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone(Constants.THIRD_QQ_APP_ID, Constants.THIRD_QQ_APP_KEY);
        // QQ和Qzone appid appkey
    }
}
