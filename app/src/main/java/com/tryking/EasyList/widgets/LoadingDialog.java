package com.tryking.EasyList.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.tryking.EasyList.R;

import butterknife.Bind;

/**
 * Created by 26011 on 2016/7/27.
 */
public class LoadingDialog extends Dialog {
    @Bind(R.id.dialog_loading)
    LinearLayout dialogLoading;
    private Context mContext;
    private final View mLoadingDialog;
    private final AnimationSet appearAnim;
    private final AnimationSet disAppearAnim;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_alert_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        mContext = context;
        mLoadingDialog = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        appearAnim = (AnimationSet)AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_dialog_appear);
        disAppearAnim = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_dialog_disappear);
    }

    @Override
    public void show() {
        super.show();
        mLoadingDialog.startAnimation(appearAnim);
        setContentView(mLoadingDialog);
    }

    @Override
    public void dismiss() {
        mLoadingDialog.startAnimation(disAppearAnim);
        super.dismiss();
    }
}
