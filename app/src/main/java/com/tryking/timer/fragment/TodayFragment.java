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
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tryking.timer.MyItemDividerDecoration;
import com.tryking.timer.R;
import com.tryking.timer.activity.AddActivity;
import com.tryking.timer.adapter.TodayEventAdapter;
import com.tryking.timer.bean.TodayEventData;
import com.tryking.timer.utils.CommonUtils;
import com.tryking.timer.utils.SPUtils;
import com.tryking.timer.utils.TT;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayFragment extends Fragment implements TodayEventAdapter.onNoEventItemClickListener, TodayEventAdapter.onHaveEventItemClickListener {
    @Bind(R.id.event_content)
    RecyclerView eventContent;
    @Bind(R.id.actionButton)
    FloatingActionButton actionButton;
    @Bind(R.id.hint)
    TextView hint;

    private static final int REQUEST_ADD_CODE = 0;//添加事项请求吗
    private boolean IS_ADDED = false;
    List<TodayEventData> todayEventDatas = new ArrayList<>();
    private int Tag = 0;

    private TodayEventAdapter todayEventAdapter;
    private Map<String, TodayEventData> mapData = new HashMap<>();

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
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
        todayEventDatas.clear();
        for (int i = 0; i < starts.length; i++) {
            IS_ADDED = true;
            if (starts[0] == "" && ends[0] == "") {
                TodayEventData data1 = new TodayEventData(0, "0000", "2400", "没有事件");
                todayEventDatas.add(data1);
            } else {
                if (i == 0 && Integer.parseInt(starts[0]) > 0) {
                    TodayEventData data2 = new TodayEventData(0, "0000", starts[0], "沒有事件");
                    todayEventDatas.add(data2);
                }
                TodayEventData data3 = new TodayEventData(1, starts[i], ends[i], "有事件");
                todayEventDatas.add(data3);
                if (i != starts.length - 1 && (!ends[i].equals(starts[i + 1]))) {
                    TodayEventData data1 = new TodayEventData(0, ends[i], starts[i + 1], "没有事件");
                    todayEventDatas.add(data1);
                }
                if (i == starts.length - 1 && Integer.parseInt(ends[ends.length - 1]) < 2400) {
                    TodayEventData data2 = new TodayEventData(0, ends[ends.length - 1], "2400", "沒有事件");
                    todayEventDatas.add(data2);
                }
            }
        }
        todayEventAdapter.refresh(todayEventDatas);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        initViews();
    }

    private void initDatas() {
        SPUtils.put(getActivity(), "startTimes", "");
        SPUtils.put(getActivity(), "endTimes", "");
        String startTimes = (String) SPUtils.get(getActivity(), "startTimes", "");
        String endTimes = (String) SPUtils.get(getActivity(), "endTimes", "");
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        String[] ends = CommonUtils.convertStrToArray(endTimes);
        todayEventDatas.clear();
        for (int i = 0; i < starts.length; i++) {
            Logger.e("到这里了" + i + "::" + starts[i] + "end::" + ends[i] + "lentg" + starts.length);
            IS_ADDED = true;
            if (starts[0] == "" && ends[0] == "") {
                TodayEventData data = new TodayEventData(0, "0000", "2400", "没有事件");
                todayEventDatas.add(data);
            } else {
                if (i == 0 && Integer.parseInt(starts[0]) > 0) {
                    TodayEventData data = new TodayEventData(0, "0000", starts[0], "沒有事件");
                    todayEventDatas.add(data);
                }
                TodayEventData data = new TodayEventData(1, starts[i], ends[i], "有事件");
                todayEventDatas.add(data);
                if (i != starts.length - 1 && (!ends[i].equals(starts[i + 1]))) {
                    TodayEventData data1 = new TodayEventData(0, ends[i], starts[i + 1], "没有事件");
                    todayEventDatas.add(data1);
                }
                if (i == starts.length - 1 && Integer.parseInt(ends[ends.length - 1]) < 2400) {
                    TodayEventData data2 = new TodayEventData(0, ends[ends.length - 1], "2400", "沒有事件");
                    todayEventDatas.add(data2);
                }
            }
        }
    }


    private void initViews() {
        if (IS_ADDED) {
            hint.setVisibility(View.GONE);
            eventContent.setVisibility(View.VISIBLE);
        } else {
            hint.setVisibility(View.VISIBLE);
            eventContent.setVisibility(View.GONE);
        }
        todayEventAdapter = new TodayEventAdapter(new WeakReference<Context>(getActivity()), todayEventDatas);
        todayEventAdapter.setOnNoEventItemClickListener(this);
        todayEventAdapter.setOnHaveEventItemClickListener(this);
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
        TT.showShort(getActivity(), "我有事件" + position + ":" + hint);
    }

    @Override
    public void onNoEventItemClick(int position, String hint) {
        TT.showShort(getActivity(), "我没事件" + position + ":" + hint);
    }


}
