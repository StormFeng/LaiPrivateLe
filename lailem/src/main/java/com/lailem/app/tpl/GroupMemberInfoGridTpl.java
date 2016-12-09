package com.lailem.app.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean.GroupMember;
import com.lailem.app.utils.Func;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GroupMemberInfoGridTpl extends BaseTpl<Object> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    private int position;
    private Object bean;

    public GroupMemberInfoGridTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_member_in_grid;
    }

    @Override
    public void setBean(Object bean, int position) {
        this.bean = bean;
        this.position = position;
        if (bean instanceof GroupMember) {
            GroupMember member = (GroupMember) bean;
            if (Func.checkImageTag(member.getHeadSPicName(), avatar_iv)) {
                Glide.with(_activity).load(ApiClient.getFileUrl(member.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
            }
            name_tv.setText(Func.formatNickName(_activity, member.getMemberId(), member.getNickname()));
        } else if (bean instanceof ObjectWrapper) {
            avatar_iv.setImageResource(R.drawable.ic_addblack);
            name_tv.setText("");
        }
    }

}
