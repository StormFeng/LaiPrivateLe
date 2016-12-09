package com.lailem.app.ui.group;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyGroupTextActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.content)
    TextView content_et;
    @Bind(R.id.submit)
    TextView submit_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_group_text);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        topbar.setTitle("申请加入群组").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        content_et.addTextChangedListener(this);
    }

    @OnClick(R.id.submit)
    public void clickSubmit() {
        String content = content_et.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            AppContext.showToast("请填写加群理由");
            return;
        }

        ApiClient.getApi().apply(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), content, null, null,null,null);
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
        submit_tv.setEnabled(content_et.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
