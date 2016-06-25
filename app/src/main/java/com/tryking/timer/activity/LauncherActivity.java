package com.tryking.timer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tryking.timer.R;
import com.tryking.timer.global.Constants;
import com.tryking.timer.utils.SPUtils;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        init();
    }

    private void init() {
        boolean isFirst = (boolean) SPUtils.get(LauncherActivity.this, Constants.SHARED_PREFERENCE_IS_FIRST_LAUNCHER, true);
        if (isFirst) {
            startActivity(new Intent(LauncherActivity.this, GuidenceActivity.class));
            SPUtils.put(LauncherActivity.this, Constants.SHARED_PREFERENCE_IS_FIRST_LAUNCHER, false);
        } else {
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        }
        finish();
    }
}
