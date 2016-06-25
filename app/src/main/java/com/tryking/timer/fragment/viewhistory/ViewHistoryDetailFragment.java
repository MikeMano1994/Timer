package com.tryking.timer.fragment.viewhistory;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.orhanobut.logger.Logger;
import com.tryking.timer.R;
import com.tryking.timer.adapter.TodayEventAdapter;
import com.tryking.timer.bean.TodayEventData;
import com.tryking.timer.db.dao.EverydayEventSourceDao;
import com.tryking.timer.db.dao.SpecificEventSourceDao;
import com.tryking.timer.db.table.EverydayEventSource;
import com.tryking.timer.db.table.SpecificEventSource;
import com.tryking.timer.utils.CommonUtils;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/6/20.
 */
public class ViewHistoryDetailFragment extends Fragment implements OnChartValueSelectedListener {
    @Bind(R.id.rv_showData)
    RecyclerView rvShowData;
    @Bind(R.id.showPieChart)
    PieChart showPieChart;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    List<TodayEventData> eventDatas = new ArrayList<>();
    String date = "";

    private TodayEventAdapter adapter;
    String[] mParties = new String[]{
            "未添加事件", "工作", "娱乐", "生活", "学习"
    };

    public static ViewHistoryDetailFragment getInstance(String date) {
        ViewHistoryDetailFragment viewHistoryDetailFragment = new ViewHistoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", date);
        viewHistoryDetailFragment.setArguments(bundle);
        return viewHistoryDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_history_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {

        Bundle arguments = getArguments();
        String getDate = arguments.getString("date");
        if (getDate != null) {
            date = CommonUtils.clearHanZiFromStr(getDate);
        }

        rvShowData.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TodayEventAdapter(new WeakReference<Context>(getActivity()), eventDatas);
        rvShowData.setAdapter(adapter);
        String[] dataFromDatabase = getDataFromDatabase(date);
        if (dataFromDatabase.length != 0) {
            refreshRecyclerViewDataByString(dataFromDatabase[0], dataFromDatabase[1], dataFromDatabase[2]);

            float[] eventData = getEventData(dataFromDatabase);
            initChart(eventData);
        }
        tvTitle.setText(getDate + "事项统计");
        showPieChart.setCenterText("点击边缘，\n查看比例");
        showPieChart.setDescription(getDate + "事项统计");
    }

    private float[] getEventData(String[] dataFromDatabase) {
        String[] starts = CommonUtils.convertStrToArray(dataFromDatabase[0]);
        String[] ends = CommonUtils.convertStrToArray(dataFromDatabase[1]);
        String[] type = CommonUtils.convertStrToArray(dataFromDatabase[2]);

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
//        Logger.e("noEvent:" + noEvent + ":work:" + work + ":amuse:" + amuse + ":life:" + life + ":study:" + study);
        return new float[]{noEvent / (24 * 60) * 100, work / (24 * 60) * 100, amuse / (24 * 60) * 100, life / (24 * 60) * 100, study / (24 * 60) * 100};
    }

    /*
    从数据库中获取数据
     */
    private String[] getDataFromDatabase(String date) {
        EverydayEventSourceDao everydayEventDao = new EverydayEventSourceDao(getActivity());
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", "");
            map.put("eventDate", date);
            List<EverydayEventSource> eventList = everydayEventDao.query(map);

            if (eventList == null || eventList.size() <= 0) {
//                Logger.e("未添加过事件");
            } else {
//                Logger.e("有事件");
                for (int i = 0; i < eventList.size(); i++) {
                    Logger.e(eventList.get(i).toString());
                    String startTimes = eventList.get(i).getStartTimes();
                    String endTimes = eventList.get(i).getEndTimes();
                    String eventTypes = eventList.get(i).getEventTypes();
                    Logger.e(startTimes + "::" + endTimes);
//                    refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);
                    return new String[]{startTimes, endTimes, eventTypes};
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[0];
    }


    /*
    根据起止时间更新RecyclerView的内容
     */
    private void refreshRecyclerViewDataByString(String startTimes, String endTimes, String eventTypes) {
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
        String[] type = CommonUtils.convertStrToArray(eventTypes);
        String[] event = new String[starts.length];
        //拿到具体的事件
        for (int i = 0; i < starts.length; i++) {
            SpecificEventSourceDao specificEventDao = new SpecificEventSourceDao(getActivity());
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("userId", "");
                map.put("eventDate", date);
                map.put("startTime", starts[i]);
                ArrayList<SpecificEventSource> specificEventList = (ArrayList<SpecificEventSource>) specificEventDao.query(map);
                if (specificEventList == null || specificEventList.size() <= 0) {
                    //本时段未添加过事项
                } else {
                    String specificEvent = specificEventList.get(0).getSpecificEvent();
                    Logger.e(specificEvent.toString());
                    event[i] = specificEvent;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        eventDatas.clear();
        for (int i = 0; i < starts.length; i++) {
            if (starts[0] == "" && ends[0] == "") {
                TodayEventData data1 = new TodayEventData(0, "0000", "2400", "未添加事件");
                eventDatas.add(data1);
            } else {
                if (i == 0 && Integer.parseInt(starts[0]) > 0) {
                    TodayEventData data2 = new TodayEventData(0, "0000", starts[0], "未添加事件");
                    eventDatas.add(data2);
                }
                TodayEventData data3 = new TodayEventData(Integer.parseInt(type[i]), starts[i], ends[i], event[i]);
                eventDatas.add(data3);
                if (i != starts.length - 1 && (!ends[i].equals(starts[i + 1]))) {
                    TodayEventData data1 = new TodayEventData(0, ends[i], starts[i + 1], "未添加事件");
                    eventDatas.add(data1);
                }
                if (i == starts.length - 1 && Integer.parseInt(ends[ends.length - 1]) < 2400) {
                    TodayEventData data2 = new TodayEventData(0, ends[ends.length - 1], "2400", "未添加事件");
                    eventDatas.add(data2);
                }
            }
        }
        adapter.refresh(eventDatas);
    }

    private void initChart(float[] eventData) {
        showPieChart.setUsePercentValues(true);
//        showPieChart.setDescription("今日事项统计");
        showPieChart.setExtraOffsets(5, 10, 5, 5);
        showPieChart.setDragDecelerationFrictionCoef(0.95f);

        showPieChart.setDrawHoleEnabled(true);
//        showPieChart.setHoleColor(Color.WHITE);
        showPieChart.setHoleColorTransparent(true);
//        showPieChart.setCenterText("今日事项统计");
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
        showPieChart.setCenterText("点击边缘，\n查看比例");
    }
}
