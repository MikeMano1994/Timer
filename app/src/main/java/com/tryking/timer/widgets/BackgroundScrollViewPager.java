package com.tryking.timer.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.tryking.timer.R;

/**
 * Created by Tryking on 2016/6/24.
 */
public class BackgroundScrollViewPager extends ViewPager {
    private Context mContext;
    private Bitmap mBitmapBackground;

    public BackgroundScrollViewPager(Context context) {
        this(context, null);
    }

    public BackgroundScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BackgroundScrollViewPager);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) array.getDrawable(R.styleable.BackgroundScrollViewPager_scrollBackground);
        mBitmapBackground = bitmapDrawable.getBitmap();
        array.recycle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBitmapBackground != null) {
            int width = mBitmapBackground.getWidth();
            int height = mBitmapBackground.getHeight();
            int count = getAdapter().getCount();
            int scrollX = getScrollX();
            //每个Item需要显示的图片的宽度
            float widthForItem = width * 1.0f / count;
            //控件每移动一个像素，图片应该移动的像素值
            float widthForPerPx = widthForItem * 1.0f / getWidth();
            Rect src = new Rect((int) (scrollX * widthForPerPx), 0, (int) (scrollX * widthForPerPx + widthForItem), height);
            Rect dest = new Rect(scrollX, 0, scrollX + getWidth(), getHeight());
            canvas.drawBitmap(mBitmapBackground, src, dest, null);
        }
        super.dispatchDraw(canvas);
    }
}
