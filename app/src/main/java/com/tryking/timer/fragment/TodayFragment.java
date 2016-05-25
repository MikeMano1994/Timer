package com.tryking.timer.fragment;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.Logger;
import com.tryking.timer.R;
import com.tryking.timer.activity.AddActivity;
import com.tryking.timer.adapter.TodayEventAdapter;
import com.tryking.timer.bean.TodayEventData;
import com.tryking.timer.utils.CommonUtils;
import com.tryking.timer.utils.SPUtils;
import com.tryking.timer.widgets.CommonDialog;
import com.tryking.timer.widgets.RecyclerView.MyItemDividerDecoration;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayFragment extends Fragment implements TodayEventAdapter.onNoEventItemClickListener, TodayEventAdapter.onHaveEventItemClickListener, TodayEventAdapter.onHaveEventItemLongClickListener {
    @Bind(R.id.event_content)
    RecyclerView eventContent;
    @Bind(R.id.actionButton)
    FloatingActionButton actionButton;


    private static final int REQUEST_ADD_CODE = 0;//添加事项请求吗
    List<TodayEventData> todayEventDatas = new ArrayList<>();
    private TodayEventAdapter todayEventAdapter;
    private RequestQueue mQueue;

    @OnClick({R.id.actionButton})
    void click(View view) {
        switch (view.getId()) {
            case R.id.actionButton:

                Animator animator = createAnimator(view);
                animator.start();
                startActivityForResult(new Intent(getActivity(), AddActivity.class), REQUEST_ADD_CODE);
                break;
        }
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
        String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
        String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
        String eventType = (String) SPUtils.get(getActivity(), "eventType", "");

        refreshRecyclerViewDataByString(startTimes, endTimes, eventType);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
    }

    private void initDatas() {
        SPUtils.put(getActivity(), "startTimes", "");
        SPUtils.put(getActivity(), "endTimes", "");
        SPUtils.put(getActivity(), "eventType", "");
        String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
        String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
        String eventType = (String) SPUtils.get(getActivity(), "eventType", "");

        refreshRecyclerViewDataByString(startTimes, endTimes, eventType);

        String currentTime = getCurrentTime();
    }

    /*
    获取网络时间
     */
    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        Logger.e(dateFormat.format(date));
        long time = 0;
        try {
            Date date1 = dateFormat.parse("2016-5-25 23:00:00");
            time = date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Logger.e(time + "::::current:" + System.currentTimeMillis());

//        Calendar galendar = GregorianCalendar.getInstance();
//        galendar.set(2016,05,25,23,00);
//        galendar.get();

        return dateFormat.format(date);
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
//            Logger.e("我来啦" + date1.getYear() + "年" + date1.getMonth() + "月" + date1.getDay() + "月" + date1.getHours() + "时" + date1.getMinutes() + "分" + date1.getSeconds() + "秒");
            Date date2 = new Date(System.currentTimeMillis());
            Logger.e("手机当前时间：：" + date2.getYear() + "年" + date2.getMonth() + "月" + date2.getDay() + "月" + date2.getHours() + "时" + date2.getMinutes() + "分" + date2.getSeconds() + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    根据起止时间更新RecyclerView的内容
     */
    private void refreshRecyclerViewDataByString(String startTimes, String endTimes, String eventType) {
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
        String[] type = CommonUtils.convertStrToArray(eventType);
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
            } else {
                Logger.e(starts[0] + "starts{0}");
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
        todayEventAdapter = new TodayEventAdapter(new WeakReference<Context>(getActivity()), todayEventDatas);
        todayEventAdapter.setOnNoEventItemClickListener(this);
        todayEventAdapter.setOnHaveEventItemClickListener(this);
        todayEventAdapter.setOnHaveEventItemLongClickListener(this);
        eventContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventContent.addItemDecoration(new MyItemDividerDecoration(getActivity(), MyItemDividerDecoration.VERTICAL_LIST));
        eventContent.setAdapter(todayEventAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator createAnimator(View v) {
        Animator animator = ViewAnimationUtils.createCircularReveal(v, v.getWidth() / 2, v.getHeight() / 2, 0, v.getWidth());
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(2000);
        return animator;
    }

    @Override
    public void onHaveEventItemClick(int position, String hint) {
//        TT.showShort(getActivity(), "我有事件" + position + ":" + hint);
//        startActivity(new Intent(getActivity(), TestActivity.class));
    }

    @Override
    public void onNoEventItemClick(int position, String hint) {
//        TT.showShort(getActivity(), "我没事件" + position + ":" + hint);
    }


    @Override
    public void onHaveEventItemLongClick(int position, final String startTime, final String endTime) {
        final CommonDialog commonDialog = new CommonDialog(getActivity());
        commonDialog.setDialogContent(null, "删除" + CommonUtils.addSignToStr(startTime) + " - " + CommonUtils.addSignToStr(endTime) + ("这条记录").replaceAll(".{1}(?!$)", "$0 "), null, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TT.showShort(getActivity(), "删除啦");
                String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
                String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
                String eventType = (String) SPUtils.get(getActivity(), "eventType", "");
                Logger.e("old::" + startTimes + "::" + endTimes + "::type::" + eventType);
                Logger.e(startTime + "start::end::" + endTime);

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
                        newTypes = eventType.substring(0, i) + eventType.substring(i + 5);
                    }
                } else {
                    newStarts = CommonUtils.deleteStr(startTimes, startTime);
                    newEnds = CommonUtils.deleteStr(endTimes, endTime);
                    int i = startTimes.indexOf(startTime);
                    if (i != -1) {
                        newTypes = eventType.substring(0, i) + eventType.substring(i + 4);
                    }
                    if (newStarts.startsWith(",") && newEnds.startsWith(",") && newTypes.startsWith(",")) {
                        newStarts = newStarts.replaceFirst(",", "");
                        newEnds = newEnds.replaceFirst(",", "");
                        newTypes = newTypes.replaceFirst(",", "");
                    }
                }
//                Logger.e("之前存在吗？？？？" + SPUtils.contains(getActivity(), startTime));
                Logger.e("news::" + newStarts + "::" + newEnds + ":::" + newTypes);
                SPUtils.put(getActivity(), "startTimes", newStarts);
                SPUtils.put(getActivity(), "endTimes", newEnds);
                SPUtils.put(getActivity(), "eventType", newTypes);
                //把存储的事件的key删除
                SPUtils.remove(getActivity(), startTime);
                refreshRecyclerViewDataByString(newStarts, newEnds, eventType);
                commonDialog.dismiss();
//                Logger.e("存在吗？？？？" + SPUtils.contains(getActivity(), startTime));
            }
        }, null);
        commonDialog.show();
    }
}
