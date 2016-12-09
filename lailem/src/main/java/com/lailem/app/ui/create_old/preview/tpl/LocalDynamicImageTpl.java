package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.ImageModel;
import com.lailem.app.widget.dynamic.DynamicImageView;

import butterknife.Bind;

public class LocalDynamicImageTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicImage)
    DynamicImageView dynamicImageView;
    private ImageModel bean;

    public LocalDynamicImageTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_image;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (ImageModel) wrapper.getObject();
        dynamicImageView.render(bean.getContent().getPics(), 1);
    }

}
