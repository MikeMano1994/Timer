package com.tryking.timer.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tryking.timer.R;
import com.tryking.timer.activity.ShowDataActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/5/13.
 */
public class WeekFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, OnChartValueSelectedListener {
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.show_choose)
    TextView showChoose;
    @Bind(R.id.showPieChart)
    PieChart showPieChart;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    String[] mParties = new String[]{
            "未添加事件", "工作", "娱乐", "生活", "学习"
    };


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
        initChart();

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        showChoose.setText(getSelectedDatesString());
    }

    private void initChart() {
        showPieChart.setUsePercentValues(true);
        showPieChart.setDescription("");
        showPieChart.setExtraOffsets(5, 10, 5, 5);
        showPieChart.setDragDecelerationFrictionCoef(0.95f);

        showPieChart.setDrawHoleEnabled(true);
        showPieChart.setHoleColor(Color.WHITE);
        showPieChart.setCenterText("今日事项统计");

        showPieChart.setTransparentCircleColor(Color.BLACK);
        showPieChart.setTransparentCircleAlpha(110);

        showPieChart.setHoleRadius(58f);
        showPieChart.setTransparentCircleRadius(61f);

        showPieChart.setDrawCenterText(true);

        showPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        showPieChart.setRotationEnabled(true);
        showPieChart.setHighlightPerTapEnabled(true);

        // showPieChart.setUnit(" €");
        // showPieChart.setDrawUnitsInChart(true);

        // add a selection listener
        showPieChart.setOnChartValueSelectedListener(this);

        setData(4, 100);

        showPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = showPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(25, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "颜色标识");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
        colors.add(getResources().getColor(R.color.noEvent));
        colors.add(getResources().getColor(R.color.work));
        colors.add(getResources().getColor(R.color.amuse));
        colors.add(getResources().getColor(R.color.life));
        colors.add(getResources().getColor(R.color.study));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tf);
        showPieChart.setData(data);

        // undo all highlights
        showPieChart.highlightValues(null);

        showPieChart.invalidate();
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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
