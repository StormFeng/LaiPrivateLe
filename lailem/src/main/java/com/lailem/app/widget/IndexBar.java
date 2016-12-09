package com.lailem.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;

import java.util.ArrayList;

/**
 * Created by XuYang on 14/12/28.
 */
public class IndexBar extends FrameLayout {
	private Context context;

	private LinearLayout letters_ll;

	private char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private ArrayList<TextView> letterTVs = new ArrayList<TextView>();

	private int allHeight = 0;
	private TextView overlay;

	private OnIndexChangeListener listener;

	public interface OnIndexChangeListener {
		void onIndexChange(char c);
	}

	public IndexBar(Context context) {
		super(context);
		init(context);
	}

	public IndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_indexbar, this);

		letters_ll = (LinearLayout) findViewById(R.id.letters);
		initOverlay();

		post(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < letters_ll.getChildCount(); i++) {
					TextView tv = (TextView) letters_ll.getChildAt(i);
					letterTVs.add(tv);
					Rect rect = new Rect();
					tv.getLocalVisibleRect(rect);
					allHeight += (rect.bottom - rect.top);
				}

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int y = (int) event.getY();

		int index = (int) (y * 1.0 / allHeight * 26);
		overlay.setText(letters[index] + "");

		if (listener != null) {
			listener.onIndexChange(letters[index]);
		}

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			overlay.setVisibility(View.VISIBLE);
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			overlay.setVisibility(View.GONE);
			break;
		}
		return true;
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		overlay = (TextView) View.inflate(context, R.layout.view_alpha_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
		Activity activity = (Activity) context;
		while (activity.getParent() != null) {
			activity = activity.getParent();
		}
		WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Activity activity = (Activity) context;
		while (activity.getParent() != null) {
			activity = activity.getParent();
		}
		WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		windowManager.removeView(overlay);
	}

	public void setListener(OnIndexChangeListener listener) {
		this.listener = listener;
	}

	public OnIndexChangeListener getListener() {
		return listener;
	}
}
