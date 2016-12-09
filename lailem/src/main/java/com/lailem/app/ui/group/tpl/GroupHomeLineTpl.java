package com.lailem.app.ui.group.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

public class GroupHomeLineTpl extends BaseTpl<Object> {

	public GroupHomeLineTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_group_home_line;
	}

	@Override
	public void setBean(Object bean, int position) {

	}

}
