package com.lailem.app.tpl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean.GroupMember;
import com.lailem.app.ui.active.ActiveMemberListActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActiveMemberTpl extends BaseTpl<GroupMember> {

    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.sexAgeArea)
    View sexAgeArea;
    @Bind(R.id.sex)
    ImageView sex_iv;
    @Bind(R.id.age)
    TextView age_tv;
    @Bind(R.id.distanceAndDate)
    TextView distanceAndDate_tv;
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.call)
    ImageView call_iv;

    private GroupMember bean;

    public ActiveMemberTpl(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        super.initView();
        name_tv.setMaxWidth((int) (TDevice.getScreenWidth() / 1.8f));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_member;
    }

    @Override
    public void setBean(GroupMember bean, int position) {
        this.bean = bean;
        if (Func.checkImageTag(bean.getHeadSPicName(), avatar_iv)) {
            Glide.with(_activity).load(ApiClient.getFileUrl(bean.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(avatar_iv);
        }

        if (!TextUtils.isEmpty(bean.getcName())) {
            if (bean.getcName().equals(bean.getRemark())) {
                name_tv.setText(bean.getRemark());
            } else {
                name_tv.setText(bean.getRemark() + "(" + bean.getcName() + ")");
            }
        } else {
            name_tv.setText(bean.getRemark());
        }


        String sex = bean.getSex();
        if (Const.MALE.equals(sex)) {
            sex_iv.setImageResource(R.drawable.ic_male);
            sexAgeArea.setBackgroundResource(R.drawable.bg_male);
        } else if (Const.FEMALE.equals(sex)) {
            sex_iv.setImageResource(R.drawable.ic_female);
            sexAgeArea.setBackgroundResource(R.drawable.bg_female);
        }
        age_tv.setText(bean.getAge());
        distanceAndDate_tv.setText("");
        if (TextUtils.isEmpty(bean.getPersonalizedSignature())) {
            content_tv.setText(Const.EMPTY_PERSONAL_SIGN);
        } else {
            content_tv.setText(bean.getPersonalizedSignature());
        }

        if (Const.ROLE_TYPE_CREATOR.equals(_Bundle.getString(ActiveMemberListActivity.BUNDLE_KEY_ROLETYPE))
                && Const.ACTIVE_JOIN_NEED_CONTACT.equals(_Bundle.getString(ActiveMemberListActivity.BUNDLE_KEY_JOIN_NEED_CONTACT))) {
            if (TextUtils.isEmpty(bean.getcPhone())) {
                call_iv.setVisibility(GONE);
            } else {
                call_iv.setVisibility(VISIBLE);
            }
        } else {
            call_iv.setVisibility(GONE);
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        UIHelper.showMemberInfo(_activity, _Bundle.getString(Const.BUNDLE_KEY_GROUP_NAME), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), bean.getMemberId(), bean.getRoleType(), _Bundle.getString(ActiveMemberListActivity.BUNDLE_KEY_ROLETYPE));
    }

    @OnClick(R.id.call)
    public void clickCall() {
        Uri uri = Uri.parse("tel:" + bean.getcPhone());
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        _activity.startActivity(intent);
    }

}
