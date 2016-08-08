package com.tryking.EasyList.activity.about_easylist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EasyListBornActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_list_born);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        initToolBar();
    }

    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.easylist_born);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyListBornActivity.this.finish();
            }
        });
    }
}
