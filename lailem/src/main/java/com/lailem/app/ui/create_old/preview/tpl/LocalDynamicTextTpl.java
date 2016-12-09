package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.TextModel;
import com.lailem.app.widget.dynamic.DynamicTextView;

import butterknife.Bind;

public class LocalDynamicTextTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicText)
    DynamicTextView dynamicTextView;
    private TextModel bean;

    public LocalDynamicTextTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_text;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (TextModel) wrapper.getObject();
        dynamicTextView.render(bean.getContent());
    }

}
