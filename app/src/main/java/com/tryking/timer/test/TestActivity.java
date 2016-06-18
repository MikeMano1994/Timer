package com.tryking.timer.test;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.tryking.timer.R;
import com.tryking.timer.utils.TT;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    @Bind(R.id.calendar)
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        initData();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initData() {
        calendar.setShownWeekCount(4);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                TT.showShort(TestActivity.this, "我被选择了" + month);
            }
        });

    }
}
