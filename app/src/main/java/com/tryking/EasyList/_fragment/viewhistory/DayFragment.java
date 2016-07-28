package com.tryking.EasyList._fragment.viewhistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.loginBean.DayEvent;
import com.tryking.EasyList.activity.MainActivity;

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

    private String[] mTitle = new String[20];
    private String[] mData = new String[20];


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
        for (int i = 0; i < 20; i++) {
            mTitle[i] = "title" + i;
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

//        dayTabDate.setTabsFromPagerAdapter();
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
