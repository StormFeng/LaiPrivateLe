package com.lailem.app.ui.group.tpl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.AddDynamicChoiceDialog;
import com.lailem.app.widget.GroupQrCodeDialog;
import com.lailem.app.widget.InviteDialog;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by XuYang on 15/12/5.
 */
public class GroupHomeForMemberTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.groupName)
    TextView groupName_tv;
    @Bind(R.id.groupType)
    TextView groupType_tv;
    @Bind(R.id.tagOne)
    TextView tagOne_tv;
    @Bind(R.id.tagTwo)
    TextView tagTwo_tv;
    @Bind(R.id.tagThree)
    TextView tagThree_tv;
    @Bind(R.id.intro)
    TextView intro_tv;

    AddDynamicChoiceDialog addDynamicChoiceDialog;


    private GroupInfoBean.GroupInfo bean;
    private GroupQrCodeDialog qrCodeDialog;

    public GroupHomeForMemberTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_for_member;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (GroupInfoBean.GroupInfo) wrapper.getObject();
        String bannerUrl = ApiClient.getFileUrl(bean.getbPicName());
        if (Func.checkImageTag(bannerUrl, avatar_iv)) {
            Glide.with(_activity).load(bannerUrl).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
        }
        groupName_tv.setText(bean.getName());
        groupType_tv.setText(bean.getTypeName());
        intro_tv.setText(bean.getIntro());
        String tagNames = bean.getTagNames();
        if (!TextUtils.isEmpty(tagNames)) {
            String[] tagNameArr = tagNames.split("、");
            if (tagNameArr.length > 0) {
                tagOne_tv.setText(tagNameArr[0]);
            } else {
                tagOne_tv.setVisibility(GONE);
            }
            if (tagNameArr.length > 1) {
                tagTwo_tv.setText(tagNameArr[1]);
            } else {
                tagTwo_tv.setVisibility(GONE);
            }
            if (tagNameArr.length > 2) {
                tagThree_tv.setText(tagNameArr[2]);
            } else {
                tagThree_tv.setVisibility(GONE);
            }
        } else {
            tagOne_tv.setVisibility(GONE);
            tagTwo_tv.setVisibility(GONE);
            tagThree_tv.setVisibility(GONE);
        }
    }

    @OnClick({R.id.avatar})
    public void clickAvatar() {
        UIHelper.showMemberInfoAlone(_activity, bean.getCreatorInfo().getId());
    }

    @OnClick(R.id.groupInfo)
    public void clickGroupInfo() {
        ac.putShareObject(Const.SHAREOBJ_KEY_GROUP_INFO, bean);
        UIHelper.showGroupInfo(_activity, bean.getId());
    }

    @OnClick(R.id.showNotice)
    public void showNotice() {
        UIHelper.showGroupNotification(_activity, "群组通知", bean.getId(), Const.ROLE_TYPE_CREATOR.equals(bean.getRoleType()));
    }

    @OnClick(R.id.showActives)
    public void showActives() {
        UIHelper.showGroupActiveList(_activity, bean.getId(), Const.ROLE_TYPE_CREATOR.equals(bean.getRoleType()));
    }

    @OnClick(R.id.invite)
    public void invite() {
        InviteDialog dialog = new InviteDialog(_activity, bean.getId(), bean.getName(), bean.getbPicName(), bean.getStartTime(), bean.getAddress(), bean.getIntro(), Const.INVITE_TYPE_ACTIVE);
        dialog.show();
    }

    @OnClick(R.id.showMembers)
    public void showMembers() {
        UIHelper.showGroupMemberList(_activity, bean.getName(), bean.getId(), bean.getRoleType());
    }

    @OnClick(R.id.showDataBank)
    public void showDataBank() {
        UIHelper.showDataBank(_activity, bean.getId());
    }

    @OnClick(R.id.addDynamic)
    public void addDynamic() {
        if (addDynamicChoiceDialog == null) {
            addDynamicChoiceDialog = new AddDynamicChoiceDialog(_activity);
            addDynamicChoiceDialog.setGroupData(_Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
        }
        addDynamicChoiceDialog.show();
    }

    @OnClick(R.id.addActive)
    public void addActive() {
        UIHelper.showCreateActiveType(_activity, bean.getId());
    }
}
