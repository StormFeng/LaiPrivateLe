package com.lailem.app.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.Md5Utils;
import com.lailem.app.widget.ValidCodeButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/28.
 */
public class BindPhoneActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.close)
    ImageView close_iv;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.headTitle)
    TextView headTitle_tv;
    @Bind(R.id.input_vp)
    ViewFlipper input_vp;
    @Bind(R.id.phone)
    EditText phont_et;
    @Bind(R.id.getValidButton)
    ValidCodeButton getValidButton;
    @Bind(R.id.validCode)
    EditText validCode_et;
    @Bind(R.id.pwd)
    EditText pwd_et;
    @Bind(R.id.pwd2)
    EditText pwd2_et;
    @Bind(R.id.submit)
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        phont_et.addTextChangedListener(this);
        validCode_et.addTextChangedListener(this);
        pwd_et.addTextChangedListener(this);
        pwd2_et.addTextChangedListener(this);

        getValidButton.setOnTapStartListener(new ValidCodeButton.OnTapStartListener() {
            @Override
            public void onTapStart() {
                String phone = phont_et.getText().toString().trim();
                getValidButton.startByAuthCode(phone, Const.AUTH_TYPE_BIND_PHONE, new ValidCodeButton.OnActionListener() {
                    @Override
                    public void onStart() {
                        showWaitDialog();
                    }

                    @Override
                    public void onSuccess(boolean isNeedVerifyCode, String transId) {
                        hideWaitDialog();
                        AppContext.showToast("短信发送成功");
                    }

                    @Override
                    public void onFail(String msg) {
                        hideWaitDialog();
                        AppContext.showToast(msg);
                    }
                });
            }
        });
    }

    @OnClick(R.id.submit)
    public void submit() {
        final String phone = phont_et.getText().toString().trim();
        String validCode = validCode_et.getText().toString().trim();
        String pwd = pwd_et.getText().toString().trim();
        String pwd2 = pwd2_et.getText().toString().trim();

        int index = input_vp.getDisplayedChild();
        if (index == 0) {
            //验证验证码
            if (!Func.isMobileNO(phone)) {
                AppContext.showToast("请输入格式正确的手机号");
                return;
            }
            ApiClient.getApi().verifyAuthCode(new ApiCallbackAdapter() {
                @Override
                public void onApiStart(String tag) {
                    showWaitDialog();
                }

                @Override
                public void onApiSuccess(Result res, String tag) {
                    hideWaitDialog();
                    if (res.isOK()) {
                        showPassword();
                    } else {
                        ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                    }
                }

                @Override
                protected void onApiError(String tag) {
                    hideWaitDialog();
                }
            }, validCode, phone, null);
        } else {
            if (pwd.length() < 6) {
                AppContext.showToast("密码要求6-32位");
                return;
            }
            //重置密码
            if (!pwd.equals(pwd2)) {
                AppContext.showToast("两次输入的密码不一致");
            }

            ApiClient.getApi().bindPhone(new ApiCallbackAdapter() {
                @Override
                public void onApiStart(String tag) {
                    showWaitDialog();
                }

                @Override
                public void onApiSuccess(Result res, String tag) {
                    hideWaitDialog();
                    if (res.isOK()) {
                        AppContext.showToast("绑定成功");
                        ac.setProperty(Const.USER_PHONE, phone);
                        finish();
                    } else {
                        ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                    }
                }

                @Override
                protected void onApiError(String tag) {
                    hideWaitDialog();
                }
            }, ac.getLoginUid(), validCode, Md5Utils.md5(pwd), phone, null);
        }
    }

    private void showVerify() {
        submit_btn.setEnabled(phont_et.length() > 0 && validCode_et.length() > 0);
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(0);
        headTitle_tv.setText("绑定手机号");
        submit_btn.setText("验证");
    }

    private void showPassword() {
        submit_btn.setEnabled(pwd_et.length() > 0 && pwd2_et.length() > 0);
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(1);
        headTitle_tv.setText("设置密码");
        submit_btn.setText("提交");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int index = input_vp.getDisplayedChild();
        if (index == 0) {
            submit_btn.setEnabled(phont_et.length() > 0 && validCode_et.length() > 0);
        } else {
            submit_btn.setEnabled(pwd_et.length() > 0 && pwd2_et.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
