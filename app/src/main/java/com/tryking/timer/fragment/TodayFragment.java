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

import com.orhanobut.logger.Logger;
import com.tryking.timer.R;
import com.tryking.timer.activity.AddActivity;
import com.tryking.timer.adapter.TodayEventAdapter;
import com.tryking.timer.bean.TodayEventData;
import com.tryking.timer.utils.CommonUtils;
import com.tryking.timer.utils.SPUtils;
import com.tryking.timer.utils.TT;
import com.tryking.timer.widgets.CommonDialog;
import com.tryking.timer.widgets.RecyclerView.MyItemDividerDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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

        refreshRecyclerViewDataByString(startTimes, endTimes);
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
        String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
        String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");

        refreshRecyclerViewDataByString(startTimes, endTimes);
    }

    /*
    根据起止时间更新RecyclerView的内容
     */
    private void refreshRecyclerViewDataByString(String startTimes, String endTimes) {
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
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
                TodayEventData data3 = new TodayEventData(((int) (Math.random() * 100)) % 2 + 1, starts[i], ends[i], "有事件");
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
        commonDialog.setDialogContent(null, "删除此条记录".replaceAll(".{1}(?!$)", "$0 "), null, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TT.showShort(getActivity(), "删除啦");
                String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
                String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
                Logger.e("old::" + startTimes + "::" + endTimes);
                Logger.e(startTime + "start::end::" + endTime);
//                Pattern compileStart = Pattern.compile(startTimes);
//                Matcher matcherStart = compileStart.matcher(startTime);
//                matcherStart.replaceFirst("1111");
//                Pattern compileEnd = Pattern.compile(endTimes);
//                Matcher matcherEnd = compileEnd.matcher(endTime);
//                matcherEnd.replaceFirst("1111");

                String newStarts;
                String newEnds;
                if (startTimes.contains("," + startTime)) {
                    newStarts = CommonUtils.deleteStr(startTimes, "," + startTime);
                    newEnds = CommonUtils.deleteStr(endTimes, "," + endTime);
                } else {
                    newStarts = CommonUtils.deleteStr(startTimes, startTime);
                    newEnds = CommonUtils.deleteStr(endTimes, endTime);
                    if (newStarts.startsWith(",") && newEnds.startsWith(",")) {
                        newStarts = newStarts.replaceFirst(",", "");
                        newEnds = newEnds.replaceFirst(",", "");
                    }
                }

                Logger.e("news::" + newStarts + "::" + newEnds);
                SPUtils.put(getActivity(), "startTimes", newStarts);
                SPUtils.put(getActivity(), "endTimes", newEnds);
                refreshRecyclerViewDataByString(newStarts, newEnds);
                commonDialog.dismiss();
            }
        }, null);
        commonDialog.show();
    }
}
