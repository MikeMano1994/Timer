//package com.tryking.EasyList.db.table;
//
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.table.DatabaseTable;
//
///**
// * Created by Tryking on 2016/5/18.
// * 存储每日的事项，不包括具体的事件
// */
//@DatabaseTable(tableName = "everyday_event_source")
//public class EverydayEventSource {
//
//    @DatabaseField(generatedId = true)
//    private int id;
//
//    @DatabaseField(columnName = "userId")
//    private String userId;
//
//    @DatabaseField(columnName = "eventDate")
//    private String eventDate;
//
//    @DatabaseField(columnName = "startTimes")
//    private String startTimes;
//
//    @DatabaseField(columnName = "endTimes")
//    private String endTimes;
//
//    @DatabaseField(columnName = "eventTypes")
//    private String eventTypes;
//
//    public EverydayEventSource() {
//
//    }
//
//    public EverydayEventSource(String userId, String eventDate, String startTimes, String endTimes, String eventTypes) {
//        this.userId = userId;
//        this.eventDate = eventDate;
//        this.startTimes = startTimes;
//        this.endTimes = endTimes;
//        this.eventTypes = eventTypes;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getEventDate() {
//        return eventDate;
//    }
//
//    public void setEventDate(String eventDate) {
//        this.eventDate = eventDate;
//    }
//
//    public String getStartTimes() {
//        return startTimes;
//    }
//
//    public void setStartTimes(String startTimes) {
//        this.startTimes = startTimes;
//    }
//
//    public String getEndTimes() {
//        return endTimes;
//    }
//
//    public void setEndTimes(String endTimes) {
//        this.endTimes = endTimes;
//    }
//
//    public String getEventTypes() {
//        return eventTypes;
//    }
//
//    public void setEventTypes(String eventTypes) {
//        this.eventTypes = eventTypes;
//    }
//
//    @Override
//    public String toString() {
//        return "EverydayEventSource{" +
//                "id=" + id +
//                ", userId='" + userId + '\'' +
//                ", eventDate='" + eventDate + '\'' +
//                ", startTimes='" + startTimes + '\'' +
//                ", endTimes='" + endTimes + '\'' +
//                ", eventTypes='" + eventTypes + '\'' +
//                '}';
//    }
//}
