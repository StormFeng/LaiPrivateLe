package com.lailem.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lailem.app.R;

/**
 * 图片选择对话框 Created by XuYang on 15/4/13.
 */
public class PicChooserDialog extends Dialog implements View.OnClickListener {

	public interface OnPicChooserListener {
		void onClickFromGallery(View view);

		void onClickFromCamera(View view);
	}

	private Context context;
	private Activity _activity;

	private OnPicChooserListener listner;

	public PicChooserDialog(Context context) {
		super(context);
		init(context);
	}

	public PicChooserDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		this._activity = (Activity) context;
		Window w = this.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.setCanceledOnTouchOutside(true);

		View contentView = View.inflate(context, R.layout.dialog_pic_chooser, null);
		this.setContentView(contentView);
		contentView.findViewById(R.id.fromCamera).setOnClickListener(this);
		contentView.findViewById(R.id.fromGallery).setOnClickListener(this);
		contentView.findViewById(R.id.cancel).setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fromCamera:
			if (listner != null) {
				listner.onClickFromCamera(view);
			}
			dismiss();
			break;
		case R.id.fromGallery:
			if (listner != null) {
				listner.onClickFromGallery(view);
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
	}

	public PicChooserDialog setListner(OnPicChooserListener listner) {
		this.listner = listner;
		return this;
	}

}
