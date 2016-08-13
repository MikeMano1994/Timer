package com.tryking.EasyList.base;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.activity.about_easylist.FeedbackActivity;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.StringUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.socialize.PlatformConfig;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Tryking on 2016/5/25.
 */
public class EasyListApplication extends Application {
    public final String TAG = "EasyListApplication";
    /**
     * APP对象
     */
    private static EasyListApplication instance;

    //网络请求队列
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();

        init();
        initThird();
    }

    private void init() {
        instance = this;
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
        String saveDate = (String) SPUtils.get(this, ApplicationGlobal.CURRENT_DATE, "");
        Logger.e("CurrentDate:" + currentDate + "\nSaveDate:" + saveDate);
        //如果不是同一天的话就要把所有的数据清除
        if (!saveDate.equals(currentDate)) {
            String startTimes = (String) SPUtils.get(this, ApplicationGlobal.START_TIMES, "");
            String[] starts = CommonUtils.convertStrToArray(startTimes);
            for (int i = 0; i < starts.length; i++) {
                //把昨天存储的事件的key删除
                SPUtils.remove(this, starts[i]);
            }
            SPUtils.put(getApplicationContext(), ApplicationGlobal.START_TIMES, "");
            SPUtils.put(getApplicationContext(), ApplicationGlobal.END_TIMES, "");
            SPUtils.put(getApplicationContext(), ApplicationGlobal.EVENT_TYPES, "");
            SPUtils.put(getApplicationContext(), ApplicationGlobal.CURRENT_DATE, currentDate);
            SPUtils.put(getApplicationContext(), Constants.SP_TODAY_ONE_WORD, "");
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

        //U盟统计集成测试
        MobclickAgent.setDebugMode(true);
        //友盟统计（因为包含Fragment，所以需要自己做处理）
        MobclickAgent.openActivityDurationTrack(false);
        //开启友盟异常捕获上传
        MobclickAgent.setCatchUncaughtExceptions(true);

        //友盟反馈
        FeedbackPush.getInstance(this).init(FeedbackActivity.class, true);
    }

    /*
    初始化友盟社会化分享
     */
    private void initUMeng() {
        PlatformConfig.setWeixin(Constants.THIRD_WEIXIN_APP_ID, Constants.THIRD_WEIXIN_APP_SECRET);
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo(Constants.THIRD_SINA_APP_KEY, Constants.THIRD_SINA_APP_SECRET);
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone(Constants.THIRD_QQ_APP_ID, Constants.THIRD_QQ_APP_KEY);
        // QQ和Qzone appid appkey
    }

    public static EasyListApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(StringUtil.isEmpty(tag) ? TAG : tag);

//		VolleyLog.d("添加网络请求到队列: %s", req.getUrl());

        req.setRetryPolicy(new DefaultRetryPolicy(ApplicationGlobal.REQUEST_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }
}
