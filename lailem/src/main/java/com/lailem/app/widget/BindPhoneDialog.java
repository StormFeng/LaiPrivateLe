package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.Md5Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindPhoneDialog extends Dialog {
    private static final int DEFAULT_THEME = R.style.confirm_dialog;

    private Context context;
    private BaseActivity _activity;
    private AppContext ac;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.close)
    ImageView close_iv;

    @Bind(R.id.titleViewFlipper)
    ViewFlipper titleViewFlipper;
    @Bind(R.id.tipSwitcherOne)
    ViewSwitcher tipSwitcherOne;
    @Bind(R.id.tipSwitcherTwo)
    ViewSwitcher tipSwitcherTwo;
    @Bind(R.id.tipSwitcherThree)
    ViewSwitcher tipSwitcherThree;
    @Bind(R.id.inputViewFlipper)
    ViewFlipper inputViewFlipper;

    @Bind(R.id.tipOne)
    TextView tipOne_tv;
    @Bind(R.id.tipTwo)
    TextView tipTwo_tv;
    @Bind(R.id.tipThree)
    TextView tipThree_tv;

    @Bind(R.id.phone)
    EditText phone_et;
    @Bind(R.id.validCode)
    EditText validCode_et;
    @Bind(R.id.password)
    EditText password_et;

    @Bind(R.id.getValid)
    ValidCodeButton getValid_tv;

    @Bind(R.id.getValidCode)
    TextView getValidCode_tv;
    @Bind(R.id.verifyValidCode)
    TextView verifyValidCode_tv;
    @Bind(R.id.setPassword)
    TextView setPassword_tv;

    private Animation inAnimation;
    private Animation outAnimation;

    private OnBindPhoneSuccessListener onBindPhoneSuccessListener;

    public interface OnBindPhoneSuccessListener {
        void onBindSuccess(String password);
    }

    public BindPhoneDialog(Context context) {
        super(context, DEFAULT_THEME);
        init(context);
    }

    public BindPhoneDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this._activity = (BaseActivity) context;
        this.ac = (AppContext) context.getApplicationContext();
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        View contentView = View.inflate(context, R.layout.dialog_bind_phone, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);

        inAnimation = AnimationUtils.loadAnimation(context, R.anim.right_in);
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.left_out);
        inputViewFlipper.setInAnimation(inAnimation);
        inputViewFlipper.setOutAnimation(outAnimation);

        getValid_tv.setOnTapStartListener(new ValidCodeButton.OnTapStartListener() {
            @Override
            public void onTapStart() {
                String phone = phone_et.getText().toString().trim();
                getValid_tv.startByAuthCode(phone, Const.AUTH_TYPE_BIND_PHONE, null);
            }
        });

    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.getValidCode)
    public void getVlaidCode() {
        String phone = phone_et.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            AppContext.showToast("请输入手机号");
            return;
        }
        if (!Func.isMobileNO(phone)) {
            AppContext.showToast("手机号格式不正确");
            return;
        }
        ApiClient.getApi().authCode(new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                tipSwitcherOne.setDisplayedChild(0);
                progressBar.setVisibility(View.VISIBLE);
                close_iv.setVisibility(View.INVISIBLE);
                phone_et.setEnabled(false);
                getValidCode_tv.setEnabled(false);
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                phone_et.setEnabled(true);
                getValidCode_tv.setEnabled(true);
                if (res.isOK()) {
                    titleViewFlipper.setDisplayedChild(1);
                    inputViewFlipper.setDisplayedChild(1);
                    getValid_tv.startCounting();
                } else {
                    tipSwitcherOne.setDisplayedChild(1);
                    tipOne_tv.setText(res.errorInfo);
                }
            }

            @Override
            protected void onApiError(String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                phone_et.setEnabled(true);
                getValidCode_tv.setEnabled(true);
                tipSwitcherOne.setDisplayedChild(1);
                tipOne_tv.setText("网络出错啦，请重试");
            }
        }, Const.AUTH_TYPE_BIND_PHONE, phone);
    }

    /**
     * 验证验证码
     */
    @OnClick(R.id.verifyValidCode)
    public void verifyValidCode() {
        String validCode = validCode_et.getText().toString().trim();
        if (TextUtils.isEmpty(validCode)) {
            AppContext.showToast("请输入验证码");
            return;
        }
        ApiCallbackAdapter apiCallbackAdapter = new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                tipSwitcherTwo.setDisplayedChild(0);
                progressBar.setVisibility(View.VISIBLE);
                close_iv.setVisibility(View.INVISIBLE);
                validCode_et.setEnabled(false);
                verifyValidCode_tv.setEnabled(false);
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                validCode_et.setEnabled(true);
                verifyValidCode_tv.setEnabled(true);
                if (res.isOK()) {
                    titleViewFlipper.setDisplayedChild(2);
                    inputViewFlipper.setDisplayedChild(2);
                } else {
                    tipSwitcherTwo.setDisplayedChild(1);
                    tipTwo_tv.setText(res.errorInfo);
                }

            }

            @Override
            protected void onApiError(String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                validCode_et.setEnabled(true);
                verifyValidCode_tv.setEnabled(true);
                tipSwitcherTwo.setDisplayedChild(1);
                tipTwo_tv.setText("网络出错啦，请重试");
            }
        };
//		if (ac.isCIA()) {
//			// CIA验证
//
//		} else {
        // 验证码验证
        ApiClient.getApi().verifyAuthCode(apiCallbackAdapter, validCode, phone_et.getText().toString().trim(), null);
//		}
    }

    /**
     * 设置密码
     */
    @OnClick(R.id.setPassword)
    public void setPassword() {
        final String password = password_et.getText().toString().trim();
        if (password.length() < 6) {
            AppContext.showToast("密码要求6-32位");
            return;
        }
        ApiClient.getApi().bindPhone(new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                tipSwitcherThree.setDisplayedChild(0);
                progressBar.setVisibility(View.VISIBLE);
                close_iv.setVisibility(View.INVISIBLE);
                password_et.setEnabled(false);
                setPassword_tv.setEnabled(false);
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                password_et.setEnabled(true);
                setPassword_tv.setEnabled(true);
                if (res.isOK()) {
                    dismiss();
                    ac.setProperty(Const.USER_PHONE, phone_et.getText().toString().trim());
                    if (onBindPhoneSuccessListener != null) {
                        onBindPhoneSuccessListener.onBindSuccess(Md5Utils.md5(password));
                    }
                } else {
                    tipSwitcherThree.setDisplayedChild(1);
                    tipThree_tv.setText(res.errorInfo);
                }
            }

            @Override
            protected void onApiError(String tag) {
                progressBar.setVisibility(View.INVISIBLE);
                close_iv.setVisibility(View.VISIBLE);
                password_et.setEnabled(true);
                setPassword_tv.setEnabled(true);
                tipSwitcherThree.setDisplayedChild(1);
                tipThree_tv.setText("网络出错啦，请重试");
            }
        }, ac.getLoginUid(), validCode_et.getText().toString().trim(), Md5Utils.md5(password), phone_et.getText().toString().trim(), null);
    }

    @OnClick(R.id.close)
    public void close() {
        dismiss();
    }

    public void setOnBindPhoneSuccessListener(OnBindPhoneSuccessListener onBindPhoneSuccessListener) {
        this.onBindPhoneSuccessListener = onBindPhoneSuccessListener;
    }

}
