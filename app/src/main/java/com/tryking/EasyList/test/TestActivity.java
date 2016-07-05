package com.tryking.EasyList.test;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tryking.EasyList.R;
import com.tryking.EasyList.widgets.BackgroundScrollViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    @Bind(R.id.test)
    BackgroundScrollViewPager test;
    private ArrayList<View> objects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        objects = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(TestActivity.this);
            imageView.setImageResource(R.drawable.ic_add);
            objects.add(imageView);
        }
        test.setAdapter(new MyAdapter());
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(objects.get(position));
            return objects.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return  view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(objects.get(position));
        }
    }
}
