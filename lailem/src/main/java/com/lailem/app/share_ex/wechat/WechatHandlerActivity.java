package com.lailem.app.share_ex.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.lailem.app.R;
import com.lailem.app.share_ex.ShareBlock;
import com.lailem.app.share_ex.data.ShareConstants;
import com.lailem.app.share_ex.model.PlatformActionListener;
import com.lailem.app.share_ex.wechat.model.WeixinAccessToken;
import com.lailem.app.share_ex.wechat.model.WeixinUser;
import com.midian.fastdevelop.afinal.FinalHttp;
import com.midian.fastdevelop.afinal.http.AjaxCallBack;
import com.socks.library.KLog;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;

import java.util.HashMap;

/**
 * Created by echo on 5/19/15.
 */
public class WechatHandlerActivity extends Activity implements IWXAPIEventHandler {

    public static final String URL_WEIXIN_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String URL_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    public static final String URL_WEIXIN_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    private IWXAPI mIWXAPI;

    private PlatformActionListener mPlatformActionListener;


    /**
     * BaseResp的getType函数获得的返回值，1:第三方授权， 2:分享
     */
    private static final int TYPE_LOGIN = 1;

    private Context mContext;
    private FinalHttp httpUtils = new FinalHttp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = WechatHandlerActivity.this;
        mIWXAPI = WechatLoginManager.getIWXAPI();
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mIWXAPI != null) {
            mIWXAPI.handleIntent(getIntent(), this);
        }
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {


        mPlatformActionListener = WechatLoginManager
                .getPlatformActionListener();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                if (resp.getType() == TYPE_LOGIN) {
                    final String code = ((SendAuth.Resp) resp).token;
                    getAccessToken(code);
                } else {
                    Toast.makeText(mContext, mContext.getString(
                            R.string.share_success), Toast.LENGTH_SHORT).show();
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:

                if (resp.getType() == TYPE_LOGIN) {
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onCancel();
                    }
                } else {
//                    Toast.makeText(mContext, mContext.getString(
//                            R.string.share_cancel), Toast.LENGTH_SHORT).show();
                }

                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                if (resp.getType() == TYPE_LOGIN) {
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onError();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(
                            R.string.share_failed), Toast.LENGTH_SHORT).show();
                }

                break;
        }
        finish();
    }

    /**
     * 获取token信息
     */
    private void getAccessToken(String code) {
        String url = URL_WEIXIN_TOKEN + "?appid=" + ShareBlock.getInstance().getWechatAppId() + "&secret=" + ShareBlock.getInstance().getWechatSecret() + "&code=" + code + "&grant_type=authorization_code";
        httpUtils.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s, String requestTag) {
                super.onSuccess(s, requestTag);

                try {
                    WeixinAccessToken tokenBean = WeixinAccessToken.parse(s);

                    if (!TextUtils.isEmpty(tokenBean.getAccess_token())) {
                        //获取用户信息
                        getUserInfo(tokenBean.getAccess_token(), tokenBean.getOpenid());
                    } else {
                        if (mPlatformActionListener != null) {
                            mPlatformActionListener
                                    .onError();
                        }
                    }
                } catch (Exception e) {
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onError();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg, String requestTag) {
                if (mPlatformActionListener != null) {
                    mPlatformActionListener
                            .onError();
                }
            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserInfo(String access_token, String openid) {
        String url = URL_WEIXIN_USERINFO + "?lang=zh_CN&access_token=" + access_token + "&openid=" + openid;
        httpUtils.get(url, new AjaxCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s, String requestTag) {
                super.onSuccess(s, requestTag);
                try {

                    WeixinUser weixinUser = WeixinUser.parse(s);
                    KLog.i(weixinUser.toString());
                    if (!TextUtils.isEmpty(weixinUser.getOpenid())) {
                        //获取用户信息成功
                        HashMap<String, Object> userInfoHashMap
                                = new HashMap<String, Object>();
                        userInfoHashMap
                                .put(ShareConstants.PARAMS_NICK_NAME,
                                        weixinUser.getNickname());
                        userInfoHashMap
                                .put(ShareConstants.PARAMS_SEX,
                                        weixinUser.getSex());
                        userInfoHashMap
                                .put(ShareConstants.PARAMS_IMAGEURL,
                                        weixinUser.getHeadimgurl());
                        userInfoHashMap
                                .put(ShareConstants.PARAMS_USERID,
                                        weixinUser.getOpenid());
                        userInfoHashMap
                                .put(ShareConstants.PARAMS_UNIONID,
                                        weixinUser.getUnionid());
                        userInfoHashMap.put(ShareConstants.PARAMS_PRIVOINCE, weixinUser.getProvince());
                        userInfoHashMap.put(ShareConstants.PARAMS_CITY, weixinUser.getCity());
                        if (mPlatformActionListener != null) {
                            mPlatformActionListener
                                    .onComplete(
                                            userInfoHashMap);
                        }
                    } else {
                        if (mPlatformActionListener != null) {
                            mPlatformActionListener
                                    .onError();
                        }
                    }
                } catch (Exception e) {
                    if (mPlatformActionListener != null) {
                        mPlatformActionListener
                                .onError();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg, String requestTag) {
                if (mPlatformActionListener != null) {
                    mPlatformActionListener
                            .onError();
                }
            }
        });
    }
}
