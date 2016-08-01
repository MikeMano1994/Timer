package com.tryking.EasyList._fragment.viewhistory;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.tryking.EasyList._bean.TodayEventDataImParcelable;
import com.tryking.EasyList.adapter.TodayEventAdapter;
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.global.Constants;

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

    public static DayDetailFragment getInstance(ArrayList<TodayEventDataImParcelable> data) {
        DayDetailFragment dayDetailFragment = new DayDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ViewHistory.DAY_DETAIL_ARGUMENT, data);
        dayDetailFragment.setArguments(bundle);
        return dayDetailFragment;
    }

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
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<TodayEventDataImParcelable> todayDatas = arguments.getParcelableArrayList(Constants.ViewHistory.DAY_DETAIL_ARGUMENT);
            for (int i = 0; i < todayDatas.size(); i++) {
                TodayEventData todayEventData = new TodayEventData(todayDatas.get(i).getDataType(), todayDatas.get(i).getStartTime(),
                        todayDatas.get(i).getEndTime(), todayDatas.get(i).getSpecificEvent());
                todayEventDatas.add(todayEventData);
            }
        }

        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContent.setAdapter(new TodayEventAdapter(new WeakReference<Context>(getContext()), todayEventDatas, false));
        header.attachTo(rvContent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
