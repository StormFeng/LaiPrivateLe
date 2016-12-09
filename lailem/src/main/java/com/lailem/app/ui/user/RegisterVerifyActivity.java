package com.lailem.app.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ValidCodeButton;
import com.lailem.app.widget.ValidCodeButton.OnActionListener;
import com.lailem.app.widget.ValidCodeButton.OnTapStartListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterVerifyActivity extends BaseActivity implements TextWatcher {

    public static final String API_TAG_AUTHCODE = "authCode";

    @Bind(R.id.tip)
    TextView tip_tv;
    @Bind(R.id.validCode)
    EditText validCode_et;
    @Bind(R.id.submit)
    Button submit_btn;
    @Bind(R.id.getValidCode)
    ValidCodeButton getValidCode_btn;
    @Bind(R.id.phone_hidden)
    EditText phone_hidden_et;

    private String phone;
    private String pwd;
    private String validCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        phone = _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PHONE);
        pwd = _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_PASSWORD);
        initView();
    }

    private void initView() {
        phone_hidden_et.setText(phone);
        tip_tv.setText("已发送验证码至手机" + phone);
        getValidCode_btn.setNormalTipText("重发验证码");
        validCode_et.addTextChangedListener(this);

        getValidCode_btn.setOnTapStartListener(new OnTapStartListener() {

            @Override
            public void onTapStart() {
                OnActionListener onActionListener = new ValidCodeButton.SimpleOnActionListener() {
                    @Override
                    public void onFail(String msg) {
                        AppContext.showToast(msg);
                    }
                };
                // 短信验证
                getValidCode_btn.startByAuthCode(phone, Const.AUTH_TYPE_REGISTER, onActionListener);

            }
        });
        getValidCode_btn.startCounting();

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.submit)
    public void submit() {
        validCode = validCode_et.getText().toString().trim();
        // 短信验证
        ApiClient.getApi().verifyAuthCode(this, validCode, phone, null);

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
            UIHelper.showRegisterInfo(this, phone, pwd, validCode, _Intent.getStringExtra(LoginActivity.BUNDLE_KEY_TRANSID));
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
        submit_btn.setEnabled(validCode_et.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
