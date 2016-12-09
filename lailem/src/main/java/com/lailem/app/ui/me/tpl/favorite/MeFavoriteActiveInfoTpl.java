package com.lailem.app.ui.me.tpl.favorite;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.personal.CollectBean.ActiveInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.TDevice;

import butterknife.Bind;

public class MeFavoriteActiveInfoTpl extends BaseTpl<ActiveInfo> {
    @Bind(R.id.activeJoinCount)
    TextView activeJoinCount_tv;
    @Bind(R.id.activeImage)
    ImageView activeImage_iv;
    @Bind(R.id.activeName)
    TextView activeName_tv;
    @Bind(R.id.activeAddress)
    TextView activeAddress_tv;
    @Bind(R.id.activeDate)
    TextView activeDate_tv;

    public MeFavoriteActiveInfoTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_favorite_activeinfo;
    }

    @Override
    protected void initView() {
        super.initView();
        activeName_tv.setShadowLayer(TDevice.dpToPixel(4), 0, TDevice.dpToPixel(2), Color.parseColor("#7F000000"));
    }

    @Override
    public void setBean(ActiveInfo bean, int position) {
        Glide.with(_activity).load(ApiClient.getFileUrl(bean.getbPicName())).placeholder(R.drawable.empty).error(R.drawable.empty).centerCrop().into(activeImage_iv);
        activeName_tv.setText(bean.getName());
        activeAddress_tv.setText(bean.getAddress());
        activeDate_tv.setText(bean.getStartTime());
    }
}
