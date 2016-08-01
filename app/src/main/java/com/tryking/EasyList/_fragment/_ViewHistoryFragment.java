package com.tryking.EasyList._fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList._bean.viewHistoryBean.ViewMonthReturnBean;
import com.tryking.EasyList._fragment.viewhistory.AllFragment;
import com.tryking.EasyList._fragment.viewhistory.DayFragment;
import com.tryking.EasyList._fragment.viewhistory.MonthFragment;
import com.tryking.EasyList._fragment.viewhistory.WeekFragment;
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.DateChooseDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;


/**
 * Created by 26011 on 2016/7/26.
 */
public class _ViewHistoryFragment extends BaseFragment {

    @Bind(R.id.rb_day)
    RadioButton rbDay;
    @Bind(R.id.rb_week)
    RadioButton rbWeek;
    @Bind(R.id.rb_month)
    RadioButton rbMonth;
    @Bind(R.id.rb_all)
    RadioButton rbAll;
    @Bind(R.id.change_content)
    FrameLayout changeContent;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String curMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_history_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    @OnLongClick({R.id.rb_day})
    boolean click(View v) {
        switch (v.getId()) {
            case R.id.rb_day:
                if (rbDay.isChecked()) {
                    DateChooseDialog dateChooseDialog = new DateChooseDialog(getContext(), mHandler, "2015", "2016", "1", "8");
                    dateChooseDialog.show();
                }
                break;
        }
        return false;
    }


    private void init() {
        rbDay.setChecked(true);
        setRadioButtonChangeListener(rbDay);
        setRadioButtonChangeListener(rbWeek);
        setRadioButtonChangeListener(rbMonth);
        setRadioButtonChangeListener(rbAll);

        Calendar calendar = Calendar.getInstance();
        curMonth = String.valueOf(calendar.get(Calendar.YEAR)) + CommonUtils.add0toOneChar(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        fragmentManager = getFragmentManager();
        getDataOfMonthFromServer(curMonth);
    }

    /*
    给radioButton设置监听
     */
    private void setRadioButtonChangeListener(final RadioButton rb) {
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb.setTextColor(getResources().getColor(R.color.white));
                    fragmentTransaction = fragmentManager.beginTransaction();
                    switch (rb.getId()) {
                        case R.id.rb_day:
                            getDataOfMonthFromServer(curMonth);

                            break;
                        case R.id.rb_week:
                            WeekFragment weekFragment = new WeekFragment();
                            fragmentTransaction.replace(R.id.change_content, weekFragment);
                            fragmentTransaction.commit();
                            break;
                        case R.id.rb_month:
                            MonthFragment monthFragment = new MonthFragment();
                            fragmentTransaction.replace(R.id.change_content, monthFragment);
                            fragmentTransaction.commit();
                            break;
                        case R.id.rb_all:
                            AllFragment allFragment = new AllFragment();
                            fragmentTransaction.replace(R.id.change_content, allFragment);
                            fragmentTransaction.commit();
                            break;
                        default:
                            break;
                    }
                } else {
                    rb.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    /**
     * 从服务端获取指定月份的数据
     *
     * @param curMonth
     */
    private void getDataOfMonthFromServer(String curMonth) {
        showLoadingDialog();
        Map<String, String> params = new HashMap<>();
        params.put("memberId", SystemInfo.getInstance(getContext()).getMemberId());
        params.put("month", curMonth);
        Logger.e("开始根据月份拿数据了:" + params.toString());
        String url = InterfaceURL.viewMonthData;

        JsonBeanRequest<ViewMonthReturnBean> viewMonthRequest = new JsonBeanRequest<>(url, params, ViewMonthReturnBean.class,
                new Response.Listener<ViewMonthReturnBean>() {
                    @Override
                    public void onResponse(ViewMonthReturnBean response) {
                        Logger.e("成功了：" + response.toString());
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
                mHandler.sendEmptyMessage(Constants.requestException);
            }
        });
        addToRequestQueue(viewMonthRequest);
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
                    TT.showShort(getContext(), "Y:" + choseYear + "|||M:" + choseMonth);
                    getDataOfMonthFromServer(choseYear + CommonUtils.add0toOneChar(choseMonth));
                    break;
                case Constants.ViewHistory.GET_DATA_FOR_MONTH_SUCCESS:
                    ViewMonthReturnBean viewMonthReturnBean = (ViewMonthReturnBean) msg.obj;
                    refreshMainContent(viewMonthReturnBean);
                    break;
                case Constants.ViewHistory.GET_DATA_FOR_MONTH_FAILURE:
                    break;
                case Constants.requestException:
                    break;
                default:
                    break;
            }
        }
    };

    /*
    更新DayFragment中的内容
     */
    private void refreshMainContent(ViewMonthReturnBean viewMonthReturnBean) {
        DayFragment dayFragment = DayFragment.getInstance(viewMonthReturnBean);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.change_content, dayFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
