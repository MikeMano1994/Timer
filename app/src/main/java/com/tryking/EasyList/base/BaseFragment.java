package com.tryking.EasyList.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.tryking.EasyList.widgets.LoadingDialog;

/**
 * Created by 26011 on 2016/7/28.
 */
public class BaseFragment extends Fragment {
    private LoadingDialog loadingDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void showLoadingDialog() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public <T> void addToRequestQueue(Request<T> req) {
        EasyListApplication.getInstance().addToRequestQueue(req, this.getClass().getName());
    }
}
