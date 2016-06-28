package com.tryking.timer.activity;

import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tryking.timer.R;
import com.tryking.timer.base.BaseActivity;
import com.tryking.timer.base.SystemInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PIMSActivity extends BaseActivity {

    @Bind(R.id.head_portrait)
    SimpleDraweeView headPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pims);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Uri uri = Uri.parse(SystemInfo.getInstance(getApplicationContext()).getPortraitUrl());
        headPortrait.setImageURI(uri);
    }
}
