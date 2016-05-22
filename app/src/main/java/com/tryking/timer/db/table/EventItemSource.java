package com.tryking.timer.db.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Tryking on 2016/5/18.
 */
@DatabaseTable(tableName = "event_item_source")
public class EventItemSource {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "userId")
    private String userId;

    @DatabaseField(columnName = "startTime")
    private int startTime;

    @DatabaseField(columnName = "stopTime")
    private int stopTime;

    @DatabaseField(columnName = "type")
    private int type;
    private String event;

    public EventItemSource(String userId, int startTime, int stopTime, int type, String event) {
        this.userId = userId;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.type = type;
        this.event = event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
