package com.tryking.EasyList._activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.tryking.EasyList.R;
import com.tryking.EasyList._fragment.ViewHistoryFragment;
import com.tryking.EasyList.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewHistoryActivity extends BaseActivity {

    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.toolBar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, new ViewHistoryFragment());
        fragmentTransaction.commit();

        initToolBar();
    }

    /*
       初始化ToolBar
        */
    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.view_history);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHistoryActivity.this.finish();
            }
        });
    }
}
