package com.tryking.timer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tryking.timer.R;
import com.tryking.timer.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuidanceActivity extends BaseActivity {

    @Bind(R.id.hint)
    TextView hint;
    @Bind(R.id.jump)
    Button jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidence);
        ButterKnife.bind(this);

        init();
    }

    @OnClick(R.id.jump)
    void click(View v) {
        startActivity(new Intent(GuidanceActivity.this, LoginAndRegisterActivity.class));
        finish();
    }


    private void init() {

    }
}
