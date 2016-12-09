package com.lailem.app.chat.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class FloatWindowManager {

	/**
	 * 小悬浮窗View的实例
	 */
	private static FloatWindowView floatWindowView;

	/**
	 * 小悬浮窗View的参数
	 */
	private static LayoutParams floatWindowParams;

	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;

	/**
	 * 用于获取手机可用内存
	 */
	private static ActivityManager mActivityManager;

	/**
	 * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void createFloatWindow(Context context) {
		if (floatWindowView == null) {
			WindowManager windowManager = getWindowManager(context);
			int screenWidth = windowManager.getDefaultDisplay().getWidth();
			int screenHeight = windowManager.getDefaultDisplay().getHeight();
			floatWindowView = new FloatWindowView(context);
			if (floatWindowParams == null) {
				floatWindowParams = new LayoutParams();
				floatWindowParams.type = LayoutParams.TYPE_PHONE;
				floatWindowParams.format = PixelFormat.RGBA_8888;
				floatWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				floatWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				floatWindowParams.width = FloatWindowView.viewWidth;
				floatWindowParams.height = FloatWindowView.viewHeight;
				floatWindowParams.x = screenWidth;
				floatWindowParams.y = screenHeight / 2;
			}
			floatWindowView.setParams(floatWindowParams);
			windowManager.addView(floatWindowView, floatWindowParams);
		}
	}

	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void removeFloatWindow(Context context) {
		if (floatWindowView != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(floatWindowView);
			floatWindowView = null;
		}
	}

	

	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	 * 
	 * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
	 */
	public static boolean isWindowShowing() {
		return floatWindowView != null;
	}

	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	/**
	 * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return ActivityManager的实例，用于获取手机可用内存。
	 */
	private static ActivityManager getActivityManager(Context context) {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}


}
