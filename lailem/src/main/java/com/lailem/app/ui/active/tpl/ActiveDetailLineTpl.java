package com.lailem.app.ui.active.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

public class ActiveDetailLineTpl extends BaseTpl<Object> {

	public ActiveDetailLineTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active_detail_line;
	}

	@Override
	public void setBean(Object bean, int position) {

	}

}
