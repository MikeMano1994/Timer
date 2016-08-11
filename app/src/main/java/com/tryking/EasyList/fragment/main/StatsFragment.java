package com.tryking.EasyList.fragment.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList.activity.LoginActivity;
import com.tryking.EasyList.activity.ViewHistoryActivity;
import com.tryking.EasyList.activity.ViewYesterdayActivity;
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.base.String4Broad;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.db.dao.EverydayEventSourceDao;
import com.tryking.EasyList.db.table.EverydayEventSource;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.marqueeView.MarqueeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tryking on 2016/5/13.
 */
public class StatsFragment extends BaseFragment implements OnChartValueSelectedListener {
    @Bind(R.id.showPieChart)
    PieChart showPieChart;
    @Bind(R.id.bt_viewHistory)
    Button btViewHistory;
    @Bind(R.id.bt_viewYesterday)
    Button btViewYesterday;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.mv_notice)
    MarqueeView mvNotice;

    //    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    String[] mParties = new String[]{
            "未添加事件", "工作", "娱乐", "生活", "学习"
    };
    @Bind(R.id.main_content)
    FrameLayout mainContent;
    private RefreshChatDataReceiver refreshChatDataReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        List<String> info = new ArrayList<>();
        info.add("时间是一切财富中最宝贵的财富。 \n—— 德奥弗拉斯多");
        info.add("没有人不爱惜他的生命，但很少人珍视他的时间。 \n—— 梁实秋");
        info.add("从不浪费时间的人，没有工夫抱怨时间不够。 \n—— 杰弗逊");
        info.add("时间是真理的挚友。 \n—— 科尔顿");
        info.add("时间像奔腾澎湃的急湍，它一去无还，毫不留恋。 \n—— 塞万提斯");
        info.add("睡眠和休息丧失了时间，却取得了明天工作的精力。 \n—— 毛泽东");
        mvNotice.startWithList(info);


        initChart(getEventData(getEventStrings()));
        btViewYesterday.setText("查看昨日");
        tvTitle.setText("今日统计");

        IntentFilter exitFilter = new IntentFilter(String4Broad.RefershChartData);
        refreshChatDataReceiver = new RefreshChatDataReceiver();
        getActivity().registerReceiver(refreshChatDataReceiver, exitFilter);
    }

    @OnClick({R.id.bt_viewHistory, R.id.bt_viewYesterday})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_viewHistory:
                if (SystemInfo.getInstance(getActivity()).isLogin()) {
                    startActivity(new Intent(getActivity(), ViewHistoryActivity.class));
                } else {
                    hintLogin();
                }
                break;
            case R.id.bt_viewYesterday:
                if (btViewYesterday.getText().toString() == "查看昨日") {
                    if (SystemInfo.getInstance(getActivity()).isLogin()) {
//                    refreshYesterdayData();
                        //上面的是从数据库获取昨日数据
                        startActivity(new Intent(getActivity(), ViewYesterdayActivity.class));
                    } else {
                        hintLogin();
                    }
                } else {
                    initChart(getEventData(getEventStrings()));
                    btViewYesterday.setText("查看昨日");
                    tvTitle.setText("今日事项统计");
                    btViewYesterday.setText("查看昨日");
                    tvTitle.setText("今日事项统计");
                }
                break;
            default:
                break;
        }
    }

    private void hintLogin() {
        new AlertDialog.Builder(getActivity())
                .setTitle("去登陆")
                .setMessage("该功能需要登陆才能使用，快去登陆吧")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        dialog.dismiss();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /*
    数据更新为昨日的数据
     */
    private void refreshYesterdayData() {
        String currentDate = (String) SPUtils.get(getActivity(), "currentDate", "");
        String previousDay = CommonUtils.getPreviousDay(currentDate, -1);
        String[] dataFromDatabase = getDataFromDatabase(previousDay);
        if (null != dataFromDatabase) {
            initChart(getEventData(dataFromDatabase));
            showPieChart.setDescription("昨日");
            showPieChart.setCenterText("昨日");
            btViewYesterday.setText("查看今日");
            tvTitle.setText("昨日事项统计");
        }
    }

    private String[] getEventStrings() {
        String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
        String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
        String eventTypes = (String) SPUtils.get(getActivity(), "eventTypes", "");
        return new String[]{startTimes, endTimes, eventTypes};
    }

    /*
     拿到每日的统计数据，直接将统计好的比例数据传回去
    */
    private float[] getEventData(String[] eventStrings) {
        String startTimes = eventStrings[0];
        String endTimes = eventStrings[1];
        String eventTypes = eventStrings[2];

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
                TT.showShort(getActivity(), "昨日未添加数据");
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
        return null;
    }

    private void initChart(float[] eventData) {
        showPieChart.setUsePercentValues(true);
        showPieChart.setDescription("");
        showPieChart.setExtraOffsets(5, 10, 5, 5);
        showPieChart.setDragDecelerationFrictionCoef(0.95f);

        showPieChart.setDrawHoleEnabled(true);
        showPieChart.setHoleColor(Color.WHITE);

        showPieChart.setTransparentCircleColor(Color.WHITE);
        showPieChart.setTransparentCircleAlpha(110);

        showPieChart.setHoleRadius(58f);
        showPieChart.setTransparentCircleRadius(61f);

        showPieChart.setDrawCenterText(true);
        showPieChart.setCenterText("今日");

        showPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        showPieChart.setRotationEnabled(true);
        showPieChart.setHighlightPerTapEnabled(true);

        // showPieChart.setUnit(" €");
        // showPieChart.setDrawUnitsInChart(true);

        // add a selection listener
        showPieChart.setOnChartValueSelectedListener(this);

        setData(5, 100, eventData);

        showPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = showPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        showPieChart.setEntryLabelColor(Color.WHITE);
        showPieChart.setEntryLabelTextSize(12f);
    }

    private void setData(int count, int range, float[] eventData) {
        float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry(eventData[i], mParties[i % mParties.length]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "颜色标识");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(getResources().getColor(R.color.noEvent));
        colors.add(getResources().getColor(R.color.work));
        colors.add(getResources().getColor(R.color.amuse));
        colors.add(getResources().getColor(R.color.life));
        colors.add(getResources().getColor(R.color.study));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //这个应当是和字体有关的
//        data.setValueTypeface(mTfLight);
        showPieChart.setData(data);
        showPieChart.highlightValues(null);

        showPieChart.invalidate();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    class RefreshChatDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initChart(getEventData(getEventStrings()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refreshChatDataReceiver != null) {
            getActivity().unregisterReceiver(refreshChatDataReceiver);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //友盟统计：由Activity和Fragment构成的页面需要这样写
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.main_stats));
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.main_stats));
    }
}
