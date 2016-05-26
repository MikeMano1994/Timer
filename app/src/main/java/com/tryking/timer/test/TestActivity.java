package com.tryking.timer.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tryking.timer.R;
import com.tryking.timer.widgets.CountDownTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.text)
    CountDownTextView text;
    @Bind(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setTextColor(Color.RED);
                text.setTextPreTime("还剩余：");
                text.setIntervalTime(60 * 2, true);
            }
        });
    }
}
