package com.lailem.app.ui.me.tpl.dynamic;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;

import butterknife.Bind;

public class MeDynamicSectionTpl extends BaseTpl<Object> {
    @Bind(R.id.section)
    TextView section_tv;

    public MeDynamicSectionTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_dynamic_section;
    }

    @Override
    public void setBean(Object bean, int position) {
        ObjectWrapper wrapper = (ObjectWrapper) bean;
        section_tv.setText("发表于" + wrapper.getObject().toString());
    }

}
