package com.lailem.app.ui.me.tpl.dynamic;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

public class MeDynamicLineTpl extends BaseTpl<Object> {

    public MeDynamicLineTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_dynamic_line;
    }

    @Override
    public void setBean(Object bean, int position) {

    }

}
