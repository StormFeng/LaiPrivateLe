package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;

import butterknife.Bind;

public class MeFavoriteSectionTpl extends BaseTpl<Object> {
    @Bind(R.id.section)
    TextView section_tv;

    public MeFavoriteSectionTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_section;
    }

    @Override
    public void setBean(Object bean, int position) {
        ObjectWrapper wrapper = (ObjectWrapper) bean;
        section_tv.setText("收藏于" + wrapper.getObject().toString());
    }

}
