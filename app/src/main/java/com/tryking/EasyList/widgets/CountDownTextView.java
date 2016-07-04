package com.tryking.EasyList.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tryking.timer.R;

/**
 * Created by Tryking on 2016/5/26.
 */
public class CountDownTextView extends TextView implements Runnable {
    private static long TWENTY_FOUR = 24;
    private static long SIXTY = 60;
    private static long ONE_DAY = TWENTY_FOUR * SIXTY * SIXTY;//一天所含的秒数
    private static long ONE_HOUR = SIXTY * SIXTY;//一小时所含的秒数
    private static long ONE_MINUTE = SIXTY;//一分钟所含的秒数

    private Long mTime;
    private long mDays;
    private long mHours;
    private long mMinutes;
    private long mSeconds;

    private boolean run;//是否运行
    private String preTime;//倒计时之前的文字

    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CountDownTextView);
//        array.get
        array.recycle();
        if (mTime != null) {
            //如果没有设置时长，默认给个时长
            mTime = new Long(0);
        }
        preTime = "";
        setRun(false);
    }

    private void initTime(long mTime) {
        long[] dateArray = getDateArray(mTime);
        mDays = dateArray[0];
        mHours = dateArray[1];
        mMinutes = dateArray[2];
        mSeconds = dateArray[3];
    }

    /**
     * 设置倒计时总时长，以及是否开始计时
     *
     * @param totalSecond
     * @param bool
     */
    public void setIntervalTime(long totalSecond, boolean bool) {
        if (!run) {
            setRun(bool);
            this.mTime = totalSecond;
            initTime(mTime);
            //开启倒计时线程
            setRun(true);
            run();
        }
    }

    /*
    根据一个总的秒数返回一个天时分秒数组
     */
    private long[] getDateArray(long totalSecond) {
        long days = totalSecond / ONE_DAY;
        long hours = (totalSecond - days * ONE_DAY) / ONE_HOUR;
        long minutes = (totalSecond - days * ONE_DAY - hours * ONE_HOUR) / ONE_MINUTE;
        long seconds = totalSecond - days * ONE_DAY - hours * ONE_HOUR - minutes * ONE_MINUTE;
        long[] times = {days, hours, minutes, seconds};
        return times;
    }

    /**
     * 运行线程
     */
    @Override
    public void run() {
        if (run) {
            if (mTime != null) {
                mTime--;
            }
            mSeconds--;
            if (mSeconds < 0) {
                mMinutes--;
                mSeconds = 59;
                if (mMinutes < 0) {
                    mHours--;
                    mMinutes = 59;
                    if (mHours < 0) {
                        mDays--;
                        mHours = 59;
                    }
                }
            }

            StringBuffer showStr = new StringBuffer();
            String days = String.valueOf(mDays);
            String hours = String.valueOf(mHours);
            String minutes = String.valueOf(mMinutes);
            String second = String.valueOf(mSeconds);
            showStr.append(days == "0" ? "" : days + "天")
                    .append(hours == "0" ? "" : hours + "小时")
                    .append(minutes == "0" ? "" : minutes + "分钟")
                    .append(second == "0" ? "" : second + "秒");
            if (days == "0" && hours == "0" && minutes == "0" && second == "0") {
                setRun(false);
            }
            this.setText(preTime + showStr);
            postDelayed(this, 1000);
        }
    }

    /**
     * 设置是否让其运行
     *
     * @param bool
     */
    public void setRun(boolean bool) {
        this.run = bool;
    }

    public void setTextPreTime(String textPreTime) {
        this.preTime = textPreTime;
    }
}
