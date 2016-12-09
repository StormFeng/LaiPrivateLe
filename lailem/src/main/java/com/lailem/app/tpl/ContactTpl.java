package com.lailem.app.tpl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Contact;
import com.lailem.app.utils.Func;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ContactTpl extends BaseTpl<Contact> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.localName)
    TextView localName_tv;
    @Bind(R.id.nickName)
    TextView nickName_tv;

    public ContactTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_contact;
    }

    @Override
    public void setBean(Contact bean, int position) {
        if (!TextUtils.isEmpty(bean.getImageUri())) {
            if (Func.checkImageTag(bean.getImageUri(), avatar_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(bean.getImageUri())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
            }
        } else {
            avatar_iv.setImageResource(R.drawable.default_avatar);
        }
        localName_tv.setText(bean.getName());
        nickName_tv.setText(bean.getNickName());
    }
}
