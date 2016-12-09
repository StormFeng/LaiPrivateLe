package com.lailem.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.share_ex.data.ShareConstants;
import com.lailem.app.share_ex.model.ILoginManager;
import com.lailem.app.share_ex.model.PlatformActionListener;
import com.lailem.app.share_ex.qq.QQLoginManager;
import com.lailem.app.share_ex.wechat.WechatLoginManager;
import com.lailem.app.share_ex.weibo.WeiboLoginManager;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.Md5Utils;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.ValidCodeButton;
import com.lailem.app.widget.ValidCodeButton.OnActionListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements TextWatcher, PlatformActionListener {
    public static final String API_TAG_LOGIN = "login";

    public static final String BUNDLE_KEY_PHONE = "phone";
    public static final String BUNDLE_KEY_PASSWORD = "pwd";
    public static final String BUNDLE_KEY_VALIDCODE = "validCode";
    public static final String BUNDLE_KEY_NICKNAME = "nickname";
    public static final String BUNDLE_KEY_BIRTHDAY = "birthday";
    public static final String BUNDLE_KEY_SEX = "sex";
    public static final String BUNDLE_KEY_PROVINCEID = "provinceId";
    public static final String BUNDLE_KEY_CITYID = "cityId";
    public static final String BUNDLE_KEY_TRANSID = "transId";

    public static final int REQUEST_LOGIN_THIRD = 1000;

    @Bind(R.id.close)
    ImageView close_iv;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.loginThird)
    TextView loginThird_tv;
    @Bind(R.id.input_vp)
    ViewFlipper input_vp;
    @Bind(R.id.headTitle)
    TextView headTitle_tv;
    @Bind(R.id.loginPhone)
    EditText loginPhone_et;
    @Bind(R.id.loginPwd)
    EditText loginPwd_et;
    @Bind(R.id.registerPhone)
    EditText registerPhone_et;
    @Bind(R.id.registerPwd)
    EditText registerPwd_et;
    @Bind(R.id.registerPwd2)
    EditText registerPwd2_et;
    @Bind(R.id.submit)
    Button submit_btn;
    @Bind(R.id.toggleLoginRegister)
    CheckBox toggleLoginRegister_cb;
    @Bind(R.id.toggleLoginPwd)
    CheckBox toggleLoginPwd_cb;
    @Bind(R.id.toggleRegisterPwd)
    CheckBox toggleRegisterPwd_cb;
    @Bind(R.id.hiddenValidButton)
    ValidCodeButton hiddenValidButton;


    private ILoginManager wechatLoginManager;
    private ILoginManager qqLoginManager;
    private ILoginManager weiboLoginManager;
    private SsoHandler mSsoHandler;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);
        initView();

        if (!ac.isInitShareReady) {
            ac.initShare();
        }

        this.weiboLoginManager = new WeiboLoginManager(this);
        this.wechatLoginManager = new WechatLoginManager(this);
        this.qqLoginManager = new QQLoginManager(this);
    }

    private void initView() {
        loginPhone_et.addTextChangedListener(this);
        loginPwd_et.addTextChangedListener(this);
        registerPhone_et.addTextChangedListener(this);
        registerPwd_et.addTextChangedListener(this);
        registerPwd2_et.addTextChangedListener(this);

        toggleLoginPwd_cb.setChecked(false);
        toggleRegisterPwd_cb.setChecked(false);

        String phone = ac.getProperty(Const.USER_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            loginPhone_et.setText(phone);
            loginPhone_et.setSelection(phone.length());
        }

    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @OnClick(R.id.loginThird)
    public void loginThird() {
        UIHelper.showLoginThird(this);
    }

    @OnClick(R.id.forgetPwd)
    public void showForgetPwd() {
        UIHelper.showForgetPwd(this);
    }

    @OnClick(R.id.weixin)
    public void clickWeixin() {
        type = "2";
        this.wechatLoginManager.login(this);
    }

    @OnClick(R.id.qq)
    public void clickQQ() {
        type = "3";
        this.qqLoginManager.login(this);
    }

    @OnClick(R.id.weibo)
    public void clickWeibo() {
        type = "4";
        this.weiboLoginManager.login(this);
    }


    @OnClick(R.id.submit)
    public void submit(Button btn) {
        if (toggleLoginRegister_cb.isChecked()) {
            register();
        } else {
            login();
        }
    }

    @OnCheckedChanged({R.id.toggleLoginRegister, R.id.toggleLoginPwd, R.id.toggleRegisterPwd})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.toggleLoginRegister:
                toggleLoginRegister(buttonView, isChecked);
                break;
            case R.id.toggleLoginPwd:
                if (isChecked) {
                    loginPwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    loginPwd_et.setSelection(loginPwd_et.length());
                } else {
                    loginPwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    loginPwd_et.setSelection(loginPwd_et.length());
                }
                break;
            case R.id.toggleRegisterPwd:
                if (isChecked) {
                    registerPwd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    registerPwd_et.setSelection(registerPwd_et.length());
                } else {
                    registerPwd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    registerPwd_et.setSelection(registerPwd_et.length());
                }
                break;
        }
    }

    private void toggleLoginRegister(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // 注册页面
            showRegister();
        } else {
            // 登录页面
            showLogin();
        }
    }

    private void showLogin() {
        headTitle_tv.setText("账号密码登录");
        toggleLoginRegister_cb.setText("注册账号");
        submit_btn.setText("登录");
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(2);
        submit_btn.setEnabled(loginPhone_et.length() > 0 && loginPwd_et.length() > 0);
    }

    private void showRegister() {
        headTitle_tv.setText("注册来了账号");
        toggleLoginRegister_cb.setText("登录账号");
        submit_btn.setText("下一步");
        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        input_vp.setInAnimation(inAnimation);
        input_vp.setOutAnimation(outAnimation);
        input_vp.setDisplayedChild(1);
        submit_btn.setEnabled(registerPhone_et.length() > 0 && registerPwd_et.length() > 0 && registerPwd2_et.length() > 0);
    }

    private void showProgressBar() {
        close_iv.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        close_iv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void login() {
        String phone = loginPhone_et.getText().toString().trim();
        String pwd = loginPwd_et.getText().toString().trim();
        if (!StringUtils.isMobileNO(phone)) {
            AppContext.showToast("请输入正确格式的手机号");
            return;
        }
        ApiClient.getApi().login(this, phone, Md5Utils.md5(pwd));
    }

    private void register() {
        final String phone = registerPhone_et.getText().toString().trim();
        final String pwd = registerPwd_et.getText().toString().trim();
        String pwd2 = registerPwd2_et.getText().toString().trim();
        if (!StringUtils.isMobileNO(phone)) {
            AppContext.showToast("请输入正确格式的手机号");
            return;
        }
        if (pwd.length() < 6) {
            AppContext.showToast("密码要求6-32位");
            return;
        }
        if (!pwd.equals(pwd2)) {
            AppContext.showToast("两次输入的密码不一致");
            return;
        }
        OnActionListener onActionListener = new OnActionListener() {

            @Override
            public void onSuccess(boolean isNeedVerifyCode, String transId) {
                hideProgressBar();
                submit_btn.setEnabled(true);
                if (!TextUtils.isEmpty(transId)) {
                    if (isNeedVerifyCode) {
                        // 需要进一步输入验证码验证
                        AppContext.showToast("请输入验证码");
                        UIHelper.showRegisterVerify(_activity, phone, Md5Utils.md5(pwd), transId);
                    } else {
                        // 已经验证成功
                        AppContext.showToast("验证成功");
                        UIHelper.showRegisterInfo(_activity, phone, Md5Utils.md5(pwd), "", transId);
                    }
                } else {
                    AppContext.showToast("验证码发送成功");
                    UIHelper.showRegisterVerify(_activity, phone, Md5Utils.md5(pwd), "");
                }
            }

            @Override
            public void onStart() {
                showProgressBar();
                submit_btn.setEnabled(false);
                loginThird_tv.setEnabled(false);
            }

            @Override
            public void onFail(String msg) {
                AppContext.showToast(msg);
                hideProgressBar();
                submit_btn.setEnabled(true);
                loginThird_tv.setEnabled(true);
            }
        };
        hiddenValidButton.startByAuthCode(phone, Const.AUTH_TYPE_REGISTER, onActionListener);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (toggleLoginRegister_cb.isChecked()) {
            // 注册页面
            submit_btn.setEnabled(registerPhone_et.length() > 0 && registerPwd_et.length() > 0 && registerPwd2_et.length() > 0);
        } else {
            // 登录页面
            submit_btn.setEnabled(loginPhone_et.length() > 0 && loginPwd_et.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (input_vp.getDisplayedChild() == 1) {
                showLogin();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        if (API_TAG_LOGIN.equals(tag)) {
            showWaitDialog();
        }
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        if (API_TAG_LOGIN.equals(tag)) {
            if (res.isOK()) {
                UserBean userBean = (UserBean) res;

                if (Const.DATA_INTACT_NO.equals(userBean.getUserInfo().getDataIntact())) {
                    UIHelper.showEditProfileActivity(_activity, userBean);
                } else {
                    ac.saveUserInfo(userBean);
                    if (ac.isNeedLoginCallback()) {
                        ac.excuteLoginCallback();
                    } else {
                        UIHelper.showMain(this, true);
                    }
                    BroadcastManager.sendLoginBroadcast(_activity);
                }
                AppContext.showToast("登录成功");
                finish();

            } else {
                ac.handleErrorCode(this, res.errorCode, res.errorInfo);
            }
        }
        hideWaitDialog();
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }


    @Override
    public void onComplete(HashMap<String, Object> userInfo) {
        System.out.println("onComplete");

        String headBUrl = userInfo.get(ShareConstants.PARAMS_IMAGEURL).toString();
        String nickName = userInfo.get(ShareConstants.PARAMS_NICK_NAME).toString();
        String openId = userInfo.get(ShareConstants.PARAMS_USERID).toString();
        String birthday = null;
        String cityId = null;
        String provinceId = null;
        String sex = null;
        String unionId = null;

        //省份和城市
        String provinceName = userInfo.get(ShareConstants.PARAMS_PRIVOINCE).toString();
        String cityName = userInfo.get(ShareConstants.PARAMS_CITY).toString();
        String[] ids = Func.getIdByName(provinceName, cityName);
        provinceId = ids[0];
        cityId = ids[1];

        if (type.equals("2")) {
            //微信
            unionId = userInfo.get(ShareConstants.PARAMS_UNIONID).toString();
            //性别
            String weixinSex = userInfo.get(ShareConstants.PARAMS_SEX).toString();
            if (!TextUtils.isEmpty(weixinSex)) {
                if ("1".equals(weixinSex)) {
                    //男
                    sex = Const.MALE;
                } else if ("2".equals(weixinSex)) {
                    sex = Const.FEMALE;
                }
            }
        } else if (type.equals("3")) {
            //QQ
            //性别
            String qqSex = userInfo.get(ShareConstants.PARAMS_SEX).toString();
            if (!TextUtils.isEmpty(qqSex)) {
                if ("男".equals(qqSex)) {
                    sex = Const.MALE;
                } else if ("女".equals(qqSex)) {
                    sex = Const.FEMALE;
                }
            }
        } else if (type.equals("4")) {
            //新浪微博
            //性别
            String weiboSex = userInfo.get(ShareConstants.PARAMS_SEX).toString();
            if (!TextUtils.isEmpty(weiboSex)) {
                if ("m".equals(weiboSex)) {
                    //男
                    sex = Const.MALE;
                } else if ("f".equals(weiboSex)) {
                    //女
                    sex = Const.FEMALE;
                }
            }
        }
        ApiClient.getApi().loginThird(new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                super.onApiStart(tag);
                showWaitDialog();
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                super.onApiSuccess(res, tag);
                if (res.isOK()) {
                    UserBean userBean = (UserBean) res;

                    AppContext.showToast("登录成功");
                    if (Const.DATA_INTACT_NO.equals(userBean.getUserInfo().getDataIntact())) {
                        UIHelper.showEditProfileActivity(_activity, userBean);
                    } else {
                        ac.saveUserInfo(userBean);
                        if (ac.isNeedLoginCallback()) {
                            ac.excuteLoginCallback();
                        } else {
                            UIHelper.showMain(_activity, true);
                        }
                        BroadcastManager.sendLoginBroadcast(_activity);
                    }
                    finish();

                } else {
                    ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                }
                hideWaitDialog();
            }

            @Override
            protected void onApiError(String tag) {
                super.onApiError(tag);
                hideWaitDialog();
            }
        }, headBUrl, nickName, openId, type, unionId, birthday, cityId, provinceId, sex);
    }

    @Override
    public void onError() {
        System.out.println("onError");
    }

    @Override
    public void onCancel() {
        System.out.println("onCancel");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mSsoHandler = WeiboLoginManager.getSsoHandler();
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        QQLoginManager ql = (QQLoginManager) qqLoginManager;
        Tencent.onActivityResultData(requestCode, resultCode, data, ql.getLoginListener());

        if (requestCode == Constants.REQUEST_API) {
            Tencent.handleResultData(data, ql.getLoginListener());
        }
    }
}
