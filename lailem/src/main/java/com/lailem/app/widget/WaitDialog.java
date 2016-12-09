package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.lailem.app.R;

public class WaitDialog extends Dialog {
	private static int DEFAULT_WIDTH = 160; // 默认宽度
	private static int DEFAULT_HEIGHT = 120;// 默认高度
	private static int DEFAULT_STYLE = R.style.dialog_msg;
	private static int DEFAULT_LAYOUT = R.layout.dialog_msg;
	private TextView message;

	public WaitDialog(Context context) {
		this(context, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_LAYOUT, DEFAULT_STYLE);
	}

	public WaitDialog(Context context, int layout, int style) {
		this(context, DEFAULT_WIDTH, DEFAULT_HEIGHT, layout, style);
	}

	public WaitDialog(Context context, int width, int height, int layout, int style) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.dimAmount = 0.0f;
		window.setAttributes(params);
		message = (TextView) findViewById(R.id.message);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
	}

	public void setMessage(String msg) {
		message.setText(msg);
	}

	public void showMessage(String msg) {
		setMessage(msg);
		show();
	}
}
