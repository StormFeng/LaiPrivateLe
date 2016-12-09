package com.lailem.app.ui.group.tpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean;
import com.lailem.app.jsonbean.activegroup.Member;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.GroupQrCodeDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by XuYang on 15/12/5.
 */
public class GroupHomeForPublicTpl extends BaseTpl<ObjectWrapper> {
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

    @Bind(R.id.groupNum)
    TextView groupNum_tv;
    @Bind(R.id.address)
    TextView address_tv;

    @Bind(R.id.memberCount)
    TextView memberCount_tv;
    @Bind(R.id.femaleCount)
    TextView femaleCount_tv;
    @Bind(R.id.maleCount)
    TextView maleCount_tv;
    @Bind(R.id.memberLayout)
    LinearLayout memberLayout;

    private GroupInfoBean.GroupInfo bean;
    private GroupQrCodeDialog qrCodeDialog;

    public GroupHomeForPublicTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_for_public;
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

        groupNum_tv.setText(bean.getGroupNum());
        address_tv.setText(bean.getAddress());

        int maleCount = Integer.parseInt(bean.getManCount());
        int femaleCount = Integer.parseInt(bean.getWomenCount());
        int memberCount = Integer.parseInt(bean.getCurrCount());
        memberCount_tv.setText("(" + memberCount + ")");
        maleCount_tv.setText(maleCount + "");
        femaleCount_tv.setText(femaleCount + "");
        ArrayList<Member> list = bean.getMemberList();
        memberLayout.removeAllViews();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Member member = list.get(i);
                ImageView imageView = (ImageView) View.inflate(_activity, R.layout.view_member_item, null);
                if (Func.checkImageTag(member.getHeadSPicName(), imageView)) {
                    Glide.with(_activity).load(ApiClient.getFileUrl(member.getHeadSPicName())).placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).bitmapTransform(new CropCircleTransformation(_activity)).into(imageView);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) TDevice.dpToPixel(30f), (int) TDevice.dpToPixel(30f));
                params.rightMargin = (int) TDevice.dpToPixel(12f);
                memberLayout.addView(imageView, params);
                if ((TDevice.dpToPixel(30 * (i + 2)) + TDevice.dpToPixel(10 * (i + 1)) + TDevice.dpToPixel(110)) > (TDevice.getScreenWidth())) {
                    break;
                }
            }
        }
    }

    @OnClick({R.id.avatar})
    public void clickAvatar() {
        UIHelper.showMemberInfoAlone(_activity, bean.getCreatorInfo().getId());
    }

    @OnClick(R.id.groupInfo)
    public void clickGroupInfo() {

    }

    @OnClick(R.id.showQrCode)
    public void showQrCode() {
        if (qrCodeDialog == null) {
            qrCodeDialog = new GroupQrCodeDialog(_activity);
            qrCodeDialog.setGroupInfo(bean.getName(), bean.getGroupNum(), bean.getId(), bean.getAddress());
        }
        qrCodeDialog.show();
    }

    @OnClick(R.id.address_ll)
    public void showAddress() {
        UIHelper.showActiveLoc(_activity, bean.getLat(), bean.getLon(), bean.getAddress(), ActiveLocActivity.TYPE_GROUP);
    }

    @OnClick(R.id.members_ll)
    public void showMembers() {
        UIHelper.showGroupMemberList(_activity, bean.getName(), bean.getId(), bean.getRoleType());
    }

    @OnClick(R.id.showActives)
    public void showActives() {
        UIHelper.showGroupActiveList(_activity, bean.getId(), Const.ROLE_TYPE_CREATOR.equals(bean.getRoleType()));
    }


}
