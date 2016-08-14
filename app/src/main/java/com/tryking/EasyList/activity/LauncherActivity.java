package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.ActivityUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        //为了测试方便，穿进去memberId，不用登陆
//        SystemInfo.getInstance(getApplicationContext()).setMemberId("6E49327D182AA31306D297B794B59EDE");

        mHandler.sendEmptyMessageDelayed(Constants.launchStartInit, 2000);
        initThird();
    }

    private void initThird() {
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        //友盟官方文档让放在入口Activity
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后

        //友盟反馈提醒用户（默认控制栏  可自定义 无自定义）
        FeedbackAgent agent = new FeedbackAgent(this);
        //显示的欢迎语
        agent.setWelcomeInfo("你的想法正闪闪发光，快告诉我们吧~");
        agent.sync();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.launchStartInit:
                    init();
                    break;
            }
        }
    };

    private void init() {
        boolean isFirst = (boolean) SPUtils.get(LauncherActivity.this, Constants.SHARED_PREFERENCE_IS_FIRST_LAUNCHER, true);
        if (isFirst) {
            startActivity(new Intent(LauncherActivity.this, GuidanceActivity.class));
        } else {
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
//            startActivity(new Intent(LauncherActivity.this, TestActivity.class));
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写
        MobclickAgent.onPageStart(getString(R.string.launcher));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.launcher));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
