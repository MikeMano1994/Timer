package com.tryking.EasyList.z_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by 26011 on 2016/8/3.
 */
public class ExampleExpandableDataProviderFragment extends Fragment {
    private ExampleExpandableDataProvider mDataProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //旋转时Fragment不会销毁
        setRetainInstance(true);
        mDataProvider = new ExampleExpandableDataProvider();
    }

    public AbstractExpandableDataProvider getDataProvider() {
        return mDataProvider;
    }
}
