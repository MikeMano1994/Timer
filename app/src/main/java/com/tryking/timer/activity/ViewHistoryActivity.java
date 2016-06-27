package com.tryking.timer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.tryking.timer.R;
import com.tryking.timer.base.BaseActivity;
import com.tryking.timer.fragment.viewhistory.ViewHistoryCalendarFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewHistoryActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.rl_content)
    FrameLayout rlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        setSupportActionBar(toolBar);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rl_content, new ViewHistoryCalendarFragment());
        transaction.commit();
    }
}
