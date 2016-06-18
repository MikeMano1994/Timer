package com.tryking.timer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class ShowDataActivity extends AppCompatActivity {

    @Bind(R.id.rv_showData)
    RecyclerView rvShowData;

    List<TodayEventData> eventDatas = new ArrayList<>();
    private TodayEventAdapter adapter;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String getDate = intent.getStringExtra("date");
        date = CommonUtils.clearHanZiFromStr(getDate);
        rvShowData.setLayoutManager(new LinearLayoutManager(ShowDataActivity.this));
        adapter = new TodayEventAdapter(new WeakReference<Context>(ShowDataActivity.this), eventDatas);
        rvShowData.setAdapter(adapter);
        getDataFromDatabase(date);
    }

    /*
    从数据库中获取数据
     */
    private void getDataFromDatabase(String date) {
//        String currentDate = (String) SPUtils.get(ShowDataActivity.this, "currentDate", "");
//        Logger.e("currentDate:" + currentDate + currentDate.length());
//        Logger.e("GotDate:" + date + date.length());
        EverydayEventSourceDao everydayEventDao = new EverydayEventSourceDao(ShowDataActivity.this);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", "");
            map.put("eventDate", date);
            List<EverydayEventSource> eventList = everydayEventDao.query(map);

            if (eventList == null || eventList.size() <= 0) {
                Logger.e("未添加过事件");
            } else {
                Logger.e("有事件");
                for (int i = 0; i < eventList.size(); i++) {
                    Logger.e(eventList.get(i).toString());
                    String startTimes = eventList.get(i).getStartTimes();
                    String endTimes = eventList.get(i).getEndTimes();
                    String eventTypes = eventList.get(i).getEventTypes();
                    Logger.e(startTimes + "::" + endTimes);
                    refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            SpecificEventSourceDao specificEventDao = new SpecificEventSourceDao(ShowDataActivity.this);
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
                    event[i]=specificEvent;
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
}
