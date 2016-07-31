package com.tryking.EasyList.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.NumberPicker;

import com.orhanobut.logger.Logger;
import com.tryking.EasyList.R;
import com.tryking.EasyList.global.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 26011 on 2016/7/30.
 */
public class DateChooseDialog extends Dialog {
    @Bind(R.id.np_year)
    NumberPicker npYear;
    @Bind(R.id.np_month)
    NumberPicker npMonth;
    private Context mContext;
    private final AnimationSet appearAnim;
    private final AnimationSet disAppearAnim;
    private final View mDateChooseDialog;
    private Handler mHandler;
    private String chooseYear;
    private String chooseMonth;
    private int sMonth;
    private int eMonth;

    public DateChooseDialog(Context context, Handler handler, String startYear, String endYear, String startMonth, String endMonth) {
        super(context, R.style.loading_alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mContext = context;
        mDateChooseDialog = LayoutInflater.from(context).inflate(R.layout.dialog_date_choose, null);
        ButterKnife.bind(this, mDateChooseDialog);

        appearAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_dialog_appear);
        disAppearAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_dialog_disappear);

        mHandler = handler;
        initNumberPicker(startYear, endYear, startMonth, endMonth);
    }

    private void initNumberPicker(String startYear, String endYear, String startMonth, String endMonth) {
        int sYear = Integer.parseInt(startYear);
        int eYear = Integer.parseInt(endYear);
        sMonth = Integer.parseInt(startMonth);
        eMonth = Integer.parseInt(endMonth);
        npYear.setMinValue(sYear);
        npYear.setMaxValue(eYear);
        npMonth.setMinValue(sMonth);
        if (sYear == eYear) {
            npMonth.setMaxValue(eMonth);
        } else {
            npMonth.setMaxValue(12);
        }
        chooseYear = String.valueOf(npYear.getMinValue());
        chooseMonth = String.valueOf(1);

        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Logger.e("old:" + oldVal + "new:" + newVal);
                chooseYear = String.valueOf(newVal);
                if (newVal == npYear.getMinValue()) {
                    npMonth.setMinValue(sMonth);
                    npMonth.setMaxValue(12);
                } else if (newVal == npYear.getMaxValue()) {
                    npMonth.setMinValue(1);
                    npMonth.setMaxValue(eMonth);
                } else {
                    npMonth.setMinValue(1);
                    npMonth.setMaxValue(12);
                }
            }
        });
        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Logger.e("old:" + oldVal + "new:" + newVal);
                chooseMonth = String.valueOf(newVal);
            }
        });
    }

    @Override
    public void show() {
        mDateChooseDialog.setAlpha(0.8f);
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.y = -300;
        this.onWindowAttributesChanged(lp);
        mDateChooseDialog.startAnimation(appearAnim);
        setContentView(mDateChooseDialog);
        super.show();
    }

    @Override
    public void dismiss() {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString(Constants.HANDLER_CHOSE_YEAR, chooseYear);
        b.putString(Constants.HANDLER_CHOSE_MONTH, chooseMonth);
        msg.setData(b);
        msg.what = Constants.ViewHistory.DAY_CHOSE_DATE;
        mHandler.sendMessage(msg);
        mDateChooseDialog.startAnimation(disAppearAnim);
        super.dismiss();
    }
}
