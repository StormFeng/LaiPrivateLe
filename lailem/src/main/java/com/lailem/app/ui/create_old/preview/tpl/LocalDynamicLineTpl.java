package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

public class LocalDynamicLineTpl extends BaseTpl<Object> {

	public LocalDynamicLineTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_dynamic_preview_line;
	}

	@Override
	public void setBean(Object bean, int position) {
		
	}

}
