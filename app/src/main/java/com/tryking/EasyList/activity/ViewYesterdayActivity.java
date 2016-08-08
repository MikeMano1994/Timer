package com.tryking.EasyList.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.tryking.EasyList.widgets.CommonDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
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

public class ViewYesterdayActivity extends BaseActivity implements DayEventAdapterWithHeader.onHaveEventItemLongClickListener {

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
    @Bind(R.id.show_no_data)
    RelativeLayout showNoData;
    @Bind(R.id.bt_reLoad)
    Button btReLoad;
    @Bind(R.id.show_no_content)
    RelativeLayout showNoContent;
    @Bind(R.id.add_one_word)
    ImageView addOneWord;

    private List<TodayEventData> mDatas;
    private String startTimes;
    private String endTimes;
    private String eventTypes;
    private String speEvents;
    private HashMap<String, String> specificEvents;
    private DayEventAdapterWithHeader adapterWithHeader;
    private boolean isEdit = false;
    private boolean isAllowAdd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_yesterday);
        ButterKnife.bind(this);

        init();
    }

    @OnClick({R.id.bt_reLoad, R.id.add_one_word})
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_reLoad:
                getYesterdayDataFromServer();
                break;
            case R.id.add_one_word:
                if (isEdit) {
                    addOneWord.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_primary_dark_18dp));
                    etOneWord.setEnabled(false);
                    isEdit = false;
                    if (!etOneWord.getText().toString().equals("")) {
                        addOneWordToServer(etOneWord.getText().toString());
                    }
                } else {
                    addOneWord.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_primary_dark_24dp));
                    etOneWord.setEnabled(true);
                    etOneWord.setFocusable(true);
                    etOneWord.requestFocus();
                    InputMethodManager imm = (InputMethodManager) etOneWord.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    isEdit = true;
                }
                break;
            default:
                break;
        }
    }

    private void init() {
        showView(showNoContent);
        initToolBar();
        startTimes = "";
        endTimes = "";
        eventTypes = "";
        specificEvents = new HashMap<>();
        mDatas = new ArrayList<>();
        mainContent.setLayoutManager(new LinearLayoutManager(ViewYesterdayActivity.this));
        adapterWithHeader = new DayEventAdapterWithHeader(ViewYesterdayActivity.this, mDatas, false);
        adapterWithHeader.setOnHaveEventItemLongClickListener(this);
        mainContent.setAdapter(adapterWithHeader);
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
                        if (isAllowAdd) {
                            Intent intent = new Intent(ViewYesterdayActivity.this, AddActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(ApplicationGlobal.START_TIMES, startTimes);
                            bundle.putString(ApplicationGlobal.END_TIMES, endTimes);
                            bundle.putString(ApplicationGlobal.EVENT_TYPES, eventTypes);
                            intent.putExtra(Constants.ViewYesterday.INTENT_ARGUMENT, bundle);
                            startActivityForResult(intent, Constants.ViewYesterday.REQUEST_ViewYesterday_To_Add);
                        } else {
                            TT.showShort(getApplicationContext(), "连接失败，不能修改昨日事件哟~");
                        }
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
                msg.what = Constants.ViewYesterday.GET_DATA_REQUEST_ERROR;
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
                        showView(showNoData);
                    } else {
                        hideAbnormalViews();
                        List<Event> eventList = dayEventBean.getEventList();
                        Logger.e("OneWord:" + dayEventBean.getOneWord());
                        if (dayEventBean.getOneWord() != null && !dayEventBean.getOneWord().equals("")) {
                            etOneWord.setText(dayEventBean.getOneWord());
                        }
                        initView(eventList);
                    }
                    //获取数据成功后，允许用户添加昨日事件
                    isAllowAdd = true;
                    break;
                case Constants.ViewYesterday.GET_YESTERDAY_DATA_FAILURE:
                    showView(showServerError);
                    String message = msg.obj.toString();
                    TT.showShort(getApplicationContext(), message);
                    break;
                case Constants.ViewYesterday.CHANGE_DATA_SUCCESS:
                    //改变昨日数据成功
                    break;
                case Constants.ViewYesterday.CHANGE_DATA_FAILURE:
                    //改变昨日数据失败
                    break;
                case Constants.ViewYesterday.GET_DATA_REQUEST_ERROR:
                    if (msg.obj != null && msg.obj.toString().contains("java.net.ConnectException")) {
                        TT.showShort(getApplicationContext(), "网络异常");
                        showView(showNoNet);
                    } else {
                        TT.showShort(getApplicationContext(), "服务器开小差啦～");
                        showView(showServerError);
                    }
                    break;
                case Constants.ViewYesterday.CHANGE_DATA_REQUEST_ERROR:
                    if (msg.obj != null && msg.obj.toString().contains("java.net.ConnectException")) {
                        TT.showShort(getApplicationContext(), "网络异常，数据无法同步");
                        showView(showNoNet);
                    } else {
                        TT.showShort(getApplicationContext(), "服务器开小差啦～数据可能无法同步");
                        showView(showServerError);
                    }
                    break;
                default:
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
                showNoData.setVisibility(View.GONE);
                showNoNet.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showContent.setVisibility(View.GONE);
                break;
            case R.id.show_no_data:
                showNoData.setVisibility(View.VISIBLE);
                showNoNet.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showNoContent.setVisibility(View.GONE);
                showContent.setVisibility(View.GONE);
                break;
            case R.id.show_no_net:
                showNoNet.setVisibility(View.VISIBLE);
                showNoData.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showNoContent.setVisibility(View.GONE);
                showContent.setVisibility(View.GONE);
                break;
            case R.id.show_server_error:
                showServerError.setVisibility(View.VISIBLE);
                showNoNet.setVisibility(View.GONE);
                showNoData.setVisibility(View.GONE);
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
        showNoData.setVisibility(View.GONE);
        showNoNet.setVisibility(View.GONE);
        showServerError.setVisibility(View.GONE);
        showContent.setVisibility(View.VISIBLE);
    }

    private void initView(List<Event> eventList) {
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
        adapterWithHeader.refresh(mDatas);
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
                refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);
                Set<String> keySet = newMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                speEvents = "";
                while (iterator.hasNext()) {
                    speEvents += newMap.get(iterator.next()) + Constants.SPLIT_SPECIFIC_EVENT_STRING;
                }
                speEvents = speEvents.substring(0, speEvents.length() - Constants.SPLIT_SPECIFIC_EVENT_STRING.length());
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
        Logger.e("到这里了。。。");
        adapterWithHeader.refresh(mDatas);
        hideAbnormalViews();
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
        Logger.e("开始改变昨日数据" + params.toString());

        String url = InterfaceURL.changeData;
        JsonBeanRequest<ChangeDataReturnBean> changeDataRequest = new JsonBeanRequest<>(url, params, ChangeDataReturnBean.class, new Response.Listener<ChangeDataReturnBean>() {
            @Override
            public void onResponse(ChangeDataReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.what = Constants.ViewYesterday.CHANGE_DATA_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = Constants.ViewYesterday.CHANGE_DATA_FAILURE;
                    msg.obj = response.getMsg();
                }
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.getMessage());
                Message msg = new Message();
                msg.what = Constants.ViewYesterday.CHANGE_DATA_REQUEST_ERROR;
                msg.obj = error.getMessage();
                mHandler.sendMessage(msg);
            }
        });
        addToRequestQueue(changeDataRequest);
    }

    @Override
    public void onHaveEventItemLongClick(int position, final String startTime, final String endTime) {
        final CommonDialog commonDialog = new CommonDialog(this);
        commonDialog.setDialogContent(null, "删除\t\t" + CommonUtils.addSignToStr(startTime) + " - " + CommonUtils.addSignToStr(endTime) + ("\t\t这条记录").replaceAll(".{1}(?!$)", "$0 "), null, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newStarts;
                        String newEnds;
                        String newTypes = "";
                        if (startTimes.contains("," + startTime)) {
                            newStarts = CommonUtils.deleteStr(startTimes, "," + startTime);
                            newEnds = CommonUtils.deleteStr(endTimes, "," + endTime);
                            int i = startTimes.indexOf("," + startTime);
                            if (i != -1) {
                                newTypes = eventTypes.substring(0, i) + eventTypes.substring(i + 5);
                            }
                        } else {
                            newStarts = CommonUtils.deleteStr(startTimes, startTime);
                            newEnds = CommonUtils.deleteStr(endTimes, endTime);
                            int i = startTimes.indexOf(startTime);
                            if (i != -1) {
                                newTypes = eventTypes.substring(0, i) + eventTypes.substring(i + 4);
                            }
                            if (newStarts.startsWith(",") && newEnds.startsWith(",") && newTypes.startsWith(",")) {
                                newStarts = newStarts.replaceFirst(",", "");
                                newEnds = newEnds.replaceFirst(",", "");
                                newTypes = newTypes.replaceFirst(",", "");
                            }
                        }
                        startTimes = newStarts;
                        endTimes = newEnds;
                        eventTypes = newTypes;
                        Logger.e(specificEvents.toString());
                        specificEvents.remove(startTime);
                        Logger.e(specificEvents.toString());

                        Map<String, String> newMap = sortMapByKey(specificEvents);

                        Set<String> keySet = newMap.keySet();
                        Iterator<String> iterator = keySet.iterator();
                        speEvents = "";
                        while (iterator.hasNext()) {
                            speEvents += newMap.get(iterator.next()) + Constants.SPLIT_SPECIFIC_EVENT_STRING;
                        }
                        speEvents = speEvents.substring(0, speEvents.length() - Constants.SPLIT_SPECIFIC_EVENT_STRING.length());

                        changeDataToServer();
                        refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);
                        commonDialog.dismiss();
                    }
                }, null);
        commonDialog.show();
    }

    /*
    将每日一句上传到服务器
     */
    private void addOneWordToServer(String oneWord) {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("date", CommonUtils.getPreviousDay((String) SPUtils.get(getApplicationContext(), ApplicationGlobal.CURRENT_DATE, ""), -1));
        params.put("dataType", Constants.oneWordDataType);
        params.put("oneWord", oneWord);
        String url = InterfaceURL.addOneWord;
        JsonBeanRequest<ChangeDataReturnBean> changeDataRequest = new JsonBeanRequest<>(url, params, ChangeDataReturnBean.class, new Response.Listener<ChangeDataReturnBean>() {
            @Override
            public void onResponse(ChangeDataReturnBean response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        addToRequestQueue(changeDataRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写(不包含Fragment)
        MobclickAgent.onPageStart(getString(R.string.view_yesterday));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.view_yesterday));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }
}
