package com.tryking.EasyList._bean.loginBean;

import java.util.Date;
import java.util.List;

/**
 * Created by 26011 on 2016/7/25.
 */
public class DayEvent {

    private String memberId;
    private String date;
    private List<Event> eventList;
    private String oneWord;

    public String getOneWord() {
        return oneWord;
    }

    public void setOneWord(String oneWord) {
        this.oneWord = oneWord;
    }
    
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "DayEvent{" +
                "memberId='" + memberId + '\'' +
                ", date='" + date + '\'' +
                ", eventList=" + eventList +
                '}';
    }
}
