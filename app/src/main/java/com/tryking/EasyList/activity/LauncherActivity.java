package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tryking.EasyList.R;
import com.tryking.EasyList._activity.LoginActivity;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.test.TestActivity;
import com.tryking.EasyList.utils.SPUtils;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mHandler.sendEmptyMessageDelayed(Constants.launchStartInit, 2000);
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
}
