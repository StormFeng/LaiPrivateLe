package com.lailem.app.ui.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class ModifyActiveIdActivity extends BaseActivity implements TextWatcher {
    public static final String BUNDLE_KEY_ACTIVEID = "activeId";
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.right_tv)
    TextView submit_tv;
    @Bind(R.id.input)
    EditText input_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_activeid);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("活动号").setLeftText("取消", UIHelper.finish(this)).setRightText("保存");
        if (_Bundle != null) {
            input_et.setText(_Bundle.getString(BUNDLE_KEY_ACTIVEID));
            input_et.setSelection(input_et.length());
        }
        submit_tv.setEnabled(input_et.length() > 0);
        input_et.addTextChangedListener(this);
    }

    @OnClick(R.id.right_tv)
    public void submit() {
        if (input_et.length() < 6) {
            AppContext.showToast("活动号至少6位");
            return;
        }
        ApiClient.getApi().setUsername(this, ac.getLoginUid(), input_et.getText().toString().trim());
    }

    @OnClick(R.id.clear)
    public void clear() {
        input_et.setText("");
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
            ac.setProperty(Const.USER_USERNAME, input_et.getText().toString().trim());
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
        submit_tv.setEnabled(input_et.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
