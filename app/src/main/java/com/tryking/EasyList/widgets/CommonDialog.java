package com.tryking.EasyList.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tryking.timer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tryking on 2016/5/23.
 */
public class CommonDialog extends Dialog {
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    private Context mContext;
    private LayoutInflater mInflater;
    private final View mDialogView;
    private String title, content, okText, cancelText;
    private View.OnClickListener okListener, cancelListener;

    public CommonDialog(Context context) {
        super(context,R.style.custom_alert_dialog);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDialogView = mInflater.inflate(R.layout.common_dialog, null);
        ButterKnife.bind(this, mDialogView);

    }

    public void setDialogContent(String title, String content, String okText, String cancelText, View.OnClickListener okClick, View.OnClickListener cancelClick) {
        this.title = title;
        this.content = content;
        this.okText = okText;
        this.cancelText = cancelText;
        this.okListener = okClick;
        this.cancelListener = cancelClick;

        initDialog();
    }

    private void initDialog() {
        if ("".equals(content) || okListener == null) {

        } else {
            tvDelete.setText(content);
            tvDelete.setOnClickListener(okListener);
        }
    }

    @Override
    public void show() {
        super.show();
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(mDialogView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
