package com.tryking.EasyList._fragment.viewhistory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList.adapter.TodayEventAdapter;
import com.tryking.EasyList.base.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 26011 on 2016/7/31.
 */
public class DayDetailFragment extends BaseFragment {
    @Bind(R.id.tv_one_word)
    TextView tvOneWord;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.header)
    RecyclerViewHeader header;
    private ArrayList<TodayEventData> todayEventDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        todayEventDatas = new ArrayList<>();
        todayEventDatas.add(new TodayEventData(1, "0120", "0220", "111"));
        todayEventDatas.add(new TodayEventData(2, "0120", "0220", "111"));
        todayEventDatas.add(new TodayEventData(3, "0320", "0320", "222"));
        todayEventDatas.add(new TodayEventData(4, "0520", "0520", "333"));
        todayEventDatas.add(new TodayEventData(0, "0720", "0720", "444"));
        todayEventDatas.add(new TodayEventData(1, "0120", "0220", "111"));
        todayEventDatas.add(new TodayEventData(2, "0120", "0220", "111"));
        todayEventDatas.add(new TodayEventData(3, "0320", "0320", "222"));
        todayEventDatas.add(new TodayEventData(4, "0520", "0520", "333"));
        todayEventDatas.add(new TodayEventData(0, "0720", "0720", "444"));
        todayEventDatas.add(new TodayEventData(1, "0920", "0920", "555"));
        todayEventDatas.add(new TodayEventData(1, "0120", "0220", "111"));
        todayEventDatas.add(new TodayEventData(2, "0120", "0220", "111"));
        todayEventDatas.add(new TodayEventData(3, "0320", "0320", "222"));
        todayEventDatas.add(new TodayEventData(4, "0520", "0520", "333"));
        todayEventDatas.add(new TodayEventData(0, "0720", "0720", "444"));
        todayEventDatas.add(new TodayEventData(1, "0920", "0920", "555"));
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContent.setAdapter(new TodayEventAdapter(new WeakReference<Context>(getContext()), todayEventDatas));
        header.attachTo(rvContent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
