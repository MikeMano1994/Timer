package com.tryking.EasyList._activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.DayEventReturnBean;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList._bean.changeEventBean.ChangeDataReturnBean;
import com.tryking.EasyList._bean.loginBean.Event;
import com.tryking.EasyList.activity.AddActivity;
import com.tryking.EasyList.adapter.DayEventAdapterWithHeader;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewYesterdayActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.et_one_word)
    EditText etOneWord;
    @Bind(R.id.main_content)
    RecyclerView mainContent;
    @Bind(R.id.show_content)
    LinearLayout showContent;
    @Bind(R.id.show_no_net)
    RelativeLayout showNoNet;
    @Bind(R.id.show_server_error)
    RelativeLayout showServerError;
    @Bind(R.id.show_no_content)
    RelativeLayout showNoContent;
    @Bind(R.id.bt_reLoad)
    Button btReLoad;

    private List<TodayEventData> mDatas;
    private String startTimes;
    private String endTimes;
    private String eventTypes;
    private String speEvents;
    private HashMap<String, String> specificEvents;
    private DayEventAdapterWithHeader adapterWithHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_yesterday);
        ButterKnife.bind(this);

        init();
    }

    @OnClick({R.id.bt_reLoad, R.id.et_one_word})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_reLoad:
                getYesterdayDataFromServer();
                break;
            case R.id.tv_one_word:
                TT.showShort(this, "我要修改");
                break;
            default:
                break;
        }
    }

    private void init() {
        initToolBar();
        startTimes = "";
        endTimes = "";
        eventTypes = "";
        specificEvents = new HashMap<>();
        getYesterdayDataFromServer();
    }

    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
