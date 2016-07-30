package com.tryking.EasyList._fragment.viewhistory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabWidget;
import android.widget.TextView;

import com.tryking.EasyList.R;
import com.tryking.EasyList.utils.TT;

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

    private String[] mTitle = new String[200];
    private String[] mData = new String[200];


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
        for (int i = 0; i < 200; i++) {
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

        dayMain.setAdapter(mAdapter);
        dayTabDate.setupWithViewPager(dayMain);
        dayMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dayTabDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TT.showShort(getContext(), "点我");
            }
        });
        dayTabDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TT.showShort(getActivity(), "长点我");
                return false;
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
