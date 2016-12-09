package com.lailem.app.ui.me.tpl.dynamic;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicImageView;

import butterknife.Bind;

public class MeDynamicImageTpl extends BaseTpl<PublishInfo> {
    @Bind(R.id.dynamicImage)
    DynamicImageView dynamicImageView;

    public MeDynamicImageTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_dynamic_image;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {
        dynamicImageView.render(bean.getPics(), 1);
    }

}
