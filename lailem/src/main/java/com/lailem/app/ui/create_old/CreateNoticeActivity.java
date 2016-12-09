package com.lailem.app.ui.create_old;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateNoticeActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.title)
    EditText title_et;
    @Bind(R.id.content)
    EditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("通知").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("发送", new OnClickListener() {

            @Override
            public void onClick(View v) {
                submit();
            }
        });

        topbar.getRight_tv().setEnabled(false);

        title_et.addTextChangedListener(this);
        content_et.addTextChangedListener(this);
        content_et.setMinHeight((int) (TDevice.getScreenHeight() / 2));

    }

    protected void submit() {
        String title = title_et.getText().toString().trim();
        String content = content_et.getText().toString().trim();

        if (content.length() < 10 || content.length() > 1024) {//10-1024
            AppContext.showToast("通知内容要求10-1024个字");
            return;
        }

        ApiClient.getApi().notice(this, ac.getLoginUid(), content, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), title);
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
            AppContext.showToast("发布成功");
            ac.setProperty(Const.USER_DYNAMIC_COUNT, (Integer.parseInt(ac.getProperty(Const.USER_DYNAMIC_COUNT)) + 1) + "");
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
        topbar.getRight_tv().setEnabled(title_et.length() > 0 && content_et.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
