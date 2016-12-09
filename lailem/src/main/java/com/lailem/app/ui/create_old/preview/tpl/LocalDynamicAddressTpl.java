package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.widget.dynamic.DynamicAddressView;

import butterknife.Bind;

public class LocalDynamicAddressTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.dynamicAddress)
    DynamicAddressView dynamicAddressView;
    private AddressModel bean;

    public LocalDynamicAddressTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_address;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (AddressModel) wrapper.getObject();
        dynamicAddressView.render(bean.getContent().getAddress(), bean.getContent().getLon(), bean.getContent().getLat());
    }

}
