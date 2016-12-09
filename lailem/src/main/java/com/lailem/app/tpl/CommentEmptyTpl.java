package com.lailem.app.tpl;

import android.content.Context;

import com.lailem.app.R;

public class CommentEmptyTpl extends BaseTpl<Object> {

	public CommentEmptyTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_comment_empty;
	}

	@Override
	public void setBean(Object bean, int position) {

	}

}
