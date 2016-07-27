package com.tryking.EasyList.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.tryking.EasyList.R;

/**
 * Created by 26011 on 2016/7/27.
 */
public class LoadingDialog extends Dialog {
    private Context mContext;
    private final View mLoadingDialog;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mContext = context;
        mLoadingDialog = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
    }

    @Override
    public void show() {
        super.show();
        setContentView(mLoadingDialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
