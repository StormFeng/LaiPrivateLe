package com.lailem.app.ui.create_old;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.GroupIdBean;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGroupActivity extends BaseActivity {

    public static final String PERMISSION_PRIVATE = "1";// 私有
    public static final String PERMISSION_PUBLIC = "2";// 公开

    @Bind(R.id.headTitle)
    TextView headTitle_tv;
    @Bind(R.id.tip)
    TextView tip_tv;
    @Bind(R.id.permissionRg)
    RadioGroup permissionRg;
    @Bind(R.id.permissionTip)
    TextView permissionTip_tv;
    @Bind(R.id.address)
    TextView address_tv;
    @Bind(R.id.brief)
    EditText brief_et;
    @Bind(R.id.publicType)
    RadioButton publicType_rb;
    @Bind(R.id.arrow_line_left_part)
    ImageView arrowLineLeftPart;
    private String permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTranslucentStatus(true, R.color.window_bg);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        headTitle_tv.setText(_Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME));
        permissionRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.privateType) {
                    permissionTip_tv.setText("群组将不公开，只能通过群组成员邀请才能加入该群组。");
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) arrowLineLeftPart.getLayoutParams();
                    params.width = (int) (publicType_rb.getWidth() + TDevice.dpToPixel(13f));
                    arrowLineLeftPart.setLayoutParams(params);
                } else if (checkedId == R.id.publicType) {
                    permissionTip_tv.setText("公开在附近群组中，附近的人能看到该群组信息及其动态。");
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) arrowLineLeftPart.getLayoutParams();
                    params.width = (int) TDevice.dpToPixel(13);
                    arrowLineLeftPart.setLayoutParams(params);
                }
            }
        });
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.address_ll)
    public void clickAddress() {
        UIHelper.showSelectLoc(this);
    }

    @OnClick(R.id.submit)
    public void submit() {
        String groupType = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_GROUP_TYPE);
        String typeId = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID);
        String typeName = _Bundle.getString(CreateTypeActivity.BUNDLE_KEY_TYPE_NAME);
        File pic = null;
        String picPath = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_LOCAL);
        if (!TextUtils.isEmpty(picPath)) {
            pic = new File(picPath);
        }
        String picMaterialId = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_MATERIALID);
        if (TextUtils.isEmpty(picMaterialId)) {
            picMaterialId = null;
        }
        String name = _Bundle.getString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME);
        //公开或私有
        permission = permissionRg.getCheckedRadioButtonId() == R.id.privateType ? PERMISSION_PRIVATE : PERMISSION_PUBLIC;

        if (address_tv.getTag() == null || !((AddressInfo) address_tv.getTag()).isValid()) {
            AppContext.showToast("请选择群地点");
            return;
        }

        //群资料
        String intro = brief_et.getText().toString().trim();

        if (intro.length() < 10 || intro.length() > 256) {//10-256
            AppContext.showToast("群资料要求10-256个字");
            return;
        }
        String contact = null;
        String startTime = null;
        String endTime = null;
        String parentId = null;

        AddressInfo addressInfo = (AddressInfo) address_tv.getTag();
        if (ac.isLogin(this, Func.getMethodName(Thread.currentThread().getStackTrace()))) {
            ApiClient.getApi().createGroup(this, ac.getLoginUid(), addressInfo.getAddress(), contact, endTime, groupType, intro, addressInfo.getLat(), addressInfo.getLng(), name, parentId, permission, pic, picMaterialId, startTime, typeId, typeName, null, null);
        } else {
            UIHelper.showLogin(this, false);
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            AppContext.showToast("创建成功");
            GroupIdBean bean = (GroupIdBean) res;
            GroupIdBean.GroupInfo groupInfo = bean.getGroupInfo();

            GroupUtils.joinGroup(this, groupInfo.getId(), groupInfo.getName(), brief_et.getText().toString().trim(), groupInfo.getSquareSPicName(), permission);
            UIHelper.showInvite(this, groupInfo.getName(), groupInfo.getSquareSPicName(), Const.INVITE_TYPE_GROUP, groupInfo.getId());
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SelectLocActivity.REQUEST_SELECT_LOC:
                    AddressInfo addressInfo = (AddressInfo) data.getExtras().getSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO);
                    if (addressInfo.isValid()) {
                        address_tv.setText(addressInfo.getAddress());
                        address_tv.setTag(addressInfo);
                    } else {
                        AppContext.showToast("您选择位置信息无效");
                    }
                    break;
                case CreateTypeActivity.REQUEST_NEXT:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

}
