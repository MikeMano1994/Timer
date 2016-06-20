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

import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tryking.timer.R;
import com.tryking.timer.activity.ViewHistoryActivity;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/6/20.
 */
public class ViewHistoryCalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.show_choose)
    TextView showChoose;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_history_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("onResume ");
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
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        showChoose.setText(getSelectedDatesString());

        Intent intent = new Intent();
        intent.setClass(getActivity(), ViewHistoryActivity.class);
        intent.putExtra("date", getSelectedDatesString());
        startActivity(intent);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