//        toolBar.setLogo(R.mipmap.ic_launcher);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.view_yesterday);
//        toolBar.setSubtitle("今日");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewYesterdayActivity.this.finish();
            }
        });
        toolBar.inflateMenu(R.menu.view_yesterday);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent intent = new Intent(ViewYesterdayActivity.this, AddActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ApplicationGlobal.START_TIMES, startTimes);
                        bundle.putString(ApplicationGlobal.END_TIMES, endTimes);
                        bundle.putString(ApplicationGlobal.EVENT_TYPES, eventTypes);
                        intent.putExtra(Constants.ViewYesterday.INTENT_ARGUMENT, bundle);
                        Logger.e("yes:" + bundle.toString());
                        startActivityForResult(intent, Constants.ViewYesterday.REQUEST_ViewYesterday_To_Add);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /*
    从服务端获取昨日的数据
     */
    private void getYesterdayDataFromServer() {
        showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("date", CommonUtils.getPreviousDay((String) SPUtils.get(getApplicationContext(), ApplicationGlobal.CURRENT_DATE, ""), -1));
        String url = InterfaceURL.viewDayData;
        JsonBeanRequest<DayEventReturnBean> dayEventRequest = new JsonBeanRequest<>(url, params, DayEventReturnBean.class, new Response.Listener<DayEventReturnBean>() {
            @Override
            public void onResponse(DayEventReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.what = Constants.ViewYesterday.GET_YESTERDAY_DATA_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = Constants.ViewYesterday.GET_YESTERDAY_DATA_FAILURE;
                    msg.obj = response.getMsg();
                }
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.getMessage());
                Message msg = new Message();
                msg.what = Constants.requestException;
                msg.obj = error.getMessage();
                mHandler.sendMessage(msg);
            }
        });
        addToRequestQueue(dayEventRequest);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissLoadingDialog();
            switch (msg.what) {
                case Constants.ViewYesterday.GET_YESTERDAY_DATA_SUCCESS:
                    DayEventReturnBean dayEventBean = (DayEventReturnBean) msg.obj;
                    if (dayEventBean == null || dayEventBean.getEventList() == null) {
                        showView(showNoContent);
                    } else {
                        hideAbnormalViews();
                        List<Event> eventList = dayEventBean.getEventList();
                        initView(eventList);
                    }
                    break;
                case Constants.ViewYesterday.GET_YESTERDAY_DATA_FAILURE:
                    showView(showServerError);
                    String message = msg.obj.toString();
                    TT.showShort(getApplicationContext(), message);
                    break;
                case Constants.requestException:
                    if (msg.obj != null && msg.obj.toString().contains("java.net.ConnectException")) {
                        TT.showShort(getApplicationContext(), "网络异常");
                        showView(showNoNet);
                    } else {
                        TT.showShort(getApplicationContext(), "服务器开小差啦～");
                        showView(showServerError);
                    }
                    break;
            }
        }
    };

    /*
    显示传入的view
     */
    private void showView(RelativeLayout view) {
        switch (view.getId()) {
            case R.id.show_no_content:
                showNoContent.setVisibility(View.VISIBLE);
                showNoNet.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showContent.setVisibility(View.GONE);
                break;
            case R.id.show_no_net:
                showNoNet.setVisibility(View.VISIBLE);
                showNoContent.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showContent.setVisibility(View.GONE);
                break;
            case R.id.show_server_error:
                showServerError.setVisibility(View.VISIBLE);
                showNoNet.setVisibility(View.GONE);
                showNoContent.setVisibility(View.GONE);
                showContent.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /*
       把异常的view全部隐藏
        */
    private void hideAbnormalViews() {
        showNoContent.setVisibility(View.GONE);
        showNoNet.setVisibility(View.GONE);
        showServerError.setVisibility(View.GONE);
        showContent.setVisibility(View.VISIBLE);
    }

    private void initView(List<Event> eventList) {
        mDatas = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            TodayEventData todayEventData = new TodayEventData(Integer.parseInt(eventList.get(i).getEventtypes()), eventList.get(i).getStarttime(),
                    eventList.get(i).getEndtime(), eventList.get(i).getRecord());
            if (i == eventList.size() - 1) {
                startTimes += eventList.get(i).getStarttime();
                endTimes += eventList.get(i).getEndtime();
                eventTypes += "000" + eventList.get(i).getEventtypes();
            } else {
                startTimes += eventList.get(i).getStarttime() + ",";
                endTimes += eventList.get(i).getEndtime() + ",";
                eventTypes += "000" + eventList.get(i).getEventtypes() + ",";
            }
            specificEvents.put(eventList.get(i).getStarttime(), eventList.get(i).getRecord());
            mDatas.add(todayEventData);
        }
        mainContent.setLayoutManager(new LinearLayoutManager(ViewYesterdayActivity.this));
        adapterWithHeader = new DayEventAdapterWithHeader(ViewYesterdayActivity.this, mDatas, false);
        mainContent.setAdapter(adapterWithHeader);
        Logger.e("old:" + specificEvents.toString());
    }

    public Map<String, String> sortMapByKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                int intKey1 = 0, intKey2 = 0;
                try {
                    intKey1 = Integer.parseInt(key1);
                    intKey2 = Integer.parseInt(key2);
                } catch (Exception e) {
                    intKey1 = 0;
                    intKey2 = 0;
                }
                return intKey1 - intKey2;
            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e(requestCode + "::" + resultCode);
        if (requestCode == Constants.ViewYesterday.REQUEST_ViewYesterday_To_Add && resultCode == Constants.ViewYesterday.RESULT_Add_To_ViewYesterday) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bundle bundle = extras.getBundle(Constants.ViewYesterday.INTENT_ARGUMENT);
                startTimes = bundle.getString(ApplicationGlobal.START_TIMES);
                endTimes = bundle.getString(ApplicationGlobal.END_TIMES);
                eventTypes = bundle.getString(ApplicationGlobal.EVENT_TYPES);
                String addStartTime = bundle.getString(Constants.ViewYesterday.INTENT_ARGUMENT_START_TIME);
                String addSpeEvent = bundle.getString(Constants.ViewYesterday.INTENT_ARGUMENT_SPECIFIC_EVENT);
//                Logger.e(startTimes + "::" + endTimes + "::" + eventTypes + "::" + addStartTime + "::" + addSpeEvent);
                specificEvents.put(addStartTime, addSpeEvent);
//                Logger.e("原来的：" + specificEvents.toString());
                Map<String, String> newMap = sortMapByKey(specificEvents);
//                Logger.e("新的：" + newMap.toString());
                //这样加进去和上面的map顺序是不一样的。所以不能这样加，直接用上面新的就好
//                specificEvents.clear();
//                specificEvents.putAll(newMap);
                Logger.e("后来的：" + newMap.toString());
                refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);
                Set<String> keySet = newMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                speEvents = "";
                while (iterator.hasNext()) {
                    speEvents += newMap.get(iterator.next()) + Constants.SPLIT_SPECIFIC_EVENT_STRING;
                }
                speEvents = speEvents.substring(0, speEvents.length() - Constants.SPLIT_SPECIFIC_EVENT_STRING.length());
                Logger.e("Spe:" + speEvents);
                changeDataToServer();
            }
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
            String s = specificEvents.get(starts[i]);
            event[i] = s;
        }
        mDatas.clear();
        for (int i = 0; i < starts.length; i++) {
            if (starts[0] == "" && ends[0] == "") {
                TodayEventData data1 = new TodayEventData(0, "0000", "2400", "未添加事件");
                mDatas.add(data1);
            } else {
                if (i == 0 && Integer.parseInt(starts[0]) > 0) {
                    TodayEventData data2 = new TodayEventData(0, "0000", starts[0], "未添加事件");
                    mDatas.add(data2);
                }
                TodayEventData data3 = new TodayEventData(Integer.parseInt(type[i]), starts[i], ends[i], event[i]);
                mDatas.add(data3);
                if (i != starts.length - 1 && (!ends[i].equals(starts[i + 1]))) {
                    TodayEventData data1 = new TodayEventData(0, ends[i], starts[i + 1], "未添加事件");
                    mDatas.add(data1);
                }
                if (i == starts.length - 1 && Integer.parseInt(ends[ends.length - 1]) < 2400) {
                    TodayEventData data2 = new TodayEventData(0, ends[ends.length - 1], "2400", "未添加事件");
                    mDatas.add(data2);
                }
            }
        }
        adapterWithHeader.refresh(mDatas);
    }

    /*
    向服务器发送数据
     */
    private void changeDataToServer() {
//        showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("date", CommonUtils.getPreviousDay((String) SPUtils.get(getApplicationContext(), ApplicationGlobal.CURRENT_DATE, ""), -1));
        params.put("startTimes", startTimes);
        params.put("endTimes", endTimes);
        params.put("eventTypes", eventTypes);
        params.put("specificEvents", speEvents);
        Logger.e("开始改变数据" + params.toString());

        String url = InterfaceURL.changeData;
        JsonBeanRequest<ChangeDataReturnBean> changeDataRequest = new JsonBeanRequest<>(url, params, ChangeDataReturnBean.class, new Response.Listener<ChangeDataReturnBean>() {
            @Override
            public void onResponse(ChangeDataReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.what = Constants.ChangeData.changeSuccess;
                    msg.obj = response;
                } else {
                    msg.what = Constants.ChangeData.changeFailure;
                    msg.obj = response.getMsg();
                }
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.getMessage());
                Message msg = new Message();
                msg.what = Constants.requestException;
                msg.obj = error.getMessage();
                mHandler.sendMessage(msg);

//                com.android.volley.TimeoutError
            }
        });
        addToRequestQueue(changeDataRequest);
    }
}
