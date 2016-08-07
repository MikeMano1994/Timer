package com.tryking.EasyList.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.viewHistoryBean.ViewMonthReturnBean;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.fragment.viewhistory.ViewHistoryFragment;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.DateChooseDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewHistoryActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.chose_month)
    LinearLayout choseMonth;
    @Bind(R.id.arrow)
    ImageView arrow;
    @Bind(R.id.main_content)
    FrameLayout mainContent;
    @Bind(R.id.top_date)
    TextView topDate;
    @Bind(R.id.show_no_net)
    RelativeLayout showNoNet;
    @Bind(R.id.show_server_error)
    RelativeLayout showServerError;
    @Bind(R.id.show_no_data)
    RelativeLayout showNoData;
    @Bind(R.id.show_no_content)
    RelativeLayout showNoContent;

    private String curMonth;
    private ViewHistoryFragment viewHistoryFragment;
    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        ButterKnife.bind(this);

        init();
    }

    @OnClick({R.id.chose_month})
    void click(View v) {
        switch (v.getId()) {
            case R.id.chose_month:
                arrow.setImageResource(R.drawable.ic_arrow_drop_up_white_18dp);
                DateChooseDialog dateChooseDialog = new DateChooseDialog(this, mHandler, "2015", curMonth.substring(0, 4), "1", curMonth.substring(4));
                dateChooseDialog.show();
                dateChooseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_white_18dp));
                    }
                });
                break;
            default:
                break;
        }
    }

    private void init() {
        initToolBar();
        manager = getSupportFragmentManager();
        String curDate = (String) SPUtils.get(getApplicationContext(), ApplicationGlobal.CURRENT_DATE, "");
        curMonth = CommonUtils.getMonthFromDate(curDate);
        getMonthDataFromServer(curMonth);

    }

    /*
       初始化ToolBar
        */
    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHistoryActivity.this.finish();
            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissLoadingDialog();
            switch (msg.what) {
                case Constants.ViewHistory.DAY_CHOSE_DATE:
                    Bundle data = msg.getData();
                    String choseMonth = data.getString(Constants.HANDLER_CHOSE_MONTH);
                    String choseYear = data.getString(Constants.HANDLER_CHOSE_YEAR);
//                    TT.showShort(getApplicationContext(), "Y:" + choseYear + "|||M:" + choseMonth);
                    topDate.setText(CommonUtils.addNianYueToDate(choseYear + CommonUtils.add0toOneChar(choseMonth)));
                    getMonthDataFromServer(choseYear + CommonUtils.add0toOneChar(choseMonth));
                    break;
                case Constants.ViewHistory.GET_DATA_FOR_MONTH_SUCCESS:
                    ViewMonthReturnBean viewMonthReturnBean = (ViewMonthReturnBean) msg.obj;
                    if (viewMonthReturnBean.getMonthEvents() == null || viewMonthReturnBean.getMonthEvents().equals("")) {
                        manager.beginTransaction().hide(viewHistoryFragment).commit();
                        showView(showNoData);
                    } else {
                        hideAbnormalViews();
                        FragmentTransaction transaction = manager.beginTransaction();
                        viewHistoryFragment = new ViewHistoryFragment(viewMonthReturnBean);
                        transaction.replace(R.id.main_content, viewHistoryFragment);
                        transaction.commit();
                    }
                    break;
                case Constants.ViewHistory.GET_DATA_FOR_MONTH_FAILURE:
                    showView(showServerError);
                    break;
                case Constants.requestException:
                    if (msg.obj != null && msg.obj.toString().contains("java.net.ConnectException")) {
                        TT.showShort(getApplicationContext(), "网络异常");
                        showView(showNoNet);
                    } else {
                        TT.showShort(getApplicationContext(), "服务器开小差啦～");
                        showView(showServerError);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /*
    把异常的view全部隐藏
     */
    private void hideAbnormalViews() {
        showNoData.setVisibility(View.GONE);
        showNoNet.setVisibility(View.GONE);
        showServerError.setVisibility(View.GONE);
    }

    /*
    显示传入的view
     */
    private void showView(RelativeLayout view) {
        switch (view.getId()) {
            case R.id.show_no_content:
                showNoContent.setVisibility(View.VISIBLE);
                showNoData.setVisibility(View.GONE);
                showNoNet.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                break;
            case R.id.show_no_data:
                showNoData.setVisibility(View.VISIBLE);
                showNoNet.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showNoContent.setVisibility(View.GONE);
                break;
            case R.id.show_no_net:
                showNoNet.setVisibility(View.VISIBLE);
                showNoData.setVisibility(View.GONE);
                showServerError.setVisibility(View.GONE);
                showNoContent.setVisibility(View.GONE);
                break;
            case R.id.show_server_error:
                showServerError.setVisibility(View.VISIBLE);
                showNoNet.setVisibility(View.GONE);
                showNoData.setVisibility(View.GONE);
                showNoContent.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /*
    从服务端获取该月的所有事件
     */
    private void getMonthDataFromServer(String date) {
        showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("month", date);

        String url = InterfaceURL.viewMonthData;
        Logger.e("开始根据月份拿数据了:" + params.toString());
        JsonBeanRequest<ViewMonthReturnBean> monthRequest = new JsonBeanRequest<ViewMonthReturnBean>(url, params, ViewMonthReturnBean.class, new Response.Listener<ViewMonthReturnBean>() {
            @Override
            public void onResponse(ViewMonthReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.what = Constants.ViewHistory.GET_DATA_FOR_MONTH_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = Constants.ViewHistory.GET_DATA_FOR_MONTH_FAILURE;
                    msg.obj = response.getMsg();
                }
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e("失败了" + error);
                Message msg = new Message();
                msg.what = Constants.requestException;
                msg.obj = error.getMessage();
                mHandler.sendMessage(msg);
            }
        });
        addToRequestQueue(monthRequest);
    }

    //友盟统计：由Activity和Fragment构成的页面需要这样写
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
