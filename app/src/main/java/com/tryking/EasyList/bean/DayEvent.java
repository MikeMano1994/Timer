package com.tryking.EasyList.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by 26011 on 2016/7/25.
 */
public class DayEvent {
    private Date date;
    private String startTimes;
    private String endTimes;
    private String eventTypes;
    private List<eventByStart> eventList;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(String startTimes) {
        this.startTimes = startTimes;
    }

    public String getEndTimes() {
        return endTimes;
    }

    public String getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(String eventTypes) {
        this.eventTypes = eventTypes;
    }

    public void setEndTimes(String endTimes) {
        this.endTimes = endTimes;
    }

    public List<eventByStart> getEventList() {
        return eventList;
    }

    public void setEventList(List<eventByStart> eventList) {
        this.eventList = eventList;
    }
}
