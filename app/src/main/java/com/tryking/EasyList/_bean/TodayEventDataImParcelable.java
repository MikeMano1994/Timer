package com.tryking.EasyList._bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 26011 on 2016/8/1.
 */
public class TodayEventDataImParcelable implements Parcelable{
    private int dataType;
    private String startTime;
    private String endTime;
    private String specificEvent;

    /**
     * @param dataType      0：无事件   1：工作    2：娱乐    3：生活    4：学习
     * @param startTime
     * @param endTime
     * @param specificEvent
     */
    public TodayEventDataImParcelable(int dataType, String startTime, String endTime, String specificEvent) {
        this.dataType = dataType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.specificEvent = specificEvent;
    }

    protected TodayEventDataImParcelable(Parcel in) {
        dataType = in.readInt();
        startTime = in.readString();
        endTime = in.readString();
        specificEvent = in.readString();
    }

    public static final Creator<TodayEventDataImParcelable> CREATOR = new Creator<TodayEventDataImParcelable>() {
        @Override
        public TodayEventDataImParcelable createFromParcel(Parcel in) {
            return new TodayEventDataImParcelable(in);
        }

        @Override
        public TodayEventDataImParcelable[] newArray(int size) {
            return new TodayEventDataImParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dataType);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(specificEvent);
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

    public static Creator<TodayEventDataImParcelable> getCREATOR() {
        return CREATOR;
    }
}
