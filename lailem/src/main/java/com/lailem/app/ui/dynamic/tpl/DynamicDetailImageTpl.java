package com.lailem.app.ui.dynamic.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicImageView;

import butterknife.Bind;

public class DynamicDetailImageTpl extends BaseTpl<PublishInfo> {

	@Bind(R.id.dynamicImage)
	DynamicImageView dynamicImageView;

	public DynamicDetailImageTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_dynamic_detail_image;
	}

	@Override
	public void setBean(PublishInfo bean, int position) {
		dynamicImageView.render(bean.getPics(),1);
	}

}
