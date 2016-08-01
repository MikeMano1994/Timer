package com.tryking.EasyList._bean.viewHistoryBean;

import com.tryking.EasyList._bean.BaseBean;
import com.tryking.EasyList._bean.loginBean.DayEvent;

import java.util.List;

/**
 * Created by 26011 on 2016/8/1.
 */
public class ViewMonthReturnBean extends BaseBean{
    private String memberId;
    private String month;
    //List中的数据为倒叙排列，比如29号的，28号的，27号的.....
    private List<DayEvent> monthEvents;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<DayEvent> getMonthEvents() {
        return monthEvents;
    }

    public void setMonthEvents(List<DayEvent> monthEvents) {
        this.monthEvents = monthEvents;
    }

    @Override
    public String toString() {
        return "ViewMonthReturnBean{" +
                "memberId='" + memberId + '\'' +
                ", month='" + month + '\'' +
                ", monthEvents=" + monthEvents +
                '}';
    }
}
