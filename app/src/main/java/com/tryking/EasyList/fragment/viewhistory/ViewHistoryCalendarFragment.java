package com.tryking.EasyList.fragment.viewhistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.tryking.EasyList.R;
import com.tryking.EasyList.db.dao.EverydayEventSourceDao;
import com.tryking.EasyList.db.table.EverydayEventSource;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.TT;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private List<Integer> haveEventDays = new ArrayList<>();
    private boolean[] shouldShowDay;

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
        haveEventDays = getAddedDayFromDatabase();
//        Logger.e("开始打印");
//        for (int i = 0; i < haveEventDays.size(); i++) {
//            Logger.e(haveEventDays.get(i) + "");
//        }
        shouldShowDay = getShouldShowDay(haveEventDays);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        // TODO: 2016/6/23 这里没有按照月份来判断，就是直接拿的日的，等连接服务器后再考虑改
        calendarView.addDecorator(new PrimeDayDisableDecorator());
    }

    private boolean[] getShouldShowDay(List<Integer> haveEventDays) {
        boolean[] bool = new boolean[33];
        for (int i = 0; i < bool.length; i++) {
            bool[i] = true;
        }
        for (int i = 0; i < haveEventDays.size(); i++) {
            bool[haveEventDays.get(i)] = false;
        }
        return bool;
    }

    /**
     * 从数据库拿到添加过事件的day
     *
     * @return 添加过事件的list<integer>集合
     */
    private List<Integer> getAddedDayFromDatabase() {
        EverydayEventSourceDao everydayEventDao = new EverydayEventSourceDao(getActivity());
        List<Integer> list = new ArrayList<>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", "");
            List<EverydayEventSource> eventList = everydayEventDao.query(map);
            for (int i = 0; i < eventList.size(); i++) {
//                Logger.e(eventList.get(i).getEventDate());
                String eventDate = eventList.get(i).getEventDate();
                String substring = eventDate.substring(6);
                int day = Integer.parseInt(substring);
                list.add(day);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
            return dateFormat.format(date.getDate());
        }
        return "未选择";
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String selectedDateString = getSelectedDateString();
        String selectedDate = CommonUtils.clearHanZiFromStr(selectedDateString);
        if (!judgeIsDatabaseHaveEvent(selectedDate)) {
            TT.showShort(getActivity(), "此日未添加过数据");
        } else {
//            ViewHistoryDetailFragment viewHistoryDetailFragment = ViewHistoryDetailFragment.getInstance(selectedDateString);
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.rl_content, viewHistoryDetailFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
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
        //这里先粗暴的改为切换月份就让其都不能点，但是貌似下一个月改变的时候不会变
        for (int i = 0; i < shouldShowDay.length; i++) {
            shouldShowDay[i] = true;
        }
    }

    class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return shouldShowDay[day.getDay()];
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
