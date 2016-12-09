package com.lailem.app.ui.active.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicVideoView;

import butterknife.Bind;

public class ActiveDetailVideoTpl extends BaseTpl<PublishInfo> {
	@Bind(R.id.dynamicVideo)
	DynamicVideoView dynamicVideoView;

	public ActiveDetailVideoTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active_detail_video;
	}

	@Override
	public void setBean(PublishInfo bean, int position) {
		dynamicVideoView.render(ApiClient.getFileUrl(bean.getPreviewPic()), ApiClient.getFileUrl(bean.getFilename()));
	}

}
