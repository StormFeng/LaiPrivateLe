package com.lailem.app.widget;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SlidingPanelLayoutExtended extends SlidingPaneLayout {

	public static final int DEFAULT_DRAGGING_START_X = -1;
	public static final int SLIDE_FROM_LEFT_EDGE = 20;

	private int startDraggingX = DEFAULT_DRAGGING_START_X;

	public SlidingPanelLayoutExtended(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingPanelLayoutExtended(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SlidingPanelLayoutExtended(Context context) {
		super(context);
	}

	public int getStartDraggingX() {
		return startDraggingX;
	}

	public void setStartDraggingX(int startX) {
		this.startDraggingX = startX;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (startDraggingX > DEFAULT_DRAGGING_START_X) {
			if ((startDraggingX == SLIDE_FROM_LEFT_EDGE && ev.getAction() == MotionEvent.ACTION_DOWN) || ev.getX() <= DEFAULT_DRAGGING_START_X) {
				return super.onTouchEvent(ev);
			} else {
				return false;
			}
		}
		return super.onTouchEvent(ev);
	}
}
