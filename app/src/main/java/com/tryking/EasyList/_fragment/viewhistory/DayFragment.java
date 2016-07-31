package com.tryking.EasyList._fragment.viewhistory;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.EveryDayEvent;
import com.tryking.EasyList._bean.loginBean.Event;
import com.tryking.EasyList.adapter.viewhistory.DayDetailFragmentAdapter;
import com.tryking.EasyList.global.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 26011 on 2016/7/27.
 */
public class DayFragment extends Fragment {

    @Bind(R.id.day_tab_date)
    TabLayout dayTabDate;
    @Bind(R.id.day_main)
    ViewPager dayMain;

    private String[] mTitle = new String[31];
    private String[] mData = new String[31];
    private ArrayList<EveryDayEvent> dayEvents;

    /**
     * 根据一系列的属性值创建DayMainFragment实例
     *
     * @return
     */
    public static DayFragment getInstance(List<EveryDayEvent> events) {
        DayFragment day = new DayFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.ViewHistory.DAY_EVENT, (ArrayList<? extends Parcelable>) events);
        day.setArguments(bundle);
        return day;
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
        for (int i = 0; i < 31; i++) {
            mTitle[i] = "2/" + i + " - 3/" + i;
            mData[i] = "data" + i;
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

//        dayMain.setAdapter(mAdapter);

        List<Fragment> dayDetailFragments = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            DayDetailFragment dayDetailFragment = new DayDetailFragment();
            dayDetailFragments.add(dayDetailFragment);
            strings.add("1" + i);
        }
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

    private PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }

        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            TextView tv = new TextView(getActivity());
            tv.setTextSize(30.f);
            tv.setText(mData[position]);
            tv.setTextColor(Color.parseColor("#000000"));
            ((ViewPager) container).addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
