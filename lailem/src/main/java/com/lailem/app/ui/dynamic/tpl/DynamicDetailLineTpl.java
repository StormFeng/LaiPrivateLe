package com.lailem.app.ui.dynamic.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

public class DynamicDetailLineTpl extends BaseTpl<Object> {

	public DynamicDetailLineTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_dynamic_detail_line;
	}

	@Override
	public void setBean(Object bean, int position) {

	}

}
