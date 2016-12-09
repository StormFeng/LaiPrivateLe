package com.lailem.app.ui.group.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.tpl.BaseTpl;

/**
 * Created by XuYang on 15/12/22.
 */
public class GroupHomeGapTpl extends BaseTpl<Object> {
    public GroupHomeGapTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_gap;
    }

    @Override
    public void setBean(Object bean, int position) {

    }
}
