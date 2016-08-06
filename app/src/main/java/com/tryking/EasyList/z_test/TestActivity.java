package com.tryking.EasyList.z_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.tryking.EasyList.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data_provider";
    private static final String FRAGMENT_LIST_VIEW = "list_view";

    @Bind(R.id.container)
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        getSupportFragmentManager().beginTransaction().add(
                new ExampleExpandableDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                .commit();
        getSupportFragmentManager().beginTransaction().add(
                R.id.container, new ExpandableExampleFragment(), FRAGMENT_LIST_VIEW).commit();
    }

    public AbstractExpandableDataProvider getDataprovider() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((ExampleExpandableDataProviderFragment) fragment).getDataProvider();
    }

}
