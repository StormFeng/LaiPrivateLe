package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

public class MeFavoriteLineTpl extends BaseTpl<Object> {

    public MeFavoriteLineTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_line;
    }

    @Override
    public void setBean(Object bean, int position) {

    }

}
