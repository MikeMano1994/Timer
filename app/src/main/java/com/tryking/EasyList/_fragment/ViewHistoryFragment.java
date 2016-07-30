package com.tryking.EasyList._fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.FrameStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.tryking.EasyList.R;
import com.tryking.EasyList._fragment.viewhistory.AllFragment;
import com.tryking.EasyList._fragment.viewhistory.DayFragment;
import com.tryking.EasyList._fragment.viewhistory.MonthFragment;
import com.tryking.EasyList._fragment.viewhistory.WeekFragment;
import com.tryking.EasyList.utils.TT;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;


/**
 * Created by 26011 on 2016/7/26.
 */
public class ViewHistoryFragment extends Fragment {

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

        fragments = new ArrayList<>();
        fragments.add(new DayFragment());
        fragments.add(new WeekFragment());
        fragments.add(new MonthFragment());
        fragments.add(new AllFragment());

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.change_content, fragments.get(0));
        fragmentTransaction.commit();
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
                            fragmentTransaction.commit();
                            break;
                        case R.id.rb_week:
                            fragmentTransaction.replace(R.id.change_content, fragments.get(1));
                            fragmentTransaction.commit();

                            break;
                        case R.id.rb_month:
                            fragmentTransaction.replace(R.id.change_content, fragments.get(2));
                            fragmentTransaction.commit();

                            break;
                        case R.id.rb_all:
                            fragmentTransaction.replace(R.id.change_content, fragments.get(3));
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
