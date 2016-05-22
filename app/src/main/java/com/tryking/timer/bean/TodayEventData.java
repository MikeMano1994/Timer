package com.tryking.timer.bean;

import java.io.Serializable;

/**
 * Created by Tryking on 2016/5/16.
 */
public class TodayEventData implements Serializable {
    private int dataType;
    private String startTime;
    private String endTime;
    private String specificEvent;

    public TodayEventData(int dataType) {
        this.dataType = dataType;
    }

    public TodayEventData(int dataType, String startTime, String endTime, String specificEvent) {
        this.dataType = dataType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificEvent = specificEvent;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSpecificEvent() {
        return specificEvent;
    }

    public void setSpecificEvent(String specificEvent) {
        this.specificEvent = specificEvent;
    }

    @Override
    public String toString() {
        return "TodayEventData{" +
                "dataType=" + dataType +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", specificEvent='" + specificEvent + '\'' +
                '}';
    }
}
