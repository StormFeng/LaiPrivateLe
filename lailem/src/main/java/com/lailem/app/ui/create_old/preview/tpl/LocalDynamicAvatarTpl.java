package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class LocalDynamicAvatarTpl extends BaseTpl<Object> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.date)
    TextView date_tv;

    public LocalDynamicAvatarTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dynamic_preview_avatar;
    }

    @Override
    public void setBean(Object bean, int position) {
        if (Func.checkImageTag(ac.getProperty(Const.USER_SHEAD), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(ac.getProperty(Const.USER_SHEAD))).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(ac.getProperty(Const.USER_NICKNAME));
        date_tv.setText(Func.formatTime(Func.getNow()));
    }

}
