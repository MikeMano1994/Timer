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
import android.widget.Button;
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
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tryking.timer.R;
import com.tryking.timer.activity.ViewHistoryActivity;
import com.tryking.timer.bean.TodayEventData;
import com.tryking.timer.utils.CommonUtils;
import com.tryking.timer.utils.SPUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.bt_viewHistory)
    Button btViewHistory;

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

    @OnClick(R.id.bt_viewHistory)
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_viewHistory:
                startActivity(new Intent(getActivity(), ViewHistoryActivity.class));
                break;
            default:
                break;
        }
    }

    /*
    刷新Fragment的数据
     */
    // TODO: 2016/6/19 掌握Fragment的生命周期，让每次见到页面时都能刷新数据
    public void refresh() {
        initChart(getEventData());
        Logger.e("超时空刷新");
    }

    private void init() {
        initChart(getEventData());

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        showChoose.setText(getSelectedDatesString());
    }

    /*
       拿到每日的统计数据，直接将统计好的比例数据传回去
        */
    private float[] getEventData() {
        String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
        String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
        String eventTypes = (String) SPUtils.get(getActivity(), "eventTypes", "");

        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
        String[] type = CommonUtils.convertStrToArray(eventTypes);

        float noEvent = 0;
        float work = 0;
        float amuse = 0;
        float life = 0;
        float study = 0;
        for (int i = 0; i < starts.length; i++) {
            if (starts[0] == "" && ends[0] == "") {
                noEvent += 24 * 60;
            } else {
                if (i == 0 && Integer.parseInt(starts[0]) > 0) {
                    noEvent += CommonUtils.durationMinutes("0000", starts[0]);
                }
                switch (Integer.parseInt(type[i])) {
                    case TodayEventData.TYPE_WORK:
                        work += CommonUtils.durationMinutes(starts[i], ends[i]);
                        break;
                    case TodayEventData.TYPE_AMUSE:
                        amuse += CommonUtils.durationMinutes(starts[i], ends[i]);
                        break;
                    case TodayEventData.TYPE_LIFE:
                        life += CommonUtils.durationMinutes(starts[i], ends[i]);
                        break;
                    case TodayEventData.TYPE_STUDY:
                        study += CommonUtils.durationMinutes(starts[i], ends[i]);
                        break;
                    default:
                        break;
                }
                if (i != starts.length - 1 && (!ends[i].equals(starts[i + 1]))) {
                    noEvent += CommonUtils.durationMinutes(ends[i], starts[i + 1]);
                }
                if (i == starts.length - 1 && Integer.parseInt(ends[ends.length - 1]) < 2400) {
                    noEvent += CommonUtils.durationMinutes(ends[ends.length - 1], "2400");
                }
            }
        }
        Logger.e("noEvent:" + noEvent + ":work:" + work + ":amuse:" + amuse + ":life:" + life + ":study:" + study);
        return new float[]{noEvent / (24 * 60) * 100, work / (24 * 60) * 100, amuse / (24 * 60) * 100, life / (24 * 60) * 100, study / (24 * 60) * 100};
    }

    private void initChart(float[] eventData) {
        showPieChart.setUsePercentValues(true);
        showPieChart.setDescription("今日事项统计");
        showPieChart.setExtraOffsets(5, 10, 5, 5);
        showPieChart.setDragDecelerationFrictionCoef(0.95f);

        showPieChart.setDrawHoleEnabled(true);
//        showPieChart.setHoleColor(Color.WHITE);
        showPieChart.setHoleColorTransparent(true);
        showPieChart.setCenterText("今日事项统计");
        showPieChart.setCenterTextSize(25);

        showPieChart.setTransparentCircleColor(Color.BLACK);
        showPieChart.setTransparentCircleAlpha(110);

        showPieChart.setHoleRadius(58f);
        showPieChart.setTransparentCircleRadius(61f);

        showPieChart.setDrawCenterText(true);

        showPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        showPieChart.setRotationEnabled(false);
        showPieChart.setHighlightPerTapEnabled(true);

        // showPieChart.setUnit(" €");
        // showPieChart.setDrawUnitsInChart(true);

        // add a selection listener
        showPieChart.setOnChartValueSelectedListener(this);

        setData(4, 100, eventData);

        showPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = showPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

    }

    /*
    给chart设置数据
     */
    private void setData(int count, float range, float[] eventData) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry(eventData[i], i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "颜色标识");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

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
        data.setValueTextColor(Color.TRANSPARENT);
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
        intent.setClass(getActivity(), ViewHistoryActivity.class);
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
//        "未添加事件", "工作", "娱乐", "生活", "学习"
        switch (e.getXIndex()) {
            case 0:
                showPieChart.setCenterText("未添加事件:\n" + CommonUtils.getApproximation(e.getVal()) + "%");
                break;
            case 1:
                showPieChart.setCenterText("工作:\n" + CommonUtils.getApproximation(e.getVal()) + "%");
                break;
            case 2:
                showPieChart.setCenterText("娱乐:\n" + CommonUtils.getApproximation(e.getVal()) + "%");
                break;
            case 3:
                showPieChart.setCenterText("生活:\n" + CommonUtils.getApproximation(e.getVal()) + "%");
                break;
            case 4:
                showPieChart.setCenterText("学习:\n" + CommonUtils.getApproximation(e.getVal()) + "%");
                break;
        }
    }

    @Override
    public void onNothingSelected() {
        showPieChart.setCenterText("今日事项统计");
    }
}
