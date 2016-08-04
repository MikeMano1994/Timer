package com.tryking.EasyList._activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.DayEventReturnBean;
import com.tryking.EasyList._bean.TodayEventData;
import com.tryking.EasyList._bean.loginBean.Event;
import com.tryking.EasyList.adapter.DayEventAdapterWithHeader;
import com.tryking.EasyList.adapter.TodayEventAdapter;
import com.tryking.EasyList.base.BaseActivity;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;
import com.tryking.EasyList.utils.TT;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewYesterdayActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.tv_one_word)
    TextView tvOneWord;
    @Bind(R.id.main_content)
    RecyclerView mainContent;

    private List<TodayEventData> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_yesterday);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        initToolBar();
        getYesterdayDataFromServer();
    }

    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_action_arrow_left_white_24dp);
//        toolBar.setLogo(R.mipmap.ic_launcher);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        toolBar.setTitle(R.string.view_yesterday);
//        toolBar.setSubtitle("今日");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewYesterdayActivity.this.finish();
            }
        });
        toolBar.inflateMenu(R.menu.view_yesterday);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /*
    从服务端获取昨日的数据
     */
    private void getYesterdayDataFromServer() {
        showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getApplicationContext()).getMemberId());
        params.put("date", CommonUtils.getPreviousDay((String) SPUtils.get(getApplicationContext(), ApplicationGlobal.CURRENT_DATE, ""), -1));
        String url = InterfaceURL.viewDayData;
        JsonBeanRequest<DayEventReturnBean> dayEventRequest = new JsonBeanRequest<>(url, params, DayEventReturnBean.class, new Response.Listener<DayEventReturnBean>() {
            @Override
            public void onResponse(DayEventReturnBean response) {
                Message msg = new Message();
                if (response.getResult().equals("1")) {
                    msg.what = Constants.ViewYesterDay.GET_YESTERDAY_DATA_SUCCESS;
                    msg.obj = response;
                } else {
                    msg.what = Constants.ViewYesterDay.GET_YESTERDAY_DATA_FAILURE;
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
        addToRequestQueue(dayEventRequest);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissLoadingDialog();
            switch (msg.what) {
                case Constants.ViewYesterDay.GET_YESTERDAY_DATA_SUCCESS:
                    DayEventReturnBean dayEventBean = (DayEventReturnBean) msg.obj;
                    List<Event> eventList = dayEventBean.getEventList();
                    initView(eventList);
                    break;
                case Constants.ViewYesterDay.GET_YESTERDAY_DATA_FAILURE:
                    String message = msg.obj.toString();
                    TT.showShort(getApplicationContext(), message);
                    break;
                case Constants.requestException:
                    break;
            }
        }
    };

    private void initView(List<Event> eventList) {
        mDatas = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            TodayEventData todayEventData = new TodayEventData(Integer.parseInt(eventList.get(i).getEventtypes()), eventList.get(i).getStarttime(),
                    eventList.get(i).getEndtime(), eventList.get(i).getRecord());
            mDatas.add(todayEventData);
        }
        mainContent.setLayoutManager(new LinearLayoutManager(ViewYesterdayActivity.this));
        mainContent.setAdapter(new DayEventAdapterWithHeader(ViewYesterdayActivity.this, mDatas, false));
    }
}
