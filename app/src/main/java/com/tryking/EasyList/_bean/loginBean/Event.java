package com.tryking.EasyList._bean.loginBean;

public class Event {
    private Long id;

    private String memberid;

    private String date;

    private String starttime;

    private String endtime;

    private String eventtypes;

    private String record;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid == null ? null : memberid.trim();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public String getEventtypes() {
        return eventtypes;
    }

    public void setEventtypes(String eventtypes) {
        this.eventtypes = eventtypes == null ? null : eventtypes.trim();
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record == null ? null : record.trim();
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", memberid='" + memberid + '\'' +
                ", date='" + date + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", eventtypes='" + eventtypes + '\'' +
                ", record='" + record + '\'' +
                '}';
    }
}