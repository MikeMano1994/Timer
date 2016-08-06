package com.tryking.EasyList.z_disuse;

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
import com.tryking.EasyList.base.BaseFragment;
import com.tryking.EasyList.base.SystemInfo;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.global.InterfaceURL;
import com.tryking.EasyList.network.JsonBeanRequest;
import com.tryking.EasyList.utils.TT;
import com.tryking.EasyList.widgets.DateChooseDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;


/**
 * Created by 26011 on 2016/7/26.
 */
public class ViewHistoryFragment extends BaseFragment {

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
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;

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
                TT.showShort(getContext(), "长按Day");
                DateChooseDialog dateChooseDialog = new DateChooseDialog(getContext(), mHandler, "2015", "2015", "3", "5");
                dateChooseDialog.show();
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
        String curMonth = String.valueOf(calendar.get(Calendar.YEAR)) + "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
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
                            fragmentTransaction.replace(R.id.change_content, fragments.get(0));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            break;
                        case R.id.rb_week:
                            fragmentTransaction.replace(R.id.change_content, fragments.get(1));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            break;
                        case R.id.rb_month:
                            fragmentTransaction.replace(R.id.change_content, fragments.get(2));
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                            break;
                        case R.id.rb_all:
                            fragmentTransaction.replace(R.id.change_content, fragments.get(3));
                            fragmentTransaction.addToBackStack(null);
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
        fragments = new ArrayList<>();
        fragments.add(DayFragment.getInstance(viewMonthReturnBean));
        fragments.add(new WeekFragment());
        fragments.add(new MonthFragment());
        fragments.add(new AllFragment());

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.change_content, fragments.get(0));
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
