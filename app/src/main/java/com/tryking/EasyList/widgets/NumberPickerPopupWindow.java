package com.tryking.EasyList.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import com.tryking.timer.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tryking on 2016/5/14.
 */
public class NumberPickerPopupWindow extends PopupWindow {
    @Bind(R.id.bt_cancel)
    Button btCancel;
    @Bind(R.id.bt_confirm)
    Button btConfirm;
    @Bind(R.id.hour_picker)
    NumberPicker hourPicker;
    @Bind(R.id.minute_picker)
    NumberPicker minutePicker;
    private Context mContext;
    private final View mNumberPicker;
    private static NumberPickerPopupWindow mInstance;
    private int currentHour;
    private int currentMinute;

    public interface FinishChooseListener {
        void onFinished(int currentHour, int currentMinute);
    }

    private FinishChooseListener mListener;

    public void setFinishChooseListener(FinishChooseListener listener) {
        mListener = listener;
    }

    public static NumberPickerPopupWindow getInstance(Context context, int hourMinValue, int hourMaxValue, int minuteMinValue, int minuteMaxValue, int currentHour) {
//        if (mInstance == null) {
//            synchronized (NumberPickerPopupWindow.class) {
//                if (mInstance == null) {
//                    mInstance = new NumberPickerPopupWindow(context, hourMinValue, hourMaxValue, minuteMinValue, minuteMaxValue);
//                }
//            }
//        } else {
//            mInstance = null;
        if (mInstance != null) {
            mInstance.dismiss();
            mInstance = null;
        }
        mInstance = new NumberPickerPopupWindow(context, hourMinValue, hourMaxValue, minuteMinValue, minuteMaxValue, currentHour);
//        }
        return mInstance;
    }

    @OnClick({R.id.bt_cancel, R.id.bt_confirm})
    void click(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                mInstance.dismiss();
                break;
            case R.id.bt_confirm:
                if (mListener != null) {
                    mListener.onFinished(currentHour, currentMinute);
                }
                mInstance.dismiss();
                break;
            default:
                break;
        }
    }

    private NumberPickerPopupWindow(Context context, int hourMinValue, int hourMaxValue, int minuteMinValue, int minuteMaxValue, int curHour) {
        super(context);
        mContext = context;
        mNumberPicker = LayoutInflater.from(context).inflate(R.layout.popupwindow_number_picker, null);
        ButterKnife.bind(this, mNumberPicker);
        setContentView(mNumberPicker);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
        setBackgroundDrawable(colorDrawable);

        //这两个一起可以保证点击外部消失
        setOutsideTouchable(true);
        setFocusable(false);

        setHourValue(hourMinValue, hourMaxValue);
        setMinuteValue(minuteMinValue, minuteMaxValue);

        currentHour = curHour;
        currentMinute = minuteMinValue;

        hourPicker.setValue(currentHour - 1);
        changeDatas();
    }

    /*
    监听数据改变时候的数据
     */
    private void changeDatas() {
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentHour = newVal + 1;
            }
        });
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Logger.e("oldVal:" + oldVal + ":::newVal:" + newVal);
                currentMinute = newVal * 5;
            }
        });
    }

    public void setHourValue(int minValue, int maxValue) {
        String[] s = generateStringArray(minValue, maxValue, 1);
        int length = s.length;
        hourPicker.setDisplayedValues(s);
        hourPicker.setMaxValue(length - 1);
        hourPicker.setMinValue(0);
    }

    public void setMinuteValue(int minValue, int maxValue) {
        String[] s = generateStringArray(minValue, maxValue - 5, 5);
        int length = s.length;
        minutePicker.setDisplayedValues(s);
        minutePicker.setMaxValue(length - 1);
        minutePicker.setMinValue(0);
    }


    private String[] generateStringArray(int minValue, int maxValue, int step) {
        int length = (maxValue - minValue) / step;
        String[] s = new String[length + 1];
        int j = 0;
        for (int i = minValue; i <= maxValue; i++) {
            if (i % step == 0) {
                s[j] = i < 10 ? "0" + i : Integer.toString(i);
                j++;
            }
        }
        return s;
    }
}
