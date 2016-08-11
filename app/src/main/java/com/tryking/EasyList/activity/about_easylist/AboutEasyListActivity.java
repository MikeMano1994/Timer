package com.tryking.EasyList.activity.about_easylist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.AppVersionReturnBean;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.AppUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.UpdateDialog;
import com.umeng.fb.FeedbackAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutEasyListActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.easylist_born)
    RelativeLayout easylistBorn;
    @Bind(R.id.opinion_feedback)
    RelativeLayout opinionFeedback;
    @Bind(R.id.check_new_version)
    RelativeLayout checkNewVersion;
    @Bind(R.id.update_new)
    TextView updateNew;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_easy_list);
        ButterKnife.bind(this);

        init();
    }

    @OnClick({R.id.easylist_born, R.id.opinion_feedback, R.id.check_new_version})
    void click(View v) {
        switch (v.getId()) {
            case R.id.easylist_born:
                startActivity(new Intent(AboutEasyListActivity.this, EasyListBornActivity.class));
                break;
            case R.id.opinion_feedback:
//                FeedbackAgent agent = new FeedbackAgent(this);
//                agent.startFeedbackActivity();
                startActivity(new Intent(AboutEasyListActivity.this, FeedbackActivity.class));
                break;
            case R.id.check_new_version:
//                new CheckUpdateTask(AboutEasyListActivity.this, true).execute();
                checkNewVersion();
                break;
            default:
                break;
        }
    }

    private void checkNewVersion() {
        Logger.e("开始获取版本信息");
        mDialog = new ProgressDialog(AboutEasyListActivity.this);
        mDialog.setMessage("正在检查新版本");
        mDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("deviceType", "android");
        params.put("appName", getString(R.string.app_name_english));
        String url = InterfaceURL.appVersion;
        JsonBeanRequest<AppVersionReturnBean> appVersionRequest = new JsonBeanRequest<>(url, params, AppVersionReturnBean.class,
                new Response.Listener<AppVersionReturnBean>() {
                    @Override
                    public void onResponse(AppVersionReturnBean response) {
                        Message msg = new Message();
                        if (response.getResult().equals("1")) {
                            msg.what = Constants.AboutEasyList.GET_APP_VERSION_SUCCESS;
                            msg.obj = response;
                        } else {
                            msg.what = Constants.AboutEasyList.GET_APP_VERSION_FAILURE;
                            msg.obj = response.getMsg();
                        }
                        mHandler.sendMessage(msg);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.getMessage());
                Message msg = new Message();
                msg.what = Constants.requestException;
                msg.obj = error.getMessage();
                mHandler.sendMessage(msg);
            }
        });
        addToRequestQueue(appVersionRequest);
    }

    private void init() {
        initToolBar();
        boolean hasNewVersion = (boolean) SPUtils.get(getApplicationContext(), Constants.SP_HAS_NEW_VERSION, false);
        if (hasNewVersion) {
            updateNew.setVisibility(View.VISIBLE);
        } else {
            updateNew.setVisibility(View.GONE);
        }
    }

    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.about_easylist);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutEasyListActivity.this.finish();
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            switch (msg.what) {
                case Constants.AboutEasyList.GET_APP_VERSION_SUCCESS:
                    AppVersionReturnBean appVersionReturnBean = (AppVersionReturnBean) msg.obj;
                    int appVersionNum = Integer.parseInt(appVersionReturnBean.getAppVersionNum());
                    if (appVersionNum > AppUtils.getVersionCode(getApplicationContext())) {
                        //更新
                        UpdateDialog.show(AboutEasyListActivity.this, appVersionReturnBean.getAppDescribe(), appVersionReturnBean.getAppDownloadPath());
                    } else {
                        TT.showShort(getApplicationContext(), "当前已是最新版本");
                    }
                    break;
                case Constants.AboutEasyList.GET_APP_VERSION_FAILURE:
                    Logger.e(((AppVersionReturnBean) msg.obj).getMsg());
                    break;
                case Constants.requestException:
                    if (msg.obj != null && msg.obj.toString().contains("ConnectException")) {
                        TT.showShort(getApplicationContext(), "网络异常");
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
