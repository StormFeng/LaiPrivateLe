package com.lailem.app.ui.active;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean;
import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.ui.common.VerifyWayForActiveActivity;
import com.lailem.app.ui.common.VerifyWayForGroupActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.togglebutton.ToggleButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XuYang on 16/1/13.
 */
public class ActiveSettingActivity extends BaseActivity {
    public static final String API_TAG_SET_VERIFY_WAY = "setVerifyWay";
    public static final String API_TAG_CHANG_ACTIVE_PROPERTY = "changActiveProperty";

    public static final int REQUEST_CODE = 6000;
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.applyFlay)
    ToggleButton applyFlayToggle;
    @Bind(R.id.verifyType)
    TextView verifyType_tv;
    @Bind(R.id.needContactToggle)
    ToggleButton needContactToggle;
    @Bind(R.id.contactName)
    EditText contactName_et;
    @Bind(R.id.contactPhone)
    EditText contactPhone_et;
    @Bind(R.id.contactArea)
    LinearLayout contactArea;
    private ActiveInfoBean.ActiveInfo activeInfo;
    private String verifyWay;
    private String applyFlay;
    private String joinNeedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_setting);
        ButterKnife.bind(this);
        activeInfo = (ActiveInfoBean.ActiveInfo) ac.getShareObject(Const.SHAREOBJ_KEY_ACTIVE_INFO);
        initView();
    }

    private void initView() {
        topbar.setTitle("设置").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        if (Const.APPLY_FLAY_START.equals(activeInfo.getApplyFlay())) {
            applyFlayToggle.setToggleOn();
        } else {
            applyFlayToggle.setToggleOff();
        }
        if (Const.VERIFY_WAY_TEXT.equals(this.activeInfo.getVerifyWay())) {
            verifyType_tv.setText("文字验证");
        } else if (Const.VERIFY_WAY_VOICE.equals(this.activeInfo.getVerifyWay())) {
            verifyType_tv.setText("语音验证");
        } else if (Const.VERIFY_WAY_NONE.equals(this.activeInfo.getVerifyWay())) {
            verifyType_tv.setText("无需验证");
        }
        if (Const.ACTIVE_JOIN_NEED_CONTACT.equals(this.activeInfo.getJoinNeedContact())) {
            needContactToggle.setToggleOn();
        } else {
            needContactToggle.setToggleOff();
        }

        ArrayList<Contact> contactList = activeInfo.getContact();

        if (contactList != null && contactList.size() > 0) {
            if (!TextUtils.isEmpty(contactList.get(0).getName())) {
                contactName_et.setText(contactList.get(0).getName());
                contactPhone_et.setText(contactList.get(0).getPhone());
            }
        }

        applyFlayToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {
                applyFlay = "";
                if (on) {
                    applyFlay = Const.APPLY_FLAY_START;
                } else {
                    applyFlay = Const.APPLY_FLAY_STOP;
                }
                ApiClient.getApi().changActiveProperty(_activity, ac.getLoginUid(), activeInfo.getId(), applyFlay, null, null, null);
            }
        });

        needContactToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(ToggleButton toggleButton, boolean on) {
                joinNeedContact = "";
                if (on) {
                    joinNeedContact = Const.ACTIVE_JOIN_NEED_CONTACT;
                } else {
                    joinNeedContact = Const.ACTIVE_JOIN_NOT_NEED_CONTACT;
                }
                ApiClient.getApi().changActiveProperty(_activity, ac.getLoginUid(), activeInfo.getId(), null, joinNeedContact, null, null);
            }
        });
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
//        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
//        hideWaitDialog();
        if (res.isOK()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Const.BUNDLE_KEY_ACTIVE_INFO_CHANGED, true);
            setResult(RESULT_OK, bundle);
            if (API_TAG_CHANG_ACTIVE_PROPERTY.equals(tag)) {
                if (!TextUtils.isEmpty(verifyWay)) {
                    activeInfo.setVerifyWay(verifyWay);
                }
                if (!TextUtils.isEmpty(joinNeedContact)) {
                    activeInfo.setJoinNeedContact(joinNeedContact);
                }
            } else if (tag.equals(API_TAG_SET_VERIFY_WAY)) {
                if (!TextUtils.isEmpty(verifyWay)) {
                    activeInfo.setVerifyWay(verifyWay);
                }
            }
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        super.onApiFailure(t, errorNo, strMsg, tag);
//        hideWaitDialog();
    }

    @OnClick(R.id.verifyType_ll)
    public void clickVerifyType() {
        UIHelper.showVerifyWayForActive(this, activeInfo.getVerifyWay());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case VerifyWayForActiveActivity.REQUEST_CODE:
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
            }
        }
    }
}
