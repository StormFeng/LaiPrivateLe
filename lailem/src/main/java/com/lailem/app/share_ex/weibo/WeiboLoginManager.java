package com.lailem.app.share_ex.weibo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.lailem.app.share_ex.ShareBlock;
import com.lailem.app.share_ex.data.ShareConstants;
import com.lailem.app.share_ex.model.ILoginManager;
import com.lailem.app.share_ex.model.PlatformActionListener;
import com.lailem.app.share_ex.weibo.model.User;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.CommonAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by echo on 5/19/15.
 */
public class WeiboLoginManager implements ILoginManager {

    private static final String SCOPE =
            "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog";

    private Context mContext;

    private static String mSinaAppKey;

    private AuthInfo mAuthInfo = null;

    private UsersAPI userAPI;
    private CommonAPI commonAPI;

    private PlatformActionListener mPlatformActionListener;


    private String mRedirectUrl;


    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private static SsoHandler mSsoHandler;


    public WeiboLoginManager(Context context) {

        mContext = context;
        mSinaAppKey = ShareBlock.getInstance().getWeiboAppId();
        mRedirectUrl = ShareBlock.getInstance().getRedriectUrl();

    }


    public static SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    @Override
    public void login(PlatformActionListener platformActionListener) {
        mPlatformActionListener = platformActionListener;
        AccessTokenKeeper.clear(mContext);
        mAuthInfo = new AuthInfo(mContext, mSinaAppKey, mRedirectUrl, SCOPE);
        mSsoHandler = new SsoHandler((Activity) mContext, mAuthInfo);
        mSsoHandler.authorize(new AuthListener());

    }

    /**
     * * 1. SSO 授权时，需要在 onActivityResult 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非SSO 授权时，当授权结束后，该回调就会被执行
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AccessTokenKeeper.writeAccessToken(mContext, accessToken);
                userAPI = new UsersAPI(mContext, mSinaAppKey, accessToken);
                commonAPI = new CommonAPI(mContext, mSinaAppKey, accessToken);
                userAPI.show(Long.parseLong(accessToken.getUid()), mListener);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mPlatformActionListener != null) {
                mPlatformActionListener.onError();
            }
        }

        @Override
        public void onCancel() {
            if (mPlatformActionListener != null) {
                mPlatformActionListener.onCancel();
            }
        }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {

                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    final HashMap<String, Object> userInfoHashMap
                            = new HashMap<String, Object>();
                    userInfoHashMap.put(ShareConstants.PARAMS_NICK_NAME,
                            user.name);
                    userInfoHashMap
                            .put(ShareConstants.PARAMS_SEX, user.gender);
                    userInfoHashMap.put(ShareConstants.PARAMS_IMAGEURL,
                            user.avatar_large);
                    userInfoHashMap.put(ShareConstants.PARAMS_USERID,
                            user.id);

                    DecimalFormat format = new DecimalFormat("000");
                    final String provinceCode = "001" + format.format(user.province);
                    final String cityCode = "001" + format.format(user.province) + format.format(user.city);
                    commonAPI.codeToLocation(provinceCode + "," + cityCode, new RequestListener() {
                        @Override
                        public void onComplete(String s) {
                            try {
                                JSONArray jsonArray = new JSONArray(s);
                                if (jsonArray != null) {
                                    JSONObject pO = (JSONObject) jsonArray.get(0);
                                    JSONObject cO = (JSONObject) jsonArray.get(1);
                                    String p = pO.optString(provinceCode);
                                    String c = cO.optString(cityCode);
                                    if (!TextUtils.isEmpty(p) && !TextUtils.isEmpty(c)) {
                                        userInfoHashMap.put(ShareConstants.PARAMS_PRIVOINCE, p);
                                        userInfoHashMap.put(ShareConstants.PARAMS_CITY, c);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (mPlatformActionListener != null) {
                                mPlatformActionListener.onComplete(userInfoHashMap);
                            }

                        }

                        @Override
                        public void onWeiboException(WeiboException e) {
                            if (mPlatformActionListener != null) {
                                mPlatformActionListener.onError();
                            }
                        }
                    });
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mPlatformActionListener != null) {
                mPlatformActionListener.onError();
            }
        }
    };


}
