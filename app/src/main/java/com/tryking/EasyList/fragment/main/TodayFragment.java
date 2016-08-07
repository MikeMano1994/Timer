package com.tryking.EasyList.fragment.main;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList._bean.changeEventBean.ChangeDataReturnBean;
import com.tryking.EasyList._bean.changeEventBean.TransferData;
import com.tryking.EasyList.activity.AddActivity;
import com.tryking.EasyList.adapter.TodayEventAdapter;
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.base.String4Broad;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.db.dao.EverydayEventSourceDao;
import com.tryking.EasyList.db.dao.SpecificEventSourceDao;
import com.tryking.EasyList.db.table.EverydayEventSource;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.CommonDialog;
import com.tryking.EasyList.widgets.CountDownTextView;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayFragment extends BaseFragment implements TodayEventAdapter.onHaveEventItemLongClickListener {
    @Bind(R.id.event_content)
    RecyclerView eventContent;
    @Bind(R.id.actionButton)
    FloatingActionButton actionButton;
    @Bind(R.id.tv_awoke)
    CountDownTextView tvAwoke;
    @Bind(R.id.hint)
    LinearLayout hint;
    @Bind(R.id.tv_one_word)
    TextView tvOneWord;
    @Bind(R.id.add_one_word)
    ImageView addOneWord;
    @Bind(R.id.ll_one_word)
    LinearLayout llOneWord;


    private TodayEventAdapter todayEventAdapter;
    private static final int REQUEST_ADD_CODE = 0;//添加事项请求吗
    private List<TodayEventData> todayEventDatas = new ArrayList<>();
    private RequestQueue mQueue;
    private String currentDate;
    private TransferData transferData;
    private boolean isTryOutAccount;
    private EditText etOneWord;
    private boolean isShowNetDisable = false;
    private boolean isShowServerDisable = false;

    @OnClick({R.id.actionButton, R.id.add_one_word})
    void click(View view) {
        switch (view.getId()) {
            case R.id.actionButton:
                Animator animator = createAnimator(view);
                if (animator != null) {
                    animator.start();
                }
                //这个是Activity启动动画，有点儿问题，先不用了，而且这个一般是那种两个Activity中都有同一个图片的那种，图片会放大跳转
//                Intent intent = new Intent(getActivity(), AddActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
//                ActivityCompat.startActivityForResult(getActivity(), intent, REQUEST_ADD_CODE, options.toBundle());
                startActivityForResult(new Intent(getActivity(), AddActivity.class), REQUEST_ADD_CODE);
                break;
            case R.id.add_one_word:
                View oneWord = LayoutInflater.from(getContext()).inflate(R.layout.add_one_word, null);
                etOneWord = (EditText) oneWord.findViewById(R.id.et_one_word);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("一句话总结今日")
                        .setView(oneWord);
                setPosAndNegButton(builder);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                etOneWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });
                //只用上面这个就很管用
//                etOneWord.setFocusable(true);
//                etOneWord.setFocusableInTouchMode(true);
//                etOneWord.requestFocus();
                break;
        }
    }

    /*
    给alert设置按钮
     */
    private void setPosAndNegButton(final AlertDialog.Builder builder) {
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!etOneWord.getText().toString().equals("")) {
                    tvOneWord.setText(etOneWord.getText().toString());
                    addOneWordToServer(etOneWord.getText().toString());
                    SPUtils.put(getActivity(), Constants.SP_TODAY_ONE_WORD, etOneWord.getText().toString());
                }
            }
        })
                .setNegativeButton("取消", null);
    }

    /*
    将每日一句上传到服务器
     */
    private void addOneWordToServer(String oneWord) {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getActivity()).getMemberId());
        params.put("date", (String) SPUtils.get(getActivity(), ApplicationGlobal.CURRENT_DATE, ""));
        params.put("dataType", Constants.oneWordDataType);
        params.put("oneWord", oneWord);
        String url = InterfaceURL.addOneWord;
        JsonBeanRequest<ChangeDataReturnBean> changeDataRequest = new JsonBeanRequest<>(url, params, ChangeDataReturnBean.class, new Response.Listener<ChangeDataReturnBean>() {
            @Override
            public void onResponse(ChangeDataReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.what = Constants.ChangeData.addOneWordSuccess;
                    msg.obj = response;
                } else {
                    msg.what = Constants.ChangeData.addOneWordFailure;
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
        addToRequestQueue(changeDataRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_CODE && resultCode == -1) {
            {
                handlerData(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    处理返回的数据
     */
    private void handlerData(Intent data) {
        String startTimes = (String) SPUtils.get(getActivity(), ApplicationGlobal.START_TIMES, "");
        String endTimes = (String) SPUtils.get(getActivity(), ApplicationGlobal.END_TIMES, "");
        String eventTypes = (String) SPUtils.get(getActivity(), ApplicationGlobal.EVENT_TYPES, "");


        transferData = new TransferData();
        transferData.setStartTimes(startTimes);
        transferData.setEndTimes(endTimes);
        transferData.setEventTypes(eventTypes);
        transferData.setSpecificEvents("");

        String[] starts = CommonUtils.convertStrToArray(startTimes);
        for (int i = 0; i < starts.length; i++) {
            String s = (String) SPUtils.get(getActivity(), starts[i], "");
            if (!starts[0].equals("")) {
                if (i == 0) {
                    transferData.setSpecificEvents(s);
                } else {
                    transferData.setSpecificEvents(transferData.getSpecificEvents() + Constants.SPLIT_SPECIFIC_EVENT_STRING + s);
                }
            }
        }
        refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);
        if (!isTryOutAccount)
            changeDataToServer();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
    }

    private void initDatas() {
        long intervalTime = getIntervalTime();
        tvAwoke.setTextPreTime("今天还可以和时间做朋友：");
        tvAwoke.setIntervalTime(intervalTime, true);

        Logger.e("当前用户：" + SystemInfo.getInstance(getContext()).getMemberId());
        //如果是试用账户就不上传服务器
        isTryOutAccount = SystemInfo.getInstance(getContext()).getMemberId().equals(Constants.TRY_OUT_ACCOUNT)
                || SystemInfo.getInstance(getContext()).getMemberId().equals("") ? true : false;

        String startTimes = (String) SPUtils.get(getActivity(), ApplicationGlobal.START_TIMES, "");
        String endTimes = (String) SPUtils.get(getActivity(), ApplicationGlobal.END_TIMES, "");
        String eventTypes = (String) SPUtils.get(getActivity(), ApplicationGlobal.EVENT_TYPES, "");

        String oneWord = (String) SPUtils.get(getActivity(), Constants.SP_TODAY_ONE_WORD, "");

        transferData = new TransferData();
        transferData.setStartTimes(startTimes);
        transferData.setEndTimes(endTimes);
        transferData.setEventTypes(eventTypes);
        transferData.setSpecificEvents("");

        String[] starts = CommonUtils.convertStrToArray(startTimes);
        for (int i = 0; i < starts.length; i++) {
            String s = (String) SPUtils.get(getActivity(), starts[i], "");
            if (!starts[0].equals("")) {
                if (i == 0) {
                    transferData.setSpecificEvents(s);
                } else {
                    transferData.setSpecificEvents(transferData.getSpecificEvents() + Constants.SPLIT_SPECIFIC_EVENT_STRING + s);
                }
            }
        }
        refreshRecyclerViewDataByString(startTimes, endTimes, eventTypes);

        if (oneWord != null && !oneWord.equals("")) {
            tvOneWord.setText(oneWord);
        }
    }

    /*
    向服务器发送数据
     */
    private void changeDataToServer() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getActivity()).getMemberId());
        params.put("date", (String) SPUtils.get(getActivity(), ApplicationGlobal.CURRENT_DATE, ""));
        params.put("startTimes", transferData.getStartTimes());
        params.put("endTimes", transferData.getEndTimes());
        params.put("eventTypes", transferData.getEventTypes());
        params.put("specificEvents", transferData.getSpecificEvents());
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

    /*
    获取倒计时总时长
     */
    private long getIntervalTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date nowDate = new Date(System.currentTimeMillis());
        String nowDateStr = dateFormat.format(nowDate);
        currentDate = nowDateStr.substring(0, 4) + nowDateStr.substring(5, 7) + nowDateStr.substring(8, 10);
        long time = 0;
        try {
            Date deadLineTime = dateFormat.parse(nowDateStr.substring(0, 10) + "-24-00-00");
            time = deadLineTime.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long interval = (time - System.currentTimeMillis()) / 1000;
        return interval;
    }

    /*
    获取网络时间
     */
    private String requestNetTime() {
        try {
            URL url = new URL("http://www.baidu.com");
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            long date = urlConnection.getDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
            Date date1 = new Date(date);
            Logger.e(simpleDateFormat.format(date1));
            Date date2 = new Date(System.currentTimeMillis());
//            Logger.e("手机当前时间：：" + date2.getYear() + "年" + date2.getMonth() + "月" + date2.getDay() + "月" + date2.getHours() + "时" + date2.getMinutes() + "分" + date2.getSeconds() + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            String s = (String) SPUtils.get(getActivity(), starts[i], "");
            event[i] = s;
        }
        todayEventDatas.clear();
        for (int i = 0; i < starts.length; i++) {
            if (starts[0] == "" && ends[0] == "") {
                TodayEventData data1 = new TodayEventData(0, "0000", "2400", "未添加事件");
                todayEventDatas.add(data1);
                hint.setVisibility(View.VISIBLE);
            } else {
                hint.setVisibility(View.GONE);
//                Logger.e(starts[0] + "starts{0}");
                if (i == 0 && Integer.parseInt(starts[0]) > 0) {
                    TodayEventData data2 = new TodayEventData(0, "0000", starts[0], "未添加事件");
                    todayEventDatas.add(data2);
                }
                TodayEventData data3 = new TodayEventData(Integer.parseInt(type[i]), starts[i], ends[i], event[i]);
                todayEventDatas.add(data3);
                if (i != starts.length - 1 && (!ends[i].equals(starts[i + 1]))) {
                    TodayEventData data1 = new TodayEventData(0, ends[i], starts[i + 1], "未添加事件");
                    todayEventDatas.add(data1);
                }
                if (i == starts.length - 1 && Integer.parseInt(ends[ends.length - 1]) < 2400) {
                    TodayEventData data2 = new TodayEventData(0, ends[ends.length - 1], "2400", "未添加事件");
                    todayEventDatas.add(data2);
                }
            }
        }
        todayEventAdapter.refresh(todayEventDatas);
    }

    private void initViews() {
        mQueue = Volley.newRequestQueue(getActivity());
        todayEventAdapter = new TodayEventAdapter(new WeakReference<Context>(getActivity()), getActivity(), todayEventDatas);
        todayEventAdapter.setOnHaveEventItemLongClickListener(this);
        eventContent.setLayoutManager(new LinearLayoutManager(getActivity()));
//        eventContent.addItemDecoration(new MyItemDividerDecoration(getActivity(), MyItemDividerDecoration.VERTICAL_LIST));
        eventContent.setHasFixedSize(true);
        eventContent.setItemAnimator(new DefaultItemAnimator());
        eventContent.setAdapter(todayEventAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator createAnimator(View v) {
        if (Build.VERSION.SDK_INT >= 21) {
            Animator animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth() / 2, v.getHeight() / 2, 0, v.getWidth());
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(2000);
            return animator;
        }
        return null;
    }


    @Override
    public void onHaveEventItemLongClick(int position, final String startTime, final String endTime) {
        final CommonDialog commonDialog = new CommonDialog(getActivity());
        commonDialog.setDialogContent(null, "删除\t\t" + CommonUtils.addSignToStr(startTime) + " - " + CommonUtils.addSignToStr(endTime) + ("\t\t这条记录").replaceAll(".{1}(?!$)", "$0 "), null, null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String startTimes = (String) SPUtils.get(getActivity(), ApplicationGlobal.START_TIMES, "");
                        String endTimes = (String) SPUtils.get(getActivity(), ApplicationGlobal.END_TIMES, "");
                        String eventTypes = (String) SPUtils.get(getActivity(), ApplicationGlobal.EVENT_TYPES, "");

                        //替换字符串的一种方式
//                Pattern compileStart = Pattern.compile(startTimes);
//                Matcher matcherStart = compileStart.matcher(startTime);
//                String s = matcherStart.replaceFirst("1111");

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
                        SPUtils.put(getActivity(), ApplicationGlobal.START_TIMES, newStarts);
                        SPUtils.put(getActivity(), ApplicationGlobal.END_TIMES, newEnds);
                        SPUtils.put(getActivity(), ApplicationGlobal.EVENT_TYPES, newTypes);
                        saveToDataBase(newStarts, newEnds, newTypes);

                        transferData = new TransferData();
                        transferData.setStartTimes(newStarts);
                        transferData.setEndTimes(newEnds);
                        transferData.setEventTypes(newTypes);
                        transferData.setSpecificEvents("");

                        String[] starts = CommonUtils.convertStrToArray(newStarts);
                        for (int i = 0; i < starts.length; i++) {
                            String s = (String) SPUtils.get(getActivity(), starts[i], "");
                            if (!starts[0].equals("")) {
                                if (i == 0) {
                                    transferData.setSpecificEvents(s);
                                } else {
                                    transferData.setSpecificEvents(transferData.getSpecificEvents() + "#^@" + s);
                                }
                            }
                        }
                        if (!isTryOutAccount)
                            changeDataToServer();

                        Intent intent_refreshChart = new Intent(String4Broad.RefershChartData);
                        getActivity().sendBroadcast(intent_refreshChart);

                        //把存储的事件的key删除
                        SPUtils.remove(getActivity(), startTime);
                        deleteFromDataBase(startTime);
                        refreshRecyclerViewDataByString(newStarts, newEnds, newTypes);
                        commonDialog.dismiss();
                    }
                }, null);
        commonDialog.show();
    }

    /*
    将删除的事件从数据库中删除
     */
    private void deleteFromDataBase(String startTime) {
        SpecificEventSourceDao specificEventDao = new SpecificEventSourceDao(getActivity());
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", SystemInfo.getInstance(getActivity()).getMemberId());
            String currentDate = (String) SPUtils.get(getActivity(), ApplicationGlobal.CURRENT_DATE, "");
            map.put("eventDate", currentDate);
            map.put("startTime", startTime);
            specificEventDao.delete(specificEventDao.query(map));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
    将每日事件保存到数据库
     */
    private void saveToDataBase(String startTimes, String endTimes, String eventTypes) {
        EverydayEventSourceDao everydayEventDao = new EverydayEventSourceDao(getActivity());
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", SystemInfo.getInstance(getActivity()).getMemberId());
            String currentDate = (String) SPUtils.get(getActivity(), ApplicationGlobal.CURRENT_DATE, "");
            map.put("eventDate", currentDate);
            ArrayList<EverydayEventSource> todayEventList = (ArrayList<EverydayEventSource>) everydayEventDao.query(map);
            if (todayEventList == null || todayEventList.size() <= 0) {
                //今日未添加过事项
                everydayEventDao.save(new EverydayEventSource(SystemInfo.getInstance(getActivity()).getMemberId(), currentDate, startTimes, endTimes, eventTypes));
            } else {
                todayEventList.get(0).setStartTimes(startTimes);
                todayEventList.get(0).setEndTimes(endTimes);
                todayEventList.get(0).setEventTypes(eventTypes);
                everydayEventDao.update(todayEventList.get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.ChangeData.changeSuccess:
                    ChangeDataReturnBean changeBean = (ChangeDataReturnBean) msg.obj;
                    if (changeBean.isAmendSuccess()) {
                        TT.showShort(getContext(), "同步成功");
                    } else {
                        TT.showShort(getContext(), "同步失败");
                    }
                    break;
                case Constants.ChangeData.changeFailure:
                    TT.showShort(getContext(), msg.obj.toString());
                    break;
                case Constants.ChangeData.addOneWordSuccess:
                    ChangeDataReturnBean chaBean = (ChangeDataReturnBean) msg.obj;
                    if (chaBean.isAmendSuccess()) {
                        TT.showShort(getContext(), "同步成功");
                    } else {
                        TT.showShort(getContext(), "同步失败");
                    }
                    break;
                case Constants.ChangeData.addOneWordFailure:
                    TT.showShort(getContext(), msg.obj.toString());
                    break;
                case Constants.requestException:
                    if (msg.obj != null && msg.obj.toString().contains("java.net.ConnectException")) {
                        if (!isShowNetDisable) {
                            //这种错误只在用户启动的时候显示一遍
                            TT.showShort(getContext(), "检测到网络异常，数据无法同步");
                            isShowNetDisable = true;
                        }
                    } else {
                        if (!isShowServerDisable) {
                            //这种错误只在用户启动的时候显示一遍
                            TT.showShort(getContext(), "服务器开小差啦~");
                            isShowServerDisable = true;
                        }
                    }
                    break;
            }
        }

    };

    //友盟统计：由Activity和Fragment构成的页面需要这样写
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.main_today));
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.main_today));
    }
}
