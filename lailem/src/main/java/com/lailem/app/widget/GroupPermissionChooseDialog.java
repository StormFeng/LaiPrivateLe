package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lailem.app.R;

public class GroupPermissionChooseDialog extends Dialog implements android.view.View.OnClickListener {
	private Context context;

	private static final int DEFAULT_THEME = R.style.bottom_dialog;
	private OnGroupPermissionChooseListener onGroupPermissionChooseListener;

	public interface OnGroupPermissionChooseListener {
		void onGroupPermissionChoose(String name, String value);
	}

	public GroupPermissionChooseDialog(Context context) {
		super(context, DEFAULT_THEME);
		init(context);
	}

	public GroupPermissionChooseDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	private void init(Context context) {
		this.context = context;

		Window w = this.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.setCanceledOnTouchOutside(true);

		View contentView = View.inflate(context, R.layout.dialog_group_permission, null);
		this.setContentView(contentView);
		contentView.findViewById(R.id.publicType).setOnClickListener(this);
		contentView.findViewById(R.id.privateType).setOnClickListener(this);
		contentView.findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publicType:
			if (onGroupPermissionChooseListener != null) {
				onGroupPermissionChooseListener.onGroupPermissionChoose("公开群", "1");
			}
			dismiss();
			break;
		case R.id.privateType:
			if (onGroupPermissionChooseListener != null) {
				onGroupPermissionChooseListener.onGroupPermissionChoose("私有群", "2");
			}
			dismiss();
		case R.id.cancel:
			dismiss();
			break;
		}
	}

	public void setOnGroupPermissionChooseListener(OnGroupPermissionChooseListener onGroupPermissionChooseListener) {
		this.onGroupPermissionChooseListener = onGroupPermissionChooseListener;
	}
}
