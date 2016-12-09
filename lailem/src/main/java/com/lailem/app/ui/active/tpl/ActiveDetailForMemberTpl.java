package com.lailem.app.ui.active.tpl;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.AddDynamicChoiceDialog;
import com.lailem.app.widget.InviteDialog;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by XuYang on 15/12/5.
 */
public class ActiveDetailForMemberTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.avatar)
    ImageView avatar_iv;
    @Bind(R.id.activeName)
    TextView activeName_tv;
    @Bind(R.id.activeType)
    TextView activeType_tv;
    @Bind(R.id.nameDate)
    TextView nameDate_tv;

    AddDynamicChoiceDialog addDynamicChoiceDialog;


    private ActiveInfoBean.ActiveInfo bean;

    public ActiveDetailForMemberTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_active_detail_for_member;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (ActiveInfoBean.ActiveInfo) wrapper.getObject();
        String avatarUrl = ApiClient.getFileUrl(bean.getCreatorInfo().getHeadSPicName());
        if (Func.checkImageTag(avatarUrl, avatar_iv)) {
            Glide.with(_activity).load(avatarUrl).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(_activity), new RoundedCornersTransformation(_activity, (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(avatar_iv);
        }
        activeName_tv.setText(bean.getName());
        activeType_tv.setText(bean.getTypeName());
        nameDate_tv.setText(bean.getRemark() + " 创建于" + Func.formatTime3(bean.getCreateTime()));

    }

    @OnClick({R.id.avatar})
    public void clickAvatar() {
        UIHelper.showMemberInfoAlone(_activity, bean.getCreatorInfo().getId());
    }

    @OnClick(R.id.activeInfo)
    public void clickActiveInfo() {
        ac.putShareObject(Const.SHAREOBJ_KEY_ACTIVE_INFO, bean);
        UIHelper.showActiveInfoDetail(_activity, bean.getId());
    }

    @OnClick(R.id.showNotice)
    public void showNotice() {
        UIHelper.showGroupNotification(_activity, "活动通知", bean.getId(),Const.ROLE_TYPE_CREATOR.equals(bean.getRoleType()));
    }

    @OnClick(R.id.showSchedule)
    public void showSchedule() {
        UIHelper.showGroupScheduleList(_activity, bean.getId(),Const.ROLE_TYPE_CREATOR.equals(bean.getRoleType()));
    }

    @OnClick(R.id.invite)
    public void invite() {
        InviteDialog dialog = new InviteDialog(_activity, bean.getId(), bean.getName(), bean.getbPicName(), bean.getStartTime(), bean.getAddress(), bean.getIntro(), Const.INVITE_TYPE_ACTIVE);
        dialog.show();
    }

    @OnClick(R.id.showMember)
    public void showMember() {
        UIHelper.showActiveMemberList(_activity, bean.getName(), bean.getId(), bean.getRoleType(), bean.getJoinNeedContact());
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
}
