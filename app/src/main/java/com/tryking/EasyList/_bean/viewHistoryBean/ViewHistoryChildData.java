package com.tryking.EasyList._bean.viewHistoryBean;

/**
 * Created by 26011 on 2016/8/3.
 */
public class ViewHistoryChildData {
    public static final int TYPE_NO_EVENT = 0;
    public static final int TYPE_WORK = 1;
    public static final int TYPE_AMUSE = 2;
    public static final int TYPE_LIFE = 3;
    public static final int TYPE_STUDY = 4;
    private int dataType;
    private String startTime;
    private String endTime;
    private String specificEvent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public ViewHistoryChildData(int dataType, String startTime, String endTime, String specificEvent, long id) {
        this.dataType = dataType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificEvent = specificEvent;
        this.id = id;
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

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ViewHistoryChildData{" +
                "dataType=" + dataType +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", specificEvent='" + specificEvent + '\'' +
                '}';
    }
}
