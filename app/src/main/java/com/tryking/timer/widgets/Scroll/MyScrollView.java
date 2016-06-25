package com.tryking.timer.widgets.Scroll;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.orhanobut.logger.Logger;

/**
 * Created by Tryking on 2016/6/21.
 */
public class MyScrollView extends NestedScrollView {
    private Context mContext;
    private int mScreenHeight;
    private boolean isSetted = false;
    private boolean isPieChart = true;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        //拿到屏幕的高度
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;//拿到屏幕的高度（像素）
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isSetted) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            PieChart wrapperPieChart = null;
            RecyclerView wrapperRecyclerView = null;
            for (int i = 0; i < wrapper.getChildCount(); i++) {
                if (wrapper.getChildAt(i) instanceof PieChart) {
                    wrapperPieChart = (PieChart) wrapper.getChildAt(i);
                } else if (wrapper.getChildAt(i) instanceof RecyclerView) {
                    wrapperRecyclerView = (RecyclerView) wrapper.getChildAt(i);
                }
            }
            //设置子View的高度为屏幕高
//            wrapperPieChart.getLayoutParams().height = mScreenHeight;
            wrapperRecyclerView.getLayoutParams().height = mScreenHeight;
            isSetted = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                int cretic = mScreenHeight / 5;
                if (isPieChart) {
                    Logger.e("isPieChart" + "::scrollY:" + scrollY + "::cretic::" + cretic);
                    if (scrollY <= cretic) {
                        this.smoothScrollTo(0, 0);
                    } else {
                        this.smoothScrollTo(0, mScreenHeight);
                        this.setFocusable(false);
                        isPieChart = false;
                    }
                } else {
                    Logger.e("isRecyclerView" + ":::scrollY:" + scrollY + "::cretic::" + cretic);
                    int scrollPadding = mScreenHeight = scrollY;
                    if (scrollPadding >= cretic) {
                        this.smoothScrollTo(0, 0);
                        isPieChart = true;
                    } else {
                        this.smoothScrollTo(0, mScreenHeight);
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
}
