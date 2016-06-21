package com.tryking.timer.fragment.viewhistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tryking.timer.R;
import com.tryking.timer.db.dao.EverydayEventSourceDao;
import com.tryking.timer.db.table.EverydayEventSource;
import com.tryking.timer.utils.CommonUtils;
import com.tryking.timer.utils.TT;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/6/20.
 */
public class ViewHistoryCalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_history_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String getSelectedDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        CalendarDay date = calendarView.getSelectedDate();
        if (date != null) {
//            return FORMATTER.format(date.getDate());
            return dateFormat.format(date.getDate());
        }
        return "未选择";
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String selectedDate = CommonUtils.clearHanZiFromStr(getSelectedDateString());
        if (!judgeIsDatabaseHaveEvent(selectedDate)) {
            TT.showShort(getActivity(), "此日未添加过数据");
        } else {
            ViewHistoryDetailFragment viewHistoryDetailFragment = ViewHistoryDetailFragment.getInstance(selectedDate);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.rl_content, viewHistoryDetailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /*
    判断数据库中是否有存的数据
     */
    private boolean judgeIsDatabaseHaveEvent(String selectedDate) {
        EverydayEventSourceDao everydayEventDao = new EverydayEventSourceDao(getActivity());
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", "");
            map.put("eventDate", selectedDate);
            List<EverydayEventSource> eventList = everydayEventDao.query(map);

            if (eventList == null || eventList.size() <= 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
