package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/21.
 */
public class MeFavoriteNewAddressTpl extends BaseTpl<DynamicBean.PublishInfo> {
    @Bind(R.id.address)
    TextView address_tv;
    private DynamicBean.PublishInfo bean;

    public MeFavoriteNewAddressTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_new_address;
    }

    @Override
    public void setBean(DynamicBean.PublishInfo bean, int position) {
        this.bean = bean;
        address_tv.setText(bean.getAddress());
    }

    @OnClick(R.id.addressArea)
    public void clickAddressArea() {
        UIHelper.showActiveLoc(getContext(), bean.getLat(), bean.getLon(), address_tv.getText().toString().trim(), ActiveLocActivity.TYPE_OTHER);
    }
}
