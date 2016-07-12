package com.tryking.EasyList.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.fragment.viewhistory.ViewHistoryCalendarFragment;

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

        initView();
        initData();
    }

    private void initView() {
        initToolBar();
    }

    /*
初始化ToolBar
 */
    private void initToolBar() {
        toolBar.setTitle(R.string.view_history);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHistoryActivity.this.finish();
            }
        });
    }

    private void initData() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rl_content, new ViewHistoryCalendarFragment());
        transaction.commit();
    }
}
