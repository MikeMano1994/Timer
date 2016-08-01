package com.tryking.EasyList._fragment.viewhistory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList._bean.TodayEventDataImParcelable;
import com.tryking.EasyList._bean.loginBean.Event;
import com.tryking.EasyList._bean.viewHistoryBean.ViewMonthReturnBean;
import com.tryking.EasyList.adapter.viewhistory.DayDetailFragmentAdapter;
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.global.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 26011 on 2016/7/27.
 */
public class DayFragment extends BaseFragment {

    @Bind(R.id.day_tab_date)
    TabLayout dayTabDate;
    @Bind(R.id.day_main)
    ViewPager dayMain;

    private ArrayList<TodayEventData> dayEvents;

    private String curDate;

    /**
     * 根据一系列的属性值创建DayMainFragment实例
     *
     * @return
     */
    public static DayFragment getInstance(ViewMonthReturnBean data) {
        DayFragment dayFragment = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ViewHistory.DAY_EVENT_FOR_MONTH, data);
        dayFragment.setArguments(bundle);
        return dayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewhistory_day, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        dayEvents = new ArrayList<>();
        List<Fragment> dayDetailFragments = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        Bundle arguments = getArguments();
        if (arguments != null) {
            ViewMonthReturnBean data = (ViewMonthReturnBean) arguments.getSerializable(Constants.ViewHistory.DAY_EVENT_FOR_MONTH);
            for (int i = 0; i < data.getMonthEvents().size(); i++) {
                List<Event> eventList = data.getMonthEvents().get(i).getEventList();
                ArrayList<TodayEventDataImParcelable> dayEventDatas = new ArrayList<>();
                for (int j = 0; j < eventList.size(); j++) {
                    TodayEventDataImParcelable todayEventDataImParcelable = new TodayEventDataImParcelable(Integer.parseInt(eventList.get(j).getEventtypes()),
                            eventList.get(j).getStarttime(), eventList.get(j).getEndtime(), eventList.get(j).getRecord());
                    dayEventDatas.add(todayEventDataImParcelable);
                }
                DayDetailFragment instance = DayDetailFragment.getInstance(dayEventDatas);
                dayDetailFragments.add(instance);
                strings.add(data.getMonthEvents().get(i).getDate());
                Logger.e(strings.toString());
            }
        }

        dayTabDate.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dayMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dayMain.setAdapter(new DayDetailFragmentAdapter(getFragmentManager(), dayDetailFragments, strings));

        dayTabDate.setupWithViewPager(dayMain);
        dayMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logger.e("位置改变啦：" + position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
