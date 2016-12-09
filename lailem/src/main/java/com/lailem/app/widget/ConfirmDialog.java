package com.lailem.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.lailem.app.R;

public class ConfirmDialog extends Dialog implements OnShowListener {

	private Context context;
	private View contentView;
	private Activity _activity;

	private View.OnClickListener onClickListener;

	private TextView title_tv;
	private TextView message_tv;
	private TextView left_action;
	private TextView right_action;
	private static final int DEFAULT_THEME = R.style.confirm_dialog;

	public ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public ConfirmDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public ConfirmDialog(Context context) {
		super(context, DEFAULT_THEME);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		this._activity = (Activity) context;
		Window w = this.getWindow();
		LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.height = LayoutParams.MATCH_PARENT;
		lp.width = LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.setCanceledOnTouchOutside(true);

		contentView = View.inflate(context, R.layout.dialog_confirm, null);
		this.setContentView(contentView);

		title_tv = (TextView) findViewById(R.id.title);
		message_tv = (TextView) findViewById(R.id.message);
		left_action = (TextView) findViewById(R.id.left);
		right_action = (TextView) findViewById(R.id.right);

		setOnShowListener(this);
	}

	public ConfirmDialog setTitleText(String title) {
		title_tv.setText(title);
		return this;
	}

	public ConfirmDialog setTitleText(int resId) {
		title_tv.setText(resId);
		return this;
	}

	public ConfirmDialog setMessageText(String message) {
		message_tv.setText(message);
		return this;
	}

	public ConfirmDialog setMessageText(int resId) {
		message_tv.setText(resId);
		return this;
	}

	public ConfirmDialog setLeft(String left,final  View.OnClickListener onClickListener) {
		left_action.setText(left);
		left_action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListener.onClick(view);
				dismiss();
			}
		});
		return this;
	}

	public ConfirmDialog setLeft(int resId, final View.OnClickListener onClickListener) {
		left_action.setText(resId);
		left_action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListener.onClick(view);
				dismiss();
			}
		});
		return this;
	}

	public ConfirmDialog setRight(String right, final View.OnClickListener onClickListener) {
		right_action.setText(right);
		right_action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListener.onClick(view);
				dismiss();
			}
		});
		return this;
	}

	public ConfirmDialog setRight(int resId, final View.OnClickListener onClickListener) {
		right_action.setText(resId);
		right_action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListener.onClick(view);
				dismiss();
			}
		});
		return this;
	}

	public ConfirmDialog config(String title, String message, String left, String right, View.OnClickListener leftOnClickListener, View.OnClickListener rightOnClickListener) {
		return setTitleText(title).setMessageText(message).setLeft(left, leftOnClickListener).setRight(right, rightOnClickListener);
	}

	public ConfirmDialog config(int titleResId, int messageResId, int leftResId, int rightResId, View.OnClickListener leftOnClickListener, View.OnClickListener rightOnClickListener) {
		return setTitleText(titleResId).setMessageText(messageResId).setLeft(leftResId, leftOnClickListener).setRight(rightResId, rightOnClickListener);
	}

	public ConfirmDialog config(String title, String message, final String right, final View.OnClickListener rightOnClickListener) {
		return setTitleText(title).setMessageText(message).setLeft("取消", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		}).setRight(right, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rightOnClickListener.onClick(view);
				dismiss();
			}
		});
	}

	public void onShow(DialogInterface dialog) {
	}

}
