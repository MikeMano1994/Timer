package com.tryking.timer.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Tryking on 2016/5/27.
 * 具体的事件资源，也就是用户输入的具体事项
 */
@DatabaseTable(tableName = "specific_event_source")
public class SpecificEventSource {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "userId")
    private String userId;

    @DatabaseField(columnName = "eventDate")
    private String eventDate;

    @DatabaseField(columnName = "startTime")
    private String startTime;

    @DatabaseField(columnName = "specificEvent")
    private String specificEvent;

    public SpecificEventSource() {

    }

    public SpecificEventSource(String userId, String eventDate, String startTime, String specificEvent) {
        this.userId = userId;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.specificEvent = specificEvent;
    }

    public int getId() {
        return id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSpecificEvent() {
        return specificEvent;
    }

    public void setSpecificEvent(String specificEvent) {
        this.specificEvent = specificEvent;
    }

    @Override
    public String toString() {
        return "SpecificEventSource{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", specificEvent='" + specificEvent + '\'' +
                '}';
    }
}
