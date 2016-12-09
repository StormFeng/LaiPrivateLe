package com.lailem.app.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.dynamic.VoterListBean.Voter;
import com.lailem.app.utils.Func;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class VoterTpl extends BaseTpl<Voter> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;

    public VoterTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_voter_or_zan;
    }

    @Override
    public void setBean(Voter bean, int position) {
        if (Func.checkImageTag(bean.getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getRemark());
    }

}
