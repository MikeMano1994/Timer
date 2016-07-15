package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.SPUtils;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        init();
    }

    private void init() {
        boolean isFirst = (boolean) SPUtils.get(LauncherActivity.this, Constants.SHARED_PREFERENCE_IS_FIRST_LAUNCHER, true);
        if (isFirst) {
            startActivity(new Intent(LauncherActivity.this, GuidanceActivity.class));
            SPUtils.put(LauncherActivity.this, Constants.SHARED_PREFERENCE_IS_FIRST_LAUNCHER, false);
        } else {
            if (SystemInfo.getInstance(getApplicationContext()).isLogin()) {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(LauncherActivity.this, LoginAndRegisterActivity.class));
            }
        }
        finish();
    }
}
