package com.tryking.EasyList.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.tryking.EasyList.R;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.String4Broad;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonSettingActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;

    @Bind(R.id.chart_switch)
    SwitchCompat chartSwitch;
    @Bind(R.id.chart_anim)
    RelativeLayout chartAnim;
    @Bind(R.id.name_switch)
    SwitchCompat nameSwitch;
    @Bind(R.id.show_app_name)
    RelativeLayout showAppName;
    @Bind(R.id.color_switch)
    SwitchCompat colorSwitch;
    @Bind(R.id.full_color)
    RelativeLayout fullColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_setting);
        ButterKnife.bind(this);

        init();
    }

    @OnClick({R.id.chart_anim, R.id.show_app_name, R.id.full_color})
    void click(View v) {
        switch (v.getId()) {
            case R.id.chart_anim:
                chartSwitch.toggle();
                if (chartSwitch.isChecked()) {
                    SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_CHART_ANIM, true);

                    Intent intent_chartAnim = new Intent(String4Broad.ChartAnim);
                    sendBroadcast(intent_chartAnim);
                } else {
                    SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_CHART_ANIM, false);

                    Intent intent_chartAnim = new Intent(String4Broad.ChartDisAnim);
                    sendBroadcast(intent_chartAnim);
                }
                break;
            case R.id.show_app_name:
                nameSwitch.toggle();
                if (nameSwitch.isChecked()) {
                    SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_SHOW_APPNAME, true);
                } else {
                    SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_SHOW_APPNAME, false);
                }
                Intent intent_showName = new Intent(String4Broad.ShowAppName);
                sendBroadcast(intent_showName);
                break;
            case R.id.full_color:
                colorSwitch.toggle();
                if (colorSwitch.isChecked()) {
                    SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_COLOR_FULL, true);
                } else {
                    SPUtils.put(getApplicationContext(), Constants.Setting.SP_SET_COLOR_FULL, false);
                }
                Intent intent_adapter = new Intent(String4Broad.ChangeAdapter);
                sendBroadcast(intent_adapter);
                break;
        }
    }

    private void init() {
        initToolBar();

        //首页饼状图动画
        if ((boolean) SPUtils.get(getApplicationContext(), Constants.Setting.SP_SET_CHART_ANIM, false)) {
            chartSwitch.setChecked(true);
        } else {
            chartSwitch.setChecked(false);
        }

        //首页“轻单儿”
        if ((boolean) SPUtils.get(getApplicationContext(), Constants.Setting.SP_SET_SHOW_APPNAME, true)) {
            nameSwitch.setChecked(true);
        } else {
            nameSwitch.setChecked(false);
        }

        if ((boolean) SPUtils.get(getApplicationContext(), Constants.Setting.SP_SET_COLOR_FULL, true)) {
            colorSwitch.setChecked(true);
        } else {
            colorSwitch.setChecked(false);
        }
    }

    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.common_setting);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计：Activity自己实现的页面需要这样写(不包含Fragment)
        MobclickAgent.onPageStart(getString(R.string.common_setting));//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.common_setting));//统计页面
        MobclickAgent.onPause(this);//统计时长
    }
}
