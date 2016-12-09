package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.AddressModel;
import com.lailem.app.utils.TDevice;

import butterknife.Bind;

public class CreateAddressTpl extends BaseTpl<ObjectWrapper> {

    @Bind(R.id.address)
    TextView address_tv;

    private AddressModel bean;

    public CreateAddressTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_address;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        setItemFillParent(getChildAt(0));
        this.bean = (AddressModel) wrapper.getObject();
        address_tv.setText(bean.getContent().getAddress());
    }

    private void setItemFillParent(View item) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();
        params.width = (int) TDevice.getScreenWidth();
        setLayoutParams(params);
    }

}
