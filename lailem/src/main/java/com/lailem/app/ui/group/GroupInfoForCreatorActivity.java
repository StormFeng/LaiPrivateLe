package com.lailem.app.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean.GroupInfo;
import com.lailem.app.ui.common.VerifyWayForGroupActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.GroupQrCodeDialog;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.VerifyWayChooseDialog;
import com.lailem.app.widget.togglebutton.ToggleButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群资料编辑
 *
 * @author XuYang
 */
public class GroupInfoForCreatorActivity extends BaseActivity {
    public static final String API_TAG_SET_VERIFY_WAY = "setVerifyWay";
    public static final String API_TAG_SET_TAG = "setTag";
    public static final String API_TAG_CHANG_GROUP_INFO = "changGroupInfo";
    public static final String API_TAG_CHANG_PROPERTY = "changProperty";
    public static final int REQUEST_INFO_EDIT = 5000;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.name)
    TextView name_tv;
    @Bind(R.id.groupCode)
    TextView groupCode_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.tags)
    TextView tags_tv;
    @Bind(R.id.permission)
    TextView permission_tv;
    @Bind(R.id.intro)
    TextView intro_tv;
    @Bind(R.id.verifyType)
    TextView verifyType_tv;
    @Bind(R.id.createActivityFlay)
    ToggleButton createActivityFlayToggle;

    private VerifyWayChooseDialog verifyWayDialog;

    private GroupInfo groupInfo;
    private String verifyWay;
    private GroupQrCodeDialog qrCodeDialog;
    private AddressInfo addressInfo;
    private String createActivityFlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info_for_creator);
        ButterKnife.bind(this);
        this.groupInfo = (GroupInfo) ac.getShareObject(Const.SHAREOBJ_KEY_GROUP_INFO);
        initView();
    }

    private void initView() {
        topbar.setTitle("群组资料").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        name_tv.setText(this.groupInfo.getName());
        address_tv.setText(this.groupInfo.getAddress());
        intro_tv.setText(this.groupInfo.getIntro());

        tags_tv.setText(this.groupInfo.getTagNames());
        if (Const.PERMISSION_PUBLIC.equals(groupInfo.getPermission())) {
            permission_tv.setText("公有群");
        } else {
            permission_tv.setText("私有群");
        }
        if (Const.VERIFY_WAY_TEXT.equals(this.groupInfo.getVerifyWay())) {
            verifyType_tv.setText("文字验证");
        } else if (Const.VERIFY_WAY_VOICE.equals(this.groupInfo.getVerifyWay())) {
            verifyType_tv.setText("语音验证");
        } else if (Const.VERIFY_WAY_NONE.equals(this.groupInfo.getVerifyWay())) {
            verifyType_tv.setText("无需验证");
        }

        if (Const.CREATE_ACTIVITY_FLAY_ALLOW.equals(groupInfo.getCreateActivityFlay())) {
            createActivityFlayToggle.setToggleOn();
        } else {
            createActivityFlayToggle.setToggleOff();
        }

        createActivityFlayToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {
                createActivityFlay = "";
                if (on) {
                    createActivityFlay = Const.CREATE_ACTIVITY_FLAY_ALLOW;
                } else {
                    createActivityFlay = Const.CREATE_ACTIVITY_FLAY_DENY;
                }
                ApiClient.getApi().changProperty(_activity, ac.getLoginUid(), groupInfo.getId(), createActivityFlay);
            }
        });
    }

    @OnClick(R.id.name_ll)
    public void clickName() {
        UIHelper.showModifyGroupName(this, name_tv.getText().toString().trim());
    }

    @OnClick(R.id.qrCode_ll)
    public void clickQrCode() {
        if (qrCodeDialog == null) {
            qrCodeDialog = new GroupQrCodeDialog(_activity);
            qrCodeDialog.setGroupInfo(groupInfo.getName(), groupInfo.getGroupNum(), groupInfo.getId(), groupInfo.getAddress());
        }
        qrCodeDialog.show();
    }

    @OnClick(R.id.address_ll)
    public void clickAddress() {
        UIHelper.showSelectLoc(_activity);
    }

    @OnClick(R.id.tags_ll)
    public void clickTags() {
        ArrayList<String> tagNames = new ArrayList<String>();
        String[] tags = this.groupInfo.getTagNames().split("、");
        for (int i = 0; i < tags.length; i++) {
            tagNames.add(tags[i]);
        }
        UIHelper.showSelectGroupTags(this, tagNames);
    }

    @OnClick(R.id.intro_ll)
    public void clickIntro_ll() {
        UIHelper.showModifyGroupIntro(this, groupInfo.getIntro());
    }

    @OnClick(R.id.verifyType_ll)
    public void clickVerifyType() {
        UIHelper.showVerifyWayForGroup(this, groupInfo.getVerifyWay());
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        if (tag.equals(API_TAG_CHANG_GROUP_INFO)) {
//            showWaitDialog();
        }
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
//        hideWaitDialog();
        if (res.isOK()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Const.BUNDLE_KEY_GROUP_INFO_CHANGED, true);
            setResult(RESULT_OK, bundle);
            if (tag.equals(API_TAG_CHANG_GROUP_INFO)) {
                if (addressInfo != null) {
                    groupInfo.setAddress(addressInfo.getAddress());
                    groupInfo.setLat(addressInfo.getLat());
                    groupInfo.setLon(addressInfo.getLng());
                }
                groupInfo.setName(name_tv.getText().toString().trim());
                groupInfo.setIntro(intro_tv.getText().toString().trim());
            } else if (tag.equals(API_TAG_SET_TAG)) {
                groupInfo.setTagNames(tags_tv.getText().toString().trim());
            } else if (tag.equals(API_TAG_SET_VERIFY_WAY)) {
                groupInfo.setVerifyWay(verifyWay);
            } else if (tag.equals(API_TAG_CHANG_PROPERTY)) {
                groupInfo.setCreateActivityFlay(createActivityFlay);
            }
            GroupUtils.updateGroup(this, groupInfo.getId(), groupInfo.getName(), groupInfo.getIntro(), "", "");
        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        super.onApiFailure(t, errorNo, strMsg, tag);
//        hideWaitDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ModifyGroupNameActivity.REQUEST_MODIFY_NAME:
                    String name = data.getExtras().getString(ModifyGroupNameActivity.BUNDLE_KEY_NAME);
                    name_tv.setText(name);
                    ApiClient.getApi().changGroupInfo(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), null, name, null, null, null);
                    break;
                case SelectGroupTagsActivity.REQUEST_SELECT_TAGS:
                    String tagIds = data.getExtras().getString(SelectGroupTagsActivity.BUNDLE_KEY_TAG_IDS);
                    String tagNames = data.getExtras().getString(SelectGroupTagsActivity.BUNDLE_KEY_TAG_NAMES);
                    tags_tv.setText(tagNames);
                    ApiClient.getApi().setTag(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), tagIds);
                    break;
                case SelectLocActivity.REQUEST_SELECT_LOC:
                    addressInfo = (AddressInfo) data.getExtras().getSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO);
                    if (addressInfo.isValid()) {
                        address_tv.setText(addressInfo.getAddress());
                        ApiClient.getApi().changGroupInfo(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), null, null, addressInfo.getAddress(), addressInfo.getLat(), addressInfo.getLng());
                    } else {
                        AppContext.showToast("您选择位置信息无效");
                    }
                    break;
                case VerifyWayForGroupActivity.REQUEST_CODE:
                    verifyWay = data.getExtras().getString(VerifyWayForGroupActivity.BUNDLE_KEY_VERIFYWAY);
                    if (Const.VERIFY_WAY_TEXT.equals(verifyWay)) {
                        verifyType_tv.setText("文字验证");
                    } else if (Const.VERIFY_WAY_VOICE.equals(verifyWay)) {
                        verifyType_tv.setText("语音验证");
                    } else if (Const.VERIFY_WAY_NONE.equals(verifyWay)) {
                        verifyType_tv.setText("无需验证");
                    }
                    ApiClient.getApi().setVerifyWay(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), verifyWay);
                    break;
                case ModifyGroupIntroActivity.REQUEST_CODE:
                    String intro = data.getExtras().getString(ModifyGroupIntroActivity.BUNDLE_KEY_INTRO);
                    intro_tv.setText(intro);
                    ApiClient.getApi().changGroupInfo(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), intro, null, null, null, null);
                    break;
            }
        }
    }
}
