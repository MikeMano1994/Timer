package com.tryking.EasyList.z_disuse;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

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
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
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

    //友盟统计：有Activity和Fragment构成的页面需要这样写
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
