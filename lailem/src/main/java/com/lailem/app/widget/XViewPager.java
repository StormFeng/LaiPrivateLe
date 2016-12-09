package com.lailem.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by XuYang on 15/4/22.
 */
public class XViewPager extends ViewPager {

	private boolean isPagingEnabled = true;

	public XViewPager(Context context) {
		super(context);
	}

	public XViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.isPagingEnabled && super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return this.isPagingEnabled && super.onInterceptTouchEvent(event);
	}

	public void setPagingEnabled(boolean b) {
		this.isPagingEnabled = b;
	}

	public boolean isPagingEnabled() {
		return isPagingEnabled;
	}
}
