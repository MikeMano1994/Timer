package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.widgets.BackgroundScrollViewPager;
import com.tryking.EasyList.widgets.circleIndicator.CircleIndicator;
import com.umeng.analytics.MobclickAgent;

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

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        init();
    }

    @OnClick(R.id.bt_entrance)
    void click(View v) {
        switch (v.getId()) {
            case R.id.bt_entrance:
                SPUtils.put(GuidanceActivity.this, Constants.SHARED_PREFERENCE_IS_FIRST_LAUNCHER, false);
                startActivity(new Intent(GuidanceActivity.this, LoginActivity.class));
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

        initConfig();
    }

    /**
     * 因为这里只会运行一次，所以设置中的初始设置可以在这里配置
     */
    private void initConfig() {
        //首页饼状图随手指转动
        SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_CHART_ANIM, true);
        //首页显示“轻单儿”
        SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_SHOW_APPNAME, true);
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

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写
        MobclickAgent.onPageStart(getString(R.string.guide));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.guide));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }
}
