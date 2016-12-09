package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lailem.app.R;

public class MaterialChooseDialog extends Dialog implements android.view.View.OnClickListener {
	private Context context;

	private static final int DEFAULT_THEME = R.style.bottom_dialog;
	private onMaterialChooseListener onMaterialChooseListener;

	public interface onMaterialChooseListener {
		void onClickFromGallery();

		void onClickFromCamera();

		void onClickFromMaterial();
	}

	public MaterialChooseDialog(Context context) {
		super(context, DEFAULT_THEME);
		init(context);
	}

	public MaterialChooseDialog(Context context, int theme) {
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

		View contentView = View.inflate(context, R.layout.dialog_material_chooser, null);
		this.setContentView(contentView);
		contentView.findViewById(R.id.fromCamera).setOnClickListener(this);
		contentView.findViewById(R.id.fromGallery).setOnClickListener(this);
		contentView.findViewById(R.id.fromMaterial).setOnClickListener(this);
		contentView.findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fromCamera:
			if (onMaterialChooseListener != null) {
				onMaterialChooseListener.onClickFromCamera();
			}
			dismiss();
			break;
		case R.id.fromGallery:
			if (onMaterialChooseListener != null) {
				onMaterialChooseListener.onClickFromGallery();
			}
			dismiss();
			break;
		case R.id.fromMaterial:
			if (onMaterialChooseListener != null) {
				onMaterialChooseListener.onClickFromMaterial();
			}
			dismiss();
			break;
		case R.id.cancel:
			dismiss();
			break;
		}
	}

	public void setOnMaterialChooseListener(onMaterialChooseListener onMaterialChooseListener) {
		this.onMaterialChooseListener = onMaterialChooseListener;
	}
}
