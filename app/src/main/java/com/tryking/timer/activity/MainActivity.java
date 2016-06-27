package com.tryking.timer.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.tryking.timer.R;
import com.tryking.timer.adapter.MainContentPagerAdapter;
import com.tryking.timer.base.BaseActivity;
import com.tryking.timer.fragment.main.IcFragment;
import com.tryking.timer.fragment.main.TodayFragment;
import com.tryking.timer.fragment.main.WeekFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.main_content)
    ViewPager mainContent;
    private FragmentManager manager;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private List<Integer> iCons;
    private FragmentTransaction transaction;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDatas();
        initViews();
//        startActivity(new Intent(MainActivity.this, TestActivity.class));
    }

    private void initDatas() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new TodayFragment());
        WeekFragment weekFragment = new WeekFragment();
        IcFragment icFragment = new IcFragment();
        fragmentList.add(weekFragment);
        fragmentList.add(icFragment);
        titles = new ArrayList<>();
        titles.add(new String("今日"));
        titles.add(new String("本周"));
        titles.add(new String("我的"));
        iCons = new ArrayList<>();
        iCons.add(R.drawable.ic_action_book);
        iCons.add(R.drawable.ic_action_calendar_day);
        iCons.add(R.drawable.ic_action_emo_wonder);
    }

    private void initViews() {
        currentIndex = 0;
        manager = getSupportFragmentManager();
        setSupportActionBar(toolBar);
        mainContent.setAdapter(new MainContentPagerAdapter(manager, fragmentList, titles));
        mainContent.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(mainContent);
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.getTabAt(i).setText("");
            tabLayout.getTabAt(i).setContentDescription(titles.get(i));
            tabLayout.getTabAt(i).setIcon(iCons.get(i));
        }
        mainContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //如果是显示第二页，刷新第二页的数据
                if (position == 1) {
                    WeekFragment weekFragment = (WeekFragment) getFragment("WeekFragment");
                    weekFragment.refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public Fragment getFragment(String fragmentName) {
        PagerAdapter adapter = mainContent.getAdapter();
        Fragment fragment = null;
        switch (fragmentName) {
            case "TodayFragment":
                fragment = (TodayFragment) adapter.instantiateItem(mainContent, 0);
                break;
            case "WeekFragment":
                fragment = (WeekFragment) adapter.instantiateItem(mainContent, 1);
                break;
            case "IcFragment":
                fragment = (IcFragment) adapter.instantiateItem(mainContent, 2);
                break;
            default:
                throw new IllegalArgumentException("fragmentName Error!");
        }
        return fragment;
    }
}
