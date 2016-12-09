package com.lailem.app.ui.group.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;

import butterknife.Bind;

public class GroupHomeCreateTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.image)
    ImageView image_iv;

    public GroupHomeCreateTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_create;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        DynamicBean bean = (DynamicBean) wrapper.getObject();
        content_tv.setText(bean.getCreateActivityInfo().getName());
        if (Func.checkImageTag(bean.getCreateActivityInfo().getSquareSPicName(), image_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getCreateActivityInfo().getSquareSPicName())).into(image_iv);
        }
    }

}
