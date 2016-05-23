package com.tryking.timer.widgets.SwipeMenu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.orhanobut.logger.Logger;

/**
 * Created by Tryking on 2016/5/22.
 */
public class ItemSlideHelper implements RecyclerView.OnItemTouchListener, GestureDetector.OnGestureListener {
    private final int DEFAULT_DURATION = 200;
    private View mTargetView;
    private int mActivePointerId;

    private int mLastX;
    private int mLastY;

    private int mMaxVelocity;
    private int mMinVelocity;

    private CallBack mCallback;
    private boolean mIsDragging;

    private int mTouchSlop;

    private Animator mExpandAndCollapseAnim;
    private GestureDetectorCompat mGestureDetector;

    public ItemSlideHelper(Context context, CallBack callback) {
        this.mCallback = callback;
        //手势用于处理fling
        mGestureDetector = new GestureDetectorCompat(context, this);

        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Logger.e("onInterceptTouchEvent:" + e.getAction());

        //触摸的动作,按下，抬起，滑动，多点按下，多点抬起。比getAction()多了触控点的信息判断
        int actionMasked = MotionEventCompat.getActionMasked(e);
        int x = (int) e.getX();
        int y = (int) e.getY();

        //如果RecyclerView滚动状态不是空闲，targetView不是空
        if (rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
            if (mTargetView != null) {
                //隐藏已经打开
                smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                mTargetView = null;
            }
            return false;
        }
        //如果正在运行动画，直接拦截什么都不做
        if (mExpandAndCollapseAnim != null && mExpandAndCollapseAnim.isRunning()) {
            return true;
        }

        boolean needIntercept = false;
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(e, 0);
                mLastX = (int) e.getX();
                mLastY = (int) e.getY();

                /*
                *如果之前有一个已经打开的条目，当此次点击事件没有发生在右侧的菜单中则返回true，
                * 如果点击的是右侧菜单，那么返回False。
                */
                if (mTargetView != null) {
                    return !inView(x, y);
                }
                //查找需要显示菜单的View
                mTargetView = mCallback.findTargetView(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;

                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    return false;
                }

                //如果移动达到要求，拦截
                needIntercept = mIsDragging = mTargetView != null && Math.abs(deltaX) >= mTouchSlop;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /*
                没有发生过拦截事件走这儿
                 */
                if (isExpanded()) {
                    if (inView(x, y)) {
                        Logger.e("Click Item");
                    } else {
                        //拦截事件，防止targetView执行onClick事件
                        needIntercept = true;
                    }
                    //折叠菜单
                    smoothHorizontalExpandOrCollapse(DEFAULT_DURATION / 2);
                }
                mTargetView = null;
                break;
        }
        return needIntercept;
    }

    public int getHorizontalRange() {
        RecyclerView.ViewHolder childViewHolder = mCallback.getChildViewHolder(mTargetView);
        return mCallback.getHorizontalRange(childViewHolder);
    }

    public boolean isExpanded() {
        return mTargetView != null && mTargetView.getScrollX() == getHorizontalRange();
    }

    /*
    折叠菜单
     */
    private boolean smoothHorizontalExpandOrCollapse(float velocityX) {
        int scrollX = mTargetView.getScrollX();
        int scrollRange = getHorizontalRange();
        if (mExpandAndCollapseAnim != null) {
            return false;
        }
        int to = 0;
        int duration = DEFAULT_DURATION;

        if (velocityX == 0) {
            if (scrollX > scrollRange / 2) {
                to = scrollRange;
            }
        } else {
            if (velocityX > 0) {
                to = 0;
            } else {
                to = scrollRange;
            }
            duration = (int) ((1.f - Math.abs(velocityX) / mMaxVelocity) * DEFAULT_DURATION);
        }

        if (to == scrollX) {
            return false;
        }

        mExpandAndCollapseAnim = ObjectAnimator.ofInt(mTargetView, "scrollX", to);
        mExpandAndCollapseAnim.setDuration(duration);
        mExpandAndCollapseAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mExpandAndCollapseAnim = null;
                if (isCollapsed()) {
                    mTargetView = null;
                }
                Logger.e("onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mExpandAndCollapseAnim = null;
                Logger.e("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mExpandAndCollapseAnim.start();
        return true;
    }

    /*
     *根据targetView的ScrollX计算出targetView的偏移，这样能够知道这个point是在菜单中
     */
    private boolean inView(int x, int y) {
        if (mTargetView == null) {
            return false;
        }
        int scrollX = mTargetView.getScrollX();
        int left = mTargetView.getWidth() - scrollX;
        int top = mTargetView.getTop();
        int right = left + getHorizontalRange();
        int bottom = mTargetView.getBottom();
        Rect rect = new Rect(left, top, right, bottom);
        return rect.contains(x, y);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Logger.e("onTouchEvent:" + e.getAction());
        if (mExpandAndCollapseAnim != null && mExpandAndCollapseAnim.isRunning() || mTargetView == null) {
            return;
        }
        //如果要响应fling事件将mIsDragging设为false
        if (mGestureDetector.onTouchEvent(e)) {
            mIsDragging = false;
            return;
        }
        int x = (int) e.getX();
        int y = (int) e.getY();
        int actionMasked = MotionEventCompat.getActionMasked(e);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                //RecyclerView不会触发这个Down事件
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastX - e.getX());
                if (mIsDragging) {
                    horizontalDrag(deltaX);
                }
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    if (!smoothHorizontalExpandOrCollapse(0) && isCollapsed()) {
                        mTargetView = null;
                    }
                    mIsDragging = false;
                }
                break;
        }
    }

    private boolean isCollapsed() {
        return mTargetView != null && mTargetView.getScrollX() == 0;
    }

    /*
    根据touch事件来滚动View的ScrollX
     */
    private void horizontalDrag(int delta) {
        int scrollX = mTargetView.getScrollX();
        int scrollY = mTargetView.getScrollY();
        if ((scrollX + delta) <= 0) {
            mTargetView.scrollTo(0, scrollY);
            return;
        }

        int horizontalRange = getHorizontalRange();
        scrollX += delta;
        if (Math.abs(scrollX) < horizontalRange) {
            mTargetView.scrollTo(scrollX, scrollY);
        } else {
            mTargetView.scrollTo(horizontalRange, scrollY);
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > mMinVelocity && Math.abs(velocityX) < mMaxVelocity) {
            if (!smoothHorizontalExpandOrCollapse(velocityX)) {
                if (isCollapsed()) {
                    mTargetView = null;
                    return true;
                }
            }
        }
        return false;
    }

    public interface CallBack {
        int getHorizontalRange(RecyclerView.ViewHolder holder);

        RecyclerView.ViewHolder getChildViewHolder(View childView);

        View findTargetView(float x, float y);
    }
}
