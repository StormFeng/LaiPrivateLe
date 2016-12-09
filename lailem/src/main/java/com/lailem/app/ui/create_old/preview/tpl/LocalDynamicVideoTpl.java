package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.dynamic.DynamicVideoView;

import butterknife.Bind;

public class LocalDynamicVideoTpl extends BaseTpl<ObjectWrapper> {
	@Bind(R.id.dynamicVideo)
	DynamicVideoView dynamicVideoView;
    private VideoModel bean;

    public LocalDynamicVideoTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_dynamic_preview_video;
	}

	@Override
	public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (VideoModel) wrapper.getObject();
		dynamicVideoView.render(StringUtils.getUri(bean.getContent().getPreviewPic()), bean.getContent().getFilename());
	}

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showPlayVideo(_activity, bean.getContent().getFilename(), bean.getContent().getPreviewPic());
    }

}
