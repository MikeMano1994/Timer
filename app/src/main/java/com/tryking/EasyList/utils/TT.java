package com.tryking.EasyList.utils;

import android.content.Context;

/**
 * Toast统一管理类
 * 
 * @author way
 * 
 */
public class TT {
	// TT
	private static android.widget.Toast toast;

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		try{
			if (null == toast) {
				toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}catch(Exception e){}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		try{
			if (null == toast) {
				toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}catch(Exception e){}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		try{
			if (null == toast) {
				toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}catch(Exception e){}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		try{
			if (null == toast) {
				toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}catch(Exception e){}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		try{
			if (null == toast) {
				toast = android.widget.Toast.makeText(context, message, duration);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}catch(Exception e){}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		try{
			if (null == toast) {
				toast = android.widget.Toast.makeText(context, message, duration);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}catch(Exception e){}
	}

	/** Hide the toast, if any. */
	public static void hideToast() {
		try{
			if (null != toast) {
				toast.cancel();
			}
		}catch(Exception e){}
	}
}
