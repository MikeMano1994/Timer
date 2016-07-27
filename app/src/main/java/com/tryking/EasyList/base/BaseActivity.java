package com.tryking.EasyList.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.android.volley.Request;
import com.tryking.EasyList.utils.ActivityUtils;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().addActivity(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public <T> void addToRequestQueue(Request<T> req){
        EasyListApplication.getInstance().addToRequestQueue(req, this.getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EasyListApplication.getInstance().cancelPendingRequests(this.getClass().getName());

        ActivityUtils.getInstance().clearActivity(this);
    }
}
