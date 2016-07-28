package com.tryking.EasyList.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.db.dao.SpecificEventSourceDao;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.widgets.NumberPickerPopupWindow;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.db.dao.EverydayEventSourceDao;
import com.tryking.EasyList.db.table.EverydayEventSource;
import com.tryking.EasyList.db.table.SpecificEventSource;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends BaseActivity implements NumberPickerPopupWindow.FinishChooseListener, PopupWindow.OnDismissListener {
    @Bind(R.id.show_choose)
    TextView showChoose;
    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.tv_startTime)
    TextView tvStartTime;
    @Bind(R.id.ll_startTime)
    RelativeLayout llStartTime;
    @Bind(R.id.tv_endTime)
    TextView tvEndTime;
    @Bind(R.id.ll_endTime)
    RelativeLayout llEndTime;
    @Bind(R.id.et_print)
    EditText etPrint;
    @Bind(R.id.bt_complete)
    Button btComplete;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.rb_work)
    RadioButton rbWork;
    @Bind(R.id.rb_amuse)
    RadioButton rbAmuse;
    @Bind(R.id.rb_life)
    RadioButton rbLife;
    @Bind(R.id.rb_study)
    RadioButton rbStudy;
    @Bind(R.id.rg_parent)
    RadioGroup rgParent;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private static int START_CLICK = 0;
    private static int STOP_CLICK = 1;
    private int clickSign = START_CLICK;
    private NumberPickerPopupWindow mPicker;
    private int startChooseHour;
    private ArrayList<Integer> nothingStartInts;//未做事情的开始时间List
    private ArrayList<Integer> nothingEndInts;//未做事情的结束时间List
    private ArrayList<Integer> haveThingStartInts;//已做事情的开始时间List
    private ArrayList<Integer> haveThingEndInts;//已做事情的结束时间List
    private ArrayList<Integer> haveThingType;//已有事情的事件类型

    private String startTimes;//从SharedPreference中取出的开始时间汇总
    private String endTimes;//从SharedPreference中取出的结束时间汇总
    private String eventTypes;//从SharedPreference中取出的事件类型汇总

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        initView();
        startChooseHour = 1;
    }

    private void initView() {
        initToolBar();
//        setSupportActionBar(toolBar);
        rbWork.setChecked(true);

        startTimes = (String) SPUtils.get(AddActivity.this, ApplicationGlobal.START_TIMES, "");
        endTimes = (String) SPUtils.get(AddActivity.this, ApplicationGlobal.END_TIMES, "");
        eventTypes = (String) SPUtils.get(AddActivity.this, ApplicationGlobal.EVENT_TYPES, "");
//        Logger.e(startTimes + "start:::" + endTimes + "end::::type" + eventTypes);
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
        String[] type = CommonUtils.convertStrToArray(eventTypes);

        haveThingStartInts = new ArrayList<>();
        haveThingEndInts = new ArrayList<>();
        haveThingType = new ArrayList<>();
        if (starts.length == 1 && starts[0] == "") {
        } else {
            for (int i = 0; i < starts.length; i++) {
                haveThingStartInts.add(Integer.parseInt(starts[i]));
                haveThingEndInts.add(Integer.parseInt(ends[i]));
                haveThingType.add(Integer.parseInt(type[i]));
            }
        }
        nothingStartInts = new ArrayList<>();
        nothingEndInts = new ArrayList<>();
        if (haveThingStartInts.size() == 0) {
            nothingStartInts.add(0);
            nothingEndInts.add(2400);
        } else {
            if (haveThingStartInts.get(0) > 0) {
                nothingStartInts.add(0);
                nothingEndInts.add(haveThingStartInts.get(0));
            }
            for (int i = 0; i < haveThingStartInts.size() - 1; i++) {
                nothingStartInts.add(haveThingEndInts.get(i));
                nothingEndInts.add(haveThingStartInts.get(i + 1));
            }
            if (haveThingEndInts.get(haveThingEndInts.size() - 1) < 2400) {
                nothingStartInts.add(haveThingEndInts.get(haveThingEndInts.size() - 1));
                nothingEndInts.add(2400);
            }
        }
        String s = "";
        if (starts.length == 1 && starts[0] == "") {
            s = "00:00-24:00";
        } else {
            for (int i = 0; i < starts.length; i++) {
                int startI = Integer.parseInt(starts[i]);
                int startInt = 0;
                if (i < starts.length - 1) {
                    startInt = Integer.parseInt(starts[i + 1]);
                }
                int endInt = Integer.parseInt(ends[i]);
                if (i == 0 && startI > 0) {
                    s = "\n" + CommonUtils.addSignToStr("0000") + "  -  " + CommonUtils.addSignToStr(starts[0]);
                }
                if (startInt > endInt) {
                    if (s == "") {
                        s = s + CommonUtils.addSignToStr(ends[i]) + "  -  " + CommonUtils.addSignToStr(starts[i + 1]);
                    } else {
                        s = s + "\n" + CommonUtils.addSignToStr(ends[i]) + "  -  " + CommonUtils.addSignToStr(starts[i + 1]);
                    }
                }
                if (i == ends.length - 1 && endInt < 2400) {
                    if (ends.length == 1) {
                        s = CommonUtils.addSignToStr(ends[i]) + "  -  " + CommonUtils.addSignToStr("2400");
                    } else
                        s = s + "\n" + CommonUtils.addSignToStr(ends[i]) + "  -  " + CommonUtils.addSignToStr("2400");
                }
            }
//            Logger.e("haveThingStartInts:" + haveThingStartInts.toString());
//            Logger.e("haveThingEndInts:" + haveThingEndInts.toString());
//            Logger.e("haveThingType:" + haveThingType.toString());
//            Logger.e("nothingStartInts:" + nothingStartInts.toString());
//            Logger.e("nothingEndInts:" + noth
        }
        showChoose.setText(s == "" ? "您没有可选择的时间段" : "您可以选择的时间段有:\n");
        tvHint.setText(s);
    }

    /*
    初始化ToolBar
     */
    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left);
