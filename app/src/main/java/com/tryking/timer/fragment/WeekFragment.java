package com.tryking.timer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tryking.timer.R;
import com.tryking.timer.activity.ShowDataActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/5/13.
 */
public class WeekFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.show_choose)
    TextView showChoose;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_weekly, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        showChoose.setText(getSelectedDatesString());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        showChoose.setText(getSelectedDatesString());

        Intent intent = new Intent();
        intent.setClass(getActivity(), ShowDataActivity.class);
        intent.putExtra("date", getSelectedDatesString());
        startActivity(intent);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private String getSelectedDatesString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        CalendarDay date = calendarView.getSelectedDate();
        if (date != null) {
//            return FORMATTER.format(date.getDate());
            return dateFormat.format(date.getDate());
        }
        return "未选择";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
