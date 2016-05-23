package com.tryking.timer.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tryking.timer.widgets.RecyclerView.MyItemDividerDecoration;
import com.tryking.timer.R;
import com.tryking.timer.bean.TodayEventData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.test)
    RecyclerView test;
    List list = new ArrayList<TodayEventData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            list.add(new TodayEventData(1, "1111", "1111", "1111"));
        }
        RecyAdapter adapter = new RecyAdapter(new WeakReference<Context>(TestActivity.this), list);
        test.setLayoutManager(new LinearLayoutManager(TestActivity.this));
        test.addItemDecoration(new MyItemDividerDecoration(TestActivity.this, MyItemDividerDecoration.VERTICAL_LIST));
        test.setAdapter(adapter);
    }
}
