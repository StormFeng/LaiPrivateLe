package com.lailem.app.ui.active.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicTextView;

import butterknife.Bind;

public class ActiveDetailTextTpl extends BaseTpl<PublishInfo> {
	@Bind(R.id.dynamicText)
	DynamicTextView dynamicTextView;

	public ActiveDetailTextTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active_detail_text;
	}

	@Override
	public void setBean(PublishInfo bean, int position) {
		dynamicTextView.render(bean.getContent());
	}

}