//        toolBar.setLogo(R.mipmap.ic_launcher);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.add_event);
//        toolBar.setSubtitle("今日");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });
    }

    @OnClick({R.id.ll_startTime, R.id.ll_endTime, R.id.bt_complete, R.id.bt_cancel})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ll_startTime:
                clickSign = START_CLICK;
                mPicker = NumberPickerPopupWindow.getInstance(AddActivity.this, 1, 24, 0, 60, 1);
                mPicker.setFinishChooseListener(this);
                mPicker.setOnDismissListener(this);
                mPicker.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_endTime:
                clickSign = STOP_CLICK;
                mPicker = NumberPickerPopupWindow.getInstance(AddActivity.this, 1, 24, 0, 60, startChooseHour);
                mPicker.setFinishChooseListener(this);
                mPicker.setOnDismissListener(this);
                mPicker.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.bt_complete:
                judgeDataIsLegal();
                break;
            case R.id.bt_cancel:
                this.finish();
            default:
                break;
        }
    }

    //判断数据是否合理
    private void judgeDataIsLegal() {
        String sStart = tvStartTime.getText().toString();
        String startTime = sStart.substring(0, 2) + sStart.substring(5, 7);
        String sEnd = tvEndTime.getText().toString();
        String endTime = sEnd.substring(0, 2) + sEnd.substring(5, 7);
        int start = Integer.parseInt(startTime);
        int end = Integer.parseInt(endTime);
        if (start >= end) {
            TT.showShort(AddActivity.this, "您似乎选择的不合理哦！");
        } else {
            int dataType = getDataType();
            String specificEvent = etPrint.getText().toString();
            if (haveThingStartInts.size() == 0) {
                haveThingStartInts.add(start);
                haveThingEndInts.add(end);
                haveThingType.add(dataType);
                startTimes = CommonUtils.listToString(haveThingStartInts);
                endTimes = CommonUtils.listToString(haveThingEndInts);
                eventTypes = CommonUtils.listToString(haveThingType);

//                Logger.e(startTimes + ":start");
//                Logger.e(endTimes + ":ends");
//                Logger.e(eventTypes + ":type");

                SPUtils.put(AddActivity.this, ApplicationGlobal.START_TIMES, startTimes);
                SPUtils.put(AddActivity.this, ApplicationGlobal.END_TIMES, endTimes);
                SPUtils.put(AddActivity.this, ApplicationGlobal.EVENT_TYPES, eventTypes);
                SPUtils.put(AddActivity.this, CommonUtils.intToStr(start), specificEvent);
//                Logger.e("在这儿");
                saveToDataBase(startTimes, endTimes, eventTypes);
                saveToDataBase(CommonUtils.intToStr(start), specificEvent);
                setResult(RESULT_OK);
                finish();
            } else {
                for (int i = 0; i < nothingStartInts.size(); i++) {
                    if (start >= nothingStartInts.get(i) && end <= nothingEndInts.get(i)) {
                        for (int j = 0; j < haveThingStartInts.size(); j++) {
                            if (start < haveThingStartInts.get(j)) {
//                                Logger.e(haveThingStartInts.get(j) + "");
                                List newHaveThingStartInts = CommonUtils.addIntToList(haveThingStartInts, j, start);
                                List newHaveThingEndInts = CommonUtils.addIntToList(haveThingEndInts, j, end);
                                List newHaveThingType = CommonUtils.addIntToList(haveThingType, j, dataType);
                                startTimes = CommonUtils.listToString(newHaveThingStartInts);
                                endTimes = CommonUtils.listToString(newHaveThingEndInts);
                                eventTypes = CommonUtils.listToString(newHaveThingType);
//                                Logger.e(startTimes + "::start");
//                                Logger.e(endTimes + "::ends");
//                                Logger.e(eventTypes + "::type");
                                SPUtils.put(AddActivity.this, ApplicationGlobal.START_TIMES, startTimes);
                                SPUtils.put(AddActivity.this, ApplicationGlobal.END_TIMES, endTimes);
                                SPUtils.put(AddActivity.this, ApplicationGlobal.EVENT_TYPES, eventTypes);
                                saveToDataBase(startTimes, endTimes, eventTypes);

                                //以开始时间作为名字存储事件
                                SPUtils.put(AddActivity.this, CommonUtils.intToStr(start), specificEvent);
                                saveToDataBase(CommonUtils.intToStr(start), specificEvent);

                                setResult(RESULT_OK);
                                finish();
                                return;
                            } else if (j == haveThingStartInts.size() - 1) {
                                haveThingStartInts.add(start);
                                haveThingEndInts.add(end);
                                haveThingType.add(dataType);
                                startTimes = CommonUtils.listToString(haveThingStartInts);
                                endTimes = CommonUtils.listToString(haveThingEndInts);
                                eventTypes = CommonUtils.listToString(haveThingType);
//                                Logger.e(startTimes + ":::start");
//                                Logger.e(endTimes + ":::ends");
//                                Logger.e(eventTypes + ":::type");

                                SPUtils.put(AddActivity.this, ApplicationGlobal.START_TIMES, startTimes);
                                SPUtils.put(AddActivity.this, ApplicationGlobal.END_TIMES, endTimes);
                                SPUtils.put(AddActivity.this, ApplicationGlobal.EVENT_TYPES, eventTypes);
                                saveToDataBase(startTimes, endTimes, eventTypes);

//                                Logger.e(String.valueOf(start) + "qqqqqqqqqq");

                                //以开始时间作为名字存储事件
                                SPUtils.put(AddActivity.this, CommonUtils.intToStr(start), specificEvent);
                                saveToDataBase(CommonUtils.intToStr(start), specificEvent);

                                setResult(RESULT_OK);
                                finish();
                                return;
                            }
                        }
                    }
                }
                TT.showShort(AddActivity.this, "您似乎选择的不合理哦！");
            }
        }
    }

    /*
    保存具体的事项到数据库
     */
    private void saveToDataBase(String startTime, String specificEvent) {
//        Logger.e("保存具体事项");
        SpecificEventSourceDao specificEventDao = new SpecificEventSourceDao(AddActivity.this);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
            String currentDate = (String) SPUtils.get(AddActivity.this, ApplicationGlobal.CURRENT_DATE, "");
            map.put("eventDate", currentDate);
            map.put("startTime", startTime);
            ArrayList<SpecificEventSource> specificEventList = (ArrayList<SpecificEventSource>) specificEventDao.query(map);
            if (specificEventList == null || specificEventList.size() <= 0) {
                //本时段未添加过事项
                specificEventDao.save(new SpecificEventSource(SystemInfo.getInstance(getApplicationContext()).getMemberId(), currentDate, startTime, specificEvent));
            } else {
                specificEventList.get(0).setSpecificEvent(specificEvent);
                specificEventDao.update(specificEventList.get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//            ArrayList<SpecificEventSource> specificEventSources = (ArrayList<SpecificEventSource>) specificEventDao.queryAll();
//            for (int i = 0; i < specificEventSources.size(); i++) {
//                Logger.e("Add保存后的数据" + specificEventSources.get(i).toString());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    /*
    将每日事件保存到数据库
     */
    private void saveToDataBase(String startTimes, String endTimes, String eventTypes) {
//        Logger.e("保存每日事件");
        EverydayEventSourceDao everydayEventDao = new EverydayEventSourceDao(AddActivity.this);
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
            String currentDate = (String) SPUtils.get(AddActivity.this, ApplicationGlobal.CURRENT_DATE, "");
            map.put("eventDate", currentDate);
            ArrayList<EverydayEventSource> todayEventList = (ArrayList<EverydayEventSource>) everydayEventDao.query(map);
            if (todayEventList == null || todayEventList.size() <= 0) {
                //今日未添加过事项
                everydayEventDao.save(new EverydayEventSource(SystemInfo.getInstance(getApplicationContext()).getMemberId(), currentDate, startTimes, endTimes, eventTypes));
            } else {
                todayEventList.get(0).setStartTimes(startTimes);
                todayEventList.get(0).setEndTimes(endTimes);
                todayEventList.get(0).setEventTypes(eventTypes);
                everydayEventDao.update(todayEventList.get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//            ArrayList<EverydayEventSource> specificEventSources = (ArrayList<EverydayEventSource>) everydayEventDao.queryAll();
//            for (int i = 0; i < specificEventSources.size(); i++) {
//                Logger.e("Add保存后的数据" + specificEventSources.get(i).toString());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    /*
    将事件类型添加到SharedPreference
     */
    private int getDataType() {
        int index = TodayEventData.TYPE_WORK;
        if (rgParent.getCheckedRadioButtonId() == rbWork.getId()) {
            index = TodayEventData.TYPE_WORK;
        } else if (rgParent.getCheckedRadioButtonId() == rbAmuse.getId()) {
            index = TodayEventData.TYPE_AMUSE;
        } else if (rgParent.getCheckedRadioButtonId() == rbLife.getId()) {
            index = TodayEventData.TYPE_LIFE;
        } else if (rgParent.getCheckedRadioButtonId() == rbStudy.getId()) {
            index = TodayEventData.TYPE_STUDY;
        }
        return index;
    }


    /**
     * 选择时间执行的回调接口
     *
     * @param currentHour
     * @param currentMinute
     */
    @Override
    public void onFinished(int currentHour, int currentMinute) {
//        Logger.e(currentHour + "::" + currentMinute);
        if (currentHour == 24) {
            currentHour = 0;
        }
        if (clickSign == START_CLICK) {
            tvStartTime.setText((currentHour < 10 ? "0" + currentHour : currentHour) + " : " + (currentMinute < 10 ? "0" + currentMinute : currentMinute));
            startChooseHour = currentHour;
        } else {
            if (currentHour == 0 && currentMinute == 0) {
                tvEndTime.setText("24 : 00");
            } else {
                tvEndTime.setText((currentHour < 10 ? "0" + currentHour : currentHour) + " : " + (currentMinute < 10 ? "0" + currentMinute : currentMinute));
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mPicker != null) {
                mPicker.dismiss();
                mPicker = null;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDismiss() {
        mPicker = null;
    }
}
