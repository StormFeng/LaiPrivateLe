package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lailem.app.R;

public class VoteActiveDetailActionDialog extends Dialog implements View.OnClickListener {
	private Context context;

	private static final int DEFAULT_THEME = R.style.bottom_dialog;
	private OnActionClickListener onActionClickListener;

	public interface OnActionClickListener {
		void onClickQrCode();
		void onClickDelete();
		void onClickShare();
	}

	public VoteActiveDetailActionDialog(Context context) {
		super(context, DEFAULT_THEME);
		init(context);
	}

	public VoteActiveDetailActionDialog(Context context, int theme) {
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

		View contentView = View.inflate(context, R.layout.dialog_vote_active_detail_action, null);
		this.setContentView(contentView);
		contentView.findViewById(R.id.qrCode).setOnClickListener(this);
		contentView.findViewById(R.id.share).setOnClickListener(this);
		contentView.findViewById(R.id.delete).setOnClickListener(this);
		contentView.findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qrCode:
			if (onActionClickListener != null) {
				onActionClickListener.onClickQrCode();
			}
			dismiss();
			break;
		case R.id.delete:
				if (onActionClickListener != null) {
					onActionClickListener.onClickDelete();
				}
				dismiss();
			break;
		case R.id.share:
			if (onActionClickListener != null) {
				onActionClickListener.onClickShare();
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
	}

	public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
		this.onActionClickListener = onActionClickListener;
	}
}
