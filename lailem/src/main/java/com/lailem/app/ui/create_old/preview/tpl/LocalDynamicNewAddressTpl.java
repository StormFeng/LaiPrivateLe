package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/21.
 */
public class LocalDynamicNewAddressTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.address)
    TextView address_tv;
    private AddressModel bean;

    public LocalDynamicNewAddressTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_new_address;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (AddressModel) wrapper.getObject();
        address_tv.setText(bean.getContent().getAddress());
    }

    @OnClick(R.id.addressArea)
    public void clickAddressArea() {
        UIHelper.showActiveLoc(getContext(), bean.getContent().getLat(), bean.getContent().getLon(), address_tv.getText().toString().trim(), ActiveLocActivity.TYPE_OTHER);
    }
}
