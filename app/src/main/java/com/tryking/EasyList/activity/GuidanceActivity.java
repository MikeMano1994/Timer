package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.widgets.BackgroundScrollViewPager;
import com.tryking.EasyList.widgets.circleIndicator.CircleIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuidanceActivity extends BaseActivity {
    @Bind(R.id.main_content)
    BackgroundScrollViewPager mainContent;
    @Bind(R.id.indicator)
    CircleIndicator indicator;
    @Bind(R.id.bt_entrance)
    Button btEntrance;
    private ArrayList<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        ButterKnife.bind(this);
        init();
    }

    @OnClick(R.id.bt_entrance)
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_entrance:
                startActivity(new Intent(GuidanceActivity.this, LoginAndRegisterActivity.class));
                finish();
        }

    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.guidance_page_1, null);
        View view2 = inflater.inflate(R.layout.guidance_page_2, null);
        View view3 = inflater.inflate(R.layout.guidance_page_3, null);

        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        };

        mainContent.setAdapter(mPagerAdapter);
        indicator.setViewPager(mainContent);
        mainContent.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == views.size() - 1) {
                btEntrance.setVisibility(View.VISIBLE);
            } else {
                btEntrance.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
