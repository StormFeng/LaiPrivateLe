package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.widget.dynamic.DynamicImageView;

import butterknife.Bind;

public class MeFavoriteImageTpl extends BaseTpl<PublishInfo> {
    @Bind(R.id.dynamicImage)
    DynamicImageView dynamicImageView;

    public MeFavoriteImageTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_image;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {
        dynamicImageView.render(bean.getPics(), 1);
    }

}
