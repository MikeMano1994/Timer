package com.tryking.EasyList._bean;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.tryking.EasyList._bean.loginBean.DayEvent;
import com.tryking.EasyList._bean.loginBean.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 26011 on 2016/7/31.
 */
public class EveryDayEvent implements Parcelable {
    private String date;
    private List<Event> eventList;

    public EveryDayEvent() {
    }

    protected EveryDayEvent(Parcel in) {
        EveryDayEvent everyDayEvent = new EveryDayEvent();
        everyDayEvent.date = in.readString();
        everyDayEvent.eventList = new ArrayList<Event>();
        in.readList(everyDayEvent.eventList, getClass().getClassLoader());
    }

    public static final Creator<EveryDayEvent> CREATOR = new Creator<EveryDayEvent>() {
        @Override
        public EveryDayEvent createFromParcel(Parcel in) {
            return new EveryDayEvent(in);
        }

        @Override
        public EveryDayEvent[] newArray(int size) {
            return new EveryDayEvent[size];
        }
    };

    //内容描述接口，没什么作用
    @Override
    public int describeContents() {
        return 0;
    }

    //写入接口函数，用来打包
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeList(eventList);
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
        return "EveryDayEvent{" +
                "date='" + date + '\'' +
                ", eventList=" + eventList +
                '}';
    }
}
