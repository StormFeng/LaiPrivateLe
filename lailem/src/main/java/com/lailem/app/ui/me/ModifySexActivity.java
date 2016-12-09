package com.lailem.app.ui.me;

import android.os.Bundle;
import android.widget.RadioGroup;

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

public class ModifySexActivity extends BaseActivity {
    public static final String BUNDLE_KEY_SEX = "sex";
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.sexRg)
    RadioGroup sexRg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_sex);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("性别").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        if (_Bundle != null && "男".equals(_Bundle.getString(BUNDLE_KEY_SEX))) {
            sexRg.check(R.id.male);
        } else {
            sexRg.check(R.id.female);
        }

    }

    @OnClick({R.id.male, R.id.female})
    public void onClick() {
        if (sexRg.getCheckedRadioButtonId() == R.id.male) {
            ApiClient.getApi().changePersonInfo(this, ac.getLoginUid(), null, null, null, null, null, null, null, Const.MALE);
        } else {
            ApiClient.getApi().changePersonInfo(this, ac.getLoginUid(), null, null, null, null, null, null, null, Const.FEMALE);
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
            if (sexRg.getCheckedRadioButtonId() == R.id.male) {
                ac.setProperty(Const.USER_SEX, Const.MALE);
            } else if (sexRg.getCheckedRadioButtonId() == R.id.female) {
                ac.setProperty(Const.USER_SEX, Const.FEMALE);
            }
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
    }

}
