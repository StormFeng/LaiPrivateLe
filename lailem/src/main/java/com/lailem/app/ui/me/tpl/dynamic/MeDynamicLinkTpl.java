package com.lailem.app.ui.me.tpl.dynamic;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.tpl.BaseTpl;

public class MeDynamicLinkTpl extends BaseTpl<PublishInfo> {

    public MeDynamicLinkTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_dynamic_link;
    }

    @Override
    public void setBean(PublishInfo bean, int position) {

    }

}
