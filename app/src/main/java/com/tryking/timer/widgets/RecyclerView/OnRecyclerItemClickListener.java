package com.tryking.timer.widgets.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Tryking on 2016/5/23.
 */
public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    //用户按下屏幕就会触发
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    //如果按下的时间超过瞬间，而且按下的时候没有松开或者是拖动，就会执行
    @Override
    public void onShowPress(MotionEvent e) {

    }

    //一次单独的轻击抬起操作，也就是轻击一下屏幕，也就是普通点击
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    //屏幕拖动事件
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    //长按触屏
    @Override
    public void onLongPress(MotionEvent e) {

    }

    //滑屏，用户按下触摸屏，快速移动后松开
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    //单击事件，用来判定该次单击是SingleTap而不是DoubleTap
    //如果连续点击两次就是DoubleTap手势，如果只单击一次，系统等待一段时间后没有收到第二次点击则判定该次点击为SingleTap而不是DoubleTap，
    //然后触发SingleTapConfirmed方法。
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    //双击事件
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    //双击间隔中发生的动作。指触发onDoubleTap以后，在双击之间发生的其他动作
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
