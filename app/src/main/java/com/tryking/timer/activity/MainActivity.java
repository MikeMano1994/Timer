package com.tryking.timer.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tryking.timer.R;
import com.tryking.timer.adapter.MainContentPagerAdapter;
import com.tryking.timer.fragment.IcFragment;
import com.tryking.timer.fragment.TodayFragment;
import com.tryking.timer.fragment.WeekFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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
        fragmentList.add(new WeekFragment());
        fragmentList.add(new IcFragment());
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
//        mainContent.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(mainContent);
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.getTabAt(i).setText("");
            tabLayout.getTabAt(i).setContentDescription(titles.get(i));
            tabLayout.getTabAt(i).setIcon(iCons.get(i));
        }
    }
}
