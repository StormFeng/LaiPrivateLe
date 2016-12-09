package com.lailem.app.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Md5Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPwdActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.oldPwd)
    EditText oldPwd_et;
    @Bind(R.id.newPwd)
    EditText newPwd_et;
    @Bind(R.id.newPwdAgain)
    EditText newPwdAgain_et;
    @Bind(R.id.submit)
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        oldPwd_et.addTextChangedListener(this);
        newPwd_et.addTextChangedListener(this);
        newPwdAgain_et.addTextChangedListener(this);
    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @OnClick(R.id.submit)
    public void submit() {
        String oldPwd = oldPwd_et.getText().toString().trim();
        String newPwd = newPwd_et.getText().toString().trim();
        String newPwdAgain = newPwdAgain_et.getText().toString().trim();

        if (!newPwd.equals(newPwdAgain)) {
            AppContext.showToast("两次输入的密码不一致");
            return;
        }

        if (newPwdAgain.length() < 6) {
            AppContext.showToast("密码要求6-32位");
            return;
        }

        ApiClient.getApi().updatePassword(this, ac.getLoginUid(), Md5Utils.md5(oldPwd), Md5Utils.md5(newPwd));

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        submit_btn.setEnabled(oldPwd_et.length() > 0 && newPwd_et.length() > 0 && newPwdAgain_et.length() > 0);

    }

    @Override
    public void afterTextChanged(Editable s) {

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
            AppContext.showToast("修改成功");
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

}
