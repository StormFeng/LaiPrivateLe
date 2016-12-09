package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;

public class MeFavoriteLinkTpl extends BaseTpl<PublishInfo> {

    public MeFavoriteLinkTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_link;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {

    }

}
