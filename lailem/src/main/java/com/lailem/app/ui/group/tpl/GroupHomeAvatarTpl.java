package com.lailem.app.ui.group.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublisherInfo;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GroupHomeAvatarTpl extends BaseTpl<PublisherInfo> {

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.dynaType)
    TextView dynaType_tv;
    @Bind(R.id.date)
    TextView date_tv;
    private PublisherInfo bean;

    public GroupHomeAvatarTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_avatar;
    }

    @Override
    public void setBean(PublisherInfo bean, int position) {
        this.bean = bean;
        if (Func.checkImageTag(bean.getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }
        name_tv.setText(bean.getRemark());
        date_tv.setText(Func.formatTime(bean.getCreateTime()));
    }

    @OnClick({R.id.avatar, R.id.name})
    public void clickAvatarOrName() {
        UIHelper.showMemberInfoAlone(_activity, bean.getId());
    }

}
