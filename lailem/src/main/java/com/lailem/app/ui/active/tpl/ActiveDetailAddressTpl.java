package com.lailem.app.ui.active.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicAddressView;

import butterknife.Bind;

public class ActiveDetailAddressTpl extends BaseTpl<PublishInfo> {
	@Bind(R.id.dynamicAddress)
	DynamicAddressView dynamicAddressView;

	public ActiveDetailAddressTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active_detail_address;
	}

	@Override
	public void setBean(PublishInfo bean, int position) {
		dynamicAddressView.render(bean.getAddress(),bean.getLon(),bean.getLat());
	}

}
