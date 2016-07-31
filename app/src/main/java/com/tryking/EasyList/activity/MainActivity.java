package com.tryking.EasyList.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.tryking.EasyList.R;
import com.tryking.EasyList.adapter.MainContentPagerAdapter;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.String4Broad;
import com.tryking.EasyList.fragment.main.IcFragment;
import com.tryking.EasyList.fragment.main.TodayFragment;
import com.tryking.EasyList.fragment.main.StatsFragment;

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
    private MainActivity.exitMainActivityReceiver exitMainActivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDatas();
        initViews();
        init();
//        startActivity(new Intent(MainActivity.this, TestActivity.class));
    }

    private void init() {
        //退出MainActivity的Receiver
        IntentFilter exitFilter = new IntentFilter(String4Broad.ExitMainActivity);
        exitMainActivityReceiver = new exitMainActivityReceiver();
        registerReceiver(exitMainActivityReceiver, exitFilter);
    }

    private void initDatas() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new TodayFragment());
        StatsFragment statsFragment = new StatsFragment();
        IcFragment icFragment = new IcFragment();
        fragmentList.add(statsFragment);
        fragmentList.add(icFragment);
        titles = new ArrayList<>();
        titles.add(new String("今日"));
        titles.add(new String("统计"));
        titles.add(new String("个人"));
        iCons = new ArrayList<>();
        iCons.add(R.drawable.ic_home_today);
        iCons.add(R.drawable.ic_home_stats);
        iCons.add(R.drawable.ic_home_center);
    }

    /*
    初始化ToolBar
     */
    private void initToolBar() {
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.app_name);
    }


    private void initViews() {
        currentIndex = 0;
        manager = getSupportFragmentManager();
        initToolBar();
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
            case "StatsFragment":
                fragment = (StatsFragment) adapter.instantiateItem(mainContent, 1);
                break;
            case "IcFragment":
                fragment = (IcFragment) adapter.instantiateItem(mainContent, 2);
                break;
            default:
                throw new IllegalArgumentException("fragmentName Error!");
        }
        return fragment;
    }

    class exitMainActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (exitMainActivityReceiver != null) {
            unregisterReceiver(exitMainActivityReceiver);
        }
        super.onDestroy();
    }
}
