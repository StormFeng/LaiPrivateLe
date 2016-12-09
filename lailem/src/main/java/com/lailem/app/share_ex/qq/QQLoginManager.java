package com.lailem.app.share_ex.qq;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.share_ex.ShareBlock;
import com.lailem.app.share_ex.data.ShareConstants;
import com.lailem.app.share_ex.model.ILoginManager;
import com.lailem.app.share_ex.model.PlatformActionListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by echo on 5/19/15.
 */
public class QQLoginManager implements ILoginManager {


    private Context mContext;

    private String mAppId;

    private Tencent mTencent;

    protected PlatformActionListener mPlatformActionListener;

    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object object) {
            JSONObject jsonObject = (JSONObject) object;
            initOpenidAndToken(jsonObject);
            UserInfo info = new UserInfo(mContext, mTencent.getQQToken());
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    HashMap<String, Object> userInfoHashMap = new HashMap<String, Object>();
                    userInfoHashMap.put(ShareConstants.PARAMS_NICK_NAME, jsonObject.optString("nickname"));
                    userInfoHashMap.put(ShareConstants.PARAMS_SEX, jsonObject.optString("gender"));
                    userInfoHashMap.put(ShareConstants.PARAMS_IMAGEURL, jsonObject.optString("figureurl_qq_2"));
                    userInfoHashMap.put(ShareConstants.PARAMS_USERID, mTencent.getOpenId());
                    userInfoHashMap.put(ShareConstants.PARAMS_PRIVOINCE, jsonObject.optString("province"));
                    userInfoHashMap.put(ShareConstants.PARAMS_CITY, jsonObject.optString("city"));

                    if (mPlatformActionListener != null) {
                        mPlatformActionListener.onComplete(userInfoHashMap);
                    }
                }

                @Override
                public void onError(UiError uiError) {
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
            });
        }

        @Override
        public void onError(UiError uiError) {
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
    };


    public QQLoginManager(Context context) {
        mContext = context;
        mAppId = ShareBlock.getInstance().getQQAppId();
        if (!TextUtils.isEmpty(mAppId)) {
            mTencent = Tencent.createInstance(mAppId, context);
        }
    }


    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void login(PlatformActionListener platformActionListener) {
        if (!mTencent.isSessionValid()) {

            mPlatformActionListener = platformActionListener;
            mTencent.login((Activity) mContext, "all", loginListener);
        } else {
            mTencent.logout(mContext);
        }
    }

    public IUiListener getLoginListener() {
        return loginListener;
    }
}


