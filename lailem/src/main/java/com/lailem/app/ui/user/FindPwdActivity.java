package com.lailem.app.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.Md5Utils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ValidCodeButton;
import com.lailem.app.widget.ValidCodeButton.OnActionListener;
import com.lailem.app.widget.ValidCodeButton.OnTapStartListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdActivity extends BaseActivity implements TextWatcher {
    public static final String API_TAG_VERIFY_AUTHCODE = "verifyAuthCode";
    public static final String API_TAG_FORGET_PASSWORD = "forgetPassword";
    @Bind(R.id.close)
    ImageView close_iv;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.headTitle)
    TextView headTitle_tv;
    @Bind(R.id.input_vp)
    ViewFlipper input_vp;
    @Bind(R.id.phone)
    EditText phone_et;
    @Bind(R.id.hiddenValidButton)
    ValidCodeButton hiddenValidButton;
    @Bind(R.id.getValidCode)
    ValidCodeButton getValidCode_btn;
    @Bind(R.id.validCode)
    EditText validCode_et;
    @Bind(R.id.pwd)
    EditText pwd_et;
    @Bind(R.id.pwd2)
    EditText pwd2_et;
    @Bind(R.id.submit)
    Button submit_btn;
    private String transId;
    private OnActionListener ciaActionListener;
    private OnActionListener authCodeActionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        phone_et.addTextChangedListener(this);
        validCode_et.addTextChangedListener(this);
        pwd_et.addTextChangedListener(this);
        pwd2_et.addTextChangedListener(this);

        ciaActionListener = new OnActionListener() {
            @Override
            public void onSuccess(boolean isNeedVerifyCode, String transId) {
                FindPwdActivity.this.transId = transId;
                submit_btn.setEnabled(true);
                hideProgressBar();
                if (isNeedVerifyCode) {
                    AppContext.showToast("请输入验证码");
                    showValidCode();
                } else {
                    AppContext.showToast("验证成功");
                    showPassword();
                }
            }

            @Override
            public void onStart() {
                submit_btn.setEnabled(false);
                showProgressBar();
            }

            @Override
            public void onFail(String msg) {
                AppContext.showToast(msg);
                submit_btn.setEnabled(true);
                hideProgressBar();
            }
        };

        authCodeActionListener = new OnActionListener() {
            @Override
            public void onStart() {
                submit_btn.setEnabled(false);
                showProgressBar();
            }

            @Override
            public void onSuccess(boolean isNeedVerifyCode, String transId) {
                AppContext.showToast("短信发送成功");
                submit_btn.setEnabled(true);
                hideProgressBar();
                showValidCode();

            }

            @Override
            public void onFail(String msg) {
                AppContext.showToast(msg);
                submit_btn.setEnabled(true);
                hideProgressBar();
            }
        };


        getValidCode_btn.setOnTapStartListener(new OnTapStartListener() {

            @Override
            public void onTapStart() {
                String phone = phone_et.getText().toString().trim();
                // 短信验证
                getValidCode_btn.startByAuthCode(phone, Const.AUTH_TYPE_FORGETPWD, authCodeActionListener);
            }
        });
    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (input_vp.getDisplayedChild() == 0) {
            onPhoneSubmit();
        } else if (input_vp.getDisplayedChild() == 1) {
            onValidCodeSubmit();
        } else if (input_vp.getDisplayedChild() == 2) {
            onPasswordSubmit();
        }
    }

    /**
     * 输入手机号后提交
     */
    private void onPhoneSubmit() {
        String phone = phone_et.getText().toString().trim();
        if (!Func.isMobileNO(phone)) {
            AppContext.showToast("手机号格式不正确");
            return;
        }
        getValidCode_btn.startByAuthCode(phone, Const.AUTH_TYPE_FORGETPWD, authCodeActionListener);
    }

    /**
     * 输入验证码后提交
     */
    private void onValidCodeSubmit() {
        final String phone = phone_et.getText().toString().trim();
        String validCode = validCode_et.getText().toString().trim();
        if (TextUtils.isEmpty(validCode)) {
            AppContext.showToast("请输入验证码");
            return;
        }
        ApiClient.getApi().verifyAuthCode(this, validCode, phone, null);
    }

    /**
     * 输入密码后提交
     */
    private void onPasswordSubmit() {
        String phone = phone_et.getText().toString().trim();
        String validCode = validCode_et.getText().toString().trim();
        String pwd = pwd_et.getText().toString().trim();
        String pwd2 = pwd2_et.getText().toString().trim();

        if (pwd.length() < 6) {
            AppContext.showToast("密码要求6-32位");
            return;
        }

        if (!pwd.equals(pwd2)) {
            AppContext.showToast("两次输入的密码不一致");
            return;
        }
        ApiClient.getApi().forgetPassword(this, validCode, Md5Utils.md5(pwd), phone, null);
    }

    /**
     * 显示输入手机号
     */
    private void showPhone() {
        if (input_vp.getDisplayedChild() == 0) {
            return;
        }
        headTitle_tv.setText("忘记密码-验证手机");
        submit_btn.setEnabled(phone_et.length() > 0);
        submit_btn.setText("下一步");
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(0);
    }

    /**
     * 显示输入验证码
     */
    private void showValidCode() {
        if (input_vp.getDisplayedChild() == 1) {
            return;
        }
        getValidCode_btn.startCounting();
        headTitle_tv.setText("忘记密码-验证手机");
        submit_btn.setEnabled(validCode_et.length() > 0);
        submit_btn.setText("下一步");
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(1);
    }

    /**
     * 显示输入密码
     */
    private void showPassword() {
        if (input_vp.getDisplayedChild() == 2) {
            return;
        }
        headTitle_tv.setText("忘记密码-重置");
        submit_btn.setEnabled(pwd_et.length() > 0 && pwd2_et.length() > 0);
        submit_btn.setText("完成");
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(2);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (input_vp.getDisplayedChild() == 0) {
            submit_btn.setEnabled(phone_et.length() > 0);
        } else if (input_vp.getDisplayedChild() == 1) {
            submit_btn.setEnabled(validCode_et.length() > 0);
        } else if (input_vp.getDisplayedChild() == 2) {
            submit_btn.setEnabled(pwd_et.length() > 0 && pwd2_et.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        submit_btn.setEnabled(false);
        showProgressBar();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        submit_btn.setEnabled(true);
        hideProgressBar();
        if (res.isOK()) {
            if (API_TAG_VERIFY_AUTHCODE.equals(tag)) {
                showPassword();
            } else if (API_TAG_FORGET_PASSWORD.equals(tag)) {
                AppContext.showToast("修改成功");
                UIHelper.showLogin(this, true);
            }
        } else {
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        submit_btn.setEnabled(true);
        hideProgressBar();
    }

    private void showProgressBar() {
        close_iv.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        close_iv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
