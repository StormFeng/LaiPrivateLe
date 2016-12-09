package com.lailem.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
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
import com.lailem.app.utils.UIHelper;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThirdLoginActivity extends BaseActivity implements PlatformActionListener {
    public static final String LOGIN_TYPE = "login_type";
    public static final int LOGIN_TYPE_WEIXIN = 1;
    public static final int LOGIN_TYPE_QQ = 2;
    public static final int LOGIN_TYPE_WEIBO = 3;

    @Bind(R.id.thirdUserInfo)
    TextView thirdUserInfo_tv;
    private ILoginManager wechatLoginManager;
    private ILoginManager qqLoginManager;
    private ILoginManager weiboLoginManager;
    private SsoHandler mSsoHandler;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_third);
        setTranslucentStatus(true, R.color.white);
        ButterKnife.bind(this);

        this.weiboLoginManager = new WeiboLoginManager(this);
        this.wechatLoginManager = new WechatLoginManager(this);
        this.qqLoginManager = new QQLoginManager(this);

        if (_Bundle != null) {
            int loginType = _Bundle.getInt(LOGIN_TYPE);
            switch (loginType) {
                case LOGIN_TYPE_WEIXIN:
                    loginWeixin();
                    break;
                case LOGIN_TYPE_QQ:
                    loginQQ();
                    break;
                case LOGIN_TYPE_WEIBO:
                    loginWeibo();
                    break;
            }
        }
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.loginWeixin)
    public void loginWeixin() {
        type = "2";
        this.wechatLoginManager.login(this);
    }

    @OnClick(R.id.loginQQ)
    public void loginQQ() {
        type = "3";
        this.qqLoginManager.login(this);
    }

    @OnClick(R.id.loginWeibo)
    public void loginWeibo() {
        type = "4";
        this.weiboLoginManager.login(this);
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
            UserBean userBean = (UserBean) res;

            AppContext.showToast("登录成功");
            if (Const.DATA_INTACT_NO.equals(userBean.getUserInfo().getDataIntact())) {
                UIHelper.showEditProfileActivity(_activity, userBean);
            } else {
                setResult(RESULT_OK);
                ac.saveUserInfo(userBean);
                if (ac.isNeedLoginCallback()) {
                    ac.excuteLoginCallback();
                } else {
                    UIHelper.showMain(this, true);
                }
                BroadcastManager.sendLoginBroadcast(_activity);
            }
            finish();

        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
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
        ApiClient.getApi().loginThird(this, headBUrl, nickName, openId, type, unionId, birthday, cityId, provinceId, sex);
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

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }
}
