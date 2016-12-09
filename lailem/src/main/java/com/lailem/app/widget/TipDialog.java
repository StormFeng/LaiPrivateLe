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

public class TipDialog extends Dialog implements OnShowListener {

	private Context context;
	private View contentView;
	private Activity _activity;

	private View.OnClickListener onClickListener;

	private TextView title_tv;
	private TextView message_tv;
	private TextView action;
	private static final int DEFAULT_THEME = R.style.confirm_dialog;

	public TipDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public TipDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public TipDialog(Context context) {
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

		contentView = View.inflate(context, R.layout.dialog_tip, null);
		this.setContentView(contentView);

		title_tv = (TextView) findViewById(R.id.title);
		message_tv = (TextView) findViewById(R.id.message);
		action = (TextView) findViewById(R.id.action);

		setOnShowListener(this);
	}

	public TipDialog setTitleText(String title) {
		title_tv.setText(title);
		return this;
	}

	public TipDialog setTitleText(int resId) {
		title_tv.setText(resId);
		return this;
	}

	public TipDialog setMessageText(String message) {
		message_tv.setText(message);
		return this;
	}

	public TipDialog setMessageText(int resId) {
		message_tv.setText(resId);
		return this;
	}

    public TipDialog setAction(String text) {
        action.setText(text);
        return this;
    }

	public TipDialog setAction(String text, View.OnClickListener onClickListener) {
		action.setText(text);
		action.setOnClickListener(onClickListener);
		return this;
	}

    public TipDialog setAction(int resId) {
        action.setText(resId);
        return this;
    }

	public TipDialog setAction(int resId, View.OnClickListener onClickListener) {
		action.setText(resId);
		action.setOnClickListener(onClickListener);
		return this;
	}

	public TipDialog config(String title, String message, String action, View.OnClickListener actionOnClickListener) {
		return setTitleText(title).setMessageText(message).setAction(action, actionOnClickListener);
	}

    public TipDialog config(String title, String message, String action) {
        return setTitleText(title).setMessageText(message).setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

	public void onShow(DialogInterface dialog) {
	}

}
