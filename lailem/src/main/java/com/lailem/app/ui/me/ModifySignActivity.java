package com.lailem.app.ui.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

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

public class ModifySignActivity extends BaseActivity implements TextWatcher {
    public static final String BUNDLE_KEY_SIGN = "sign";
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.input)
    EditText input_et;
    @Bind(R.id.count)
    TextView count_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_sign);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("个性签名").setLeftText("取消", UIHelper.finish(this)).setRightText("保存").getRight_tv().setEnabled(false);
        ;
        if (_Bundle != null) {
            input_et.setText(_Bundle.getString(BUNDLE_KEY_SIGN));
            input_et.setTag(_Bundle.getString(BUNDLE_KEY_SIGN));
            input_et.setSelection(input_et.length());
            count_tv.setText(input_et.length() + "/30");
        }
        input_et.addTextChangedListener(this);
    }

    @OnClick(R.id.right_tv)
    public void submit() {
        ApiClient.getApi().changePersonInfo(this, ac.getLoginUid(), null, null, null, null, null, input_et.getText().toString().trim(), null, null);

    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
        topbar.getRight_tv().setEnabled(false);
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            String sign = input_et.getText().toString().trim();
            ac.setProperty(Const.USER_SIGN, sign);
            setResult(RESULT_OK);
            finish();
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
        topbar.getRight_tv().setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        count_tv.setText(input_et.length() + "/30");
        topbar.getRight_tv().setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
