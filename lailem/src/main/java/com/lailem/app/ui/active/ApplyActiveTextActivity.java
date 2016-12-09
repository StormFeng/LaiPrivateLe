package com.lailem.app.ui.active;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyActiveTextActivity extends BaseActivity implements TextWatcher {

    public static final String BUNDLE_KEY_JOIN_NEED_CONTACT = "joinNeedContact";
    public static final String BUNDLE_KEY_JOIN_NEED_VERIFY = "joinNeedVerify";

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.contactArea)
    View contactArea;
    @Bind(R.id.name)
    EditText name_tv;
    @Bind(R.id.phone)
    EditText phone_tv;
    @Bind(R.id.contentArea)
    View contentArea;
    @Bind(R.id.content)
    TextView content_et;
    @Bind(R.id.submit)
    TextView submit_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_active_text);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        topbar.setTitle("申请加入活动").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        content_et.addTextChangedListener(this);
        name_tv.addTextChangedListener(this);
        phone_tv.addTextChangedListener(this);

        name_tv.setText(ac.getProperty(Const.USER_NICKNAME));
        phone_tv.setText(ac.getProperty(Const.USER_PHONE));
        name_tv.setSelection(name_tv.length());
        phone_tv.setSelection(phone_tv.length());

        if (Const.ACTIVE_JOIN_NEED_CONTACT.equals(_Bundle.getString(BUNDLE_KEY_JOIN_NEED_CONTACT))) {
            contactArea.setVisibility(View.VISIBLE);
        } else {
            contactArea.setVisibility(View.GONE);
        }

        if (Const.ACTIVE_JOIN_NEED_VERIFY.equals(_Bundle.getString(BUNDLE_KEY_JOIN_NEED_VERIFY))) {
            contentArea.setVisibility(View.VISIBLE);
        } else {
            contentArea.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.submit)
    public void clickSubmit() {
        String content = null;
        String name = null;
        String phone = null;

        if (Const.ACTIVE_JOIN_NEED_CONTACT.equals(_Bundle.getString(BUNDLE_KEY_JOIN_NEED_CONTACT))) {
            String n = name_tv.getText().toString().trim();
            String p = phone_tv.getText().toString().trim();
            if (!TextUtils.isEmpty(n) && !TextUtils.isEmpty(p)) {
                if (!Func.isPhoneNumber(p)) {
                    AppContext.showToast("联系方式格式不正确");
                    return;
                }
                name = n;
                phone = p;
            } else if (TextUtils.isEmpty(n) && !TextUtils.isEmpty(p)) {
                AppContext.showToast("请填写联系人");
                return;
            } else if (!TextUtils.isEmpty(n) && TextUtils.isEmpty(p)) {
                AppContext.showToast("请填写手机号");
                return;
            } else {
                AppContext.showToast("请填写联系人和手机号");
                return;
            }
        }

        if (Const.ACTIVE_JOIN_NEED_VERIFY.equals(_Bundle.getString(BUNDLE_KEY_JOIN_NEED_VERIFY))) {
            String c = content_et.getText().toString().trim();
            if (!TextUtils.isEmpty(c)) {
                content = c;
            } else {
                AppContext.showToast("请填写您的加入活动的理由哟！");
                return;
            }
        }

        ApiClient.getApi().apply(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), content, null, null, name, phone);
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
            AppContext.showToast("申请提交成功");
            finish();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean isNeedContact = Const.ACTIVE_JOIN_NEED_CONTACT.equals(_Bundle.getString(BUNDLE_KEY_JOIN_NEED_CONTACT));
        boolean isNeedVerify = Const.ACTIVE_JOIN_NEED_VERIFY.equals(_Bundle.getString(BUNDLE_KEY_JOIN_NEED_VERIFY));
        if (isNeedContact && isNeedVerify) {
            submit_tv.setEnabled(name_tv.length() > 0 && phone_tv.length() > 0 && content_et.length() > 0);
        } else if (!isNeedContact && isNeedVerify) {
            submit_tv.setEnabled(content_et.length() > 0);
        } else if (isNeedContact && !isNeedVerify) {
            submit_tv.setEnabled(name_tv.length() > 0 && phone_tv.length() > 0);
        } else {
            submit_tv.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
