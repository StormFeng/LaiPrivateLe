package com.lailem.app.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;

public class MainGroupSectionTpl extends BaseTpl<Object> {
    @Bind(R.id.label)
    TextView label_tv;
    @Bind(R.id.option)
    TextView option_tv;

    public MainGroupSectionTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_main_group_section;
    }

    @Override
    public void setBean(Object bean, int position) {

    }

    @OnClick(R.id.option)
    public void clickOption() {
        UIHelper.showGroupTags(_activity);
    }
}
