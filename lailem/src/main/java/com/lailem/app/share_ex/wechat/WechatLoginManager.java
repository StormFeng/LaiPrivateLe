package com.lailem.app.share_ex.wechat;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.share_ex.ShareBlock;
import com.lailem.app.share_ex.model.ILoginManager;
import com.lailem.app.share_ex.model.PlatformActionListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by echo on 5/19/15.
 */
public class WechatLoginManager implements ILoginManager {

    private static final String SCOPE = "snsapi_userinfo";

    private static final String STATE = "lls_engzo_wechat_login";


    private String mWeChatAppId;

    private static IWXAPI mIWXAPI;

    private static PlatformActionListener mPlatformActionListener;

    public WechatLoginManager(Context context) {
        mWeChatAppId = ShareBlock.getInstance().getWechatAppId();
        if (!TextUtils.isEmpty(mWeChatAppId)) {
            mIWXAPI = WXAPIFactory.createWXAPI(context, mWeChatAppId, true);
            if (!mIWXAPI.isWXAppInstalled()) {
//                Toast.makeText(context, context.getString(R.string.share_install_wechat_tips), Toast.LENGTH_SHORT).show();
                return;
            } else {
                mIWXAPI.registerApp(mWeChatAppId);
            }
        }
    }


    public static IWXAPI getIWXAPI() {
        return mIWXAPI;
    }


    public static PlatformActionListener getPlatformActionListener() {
        return mPlatformActionListener;
    }

    @Override
    public void login(PlatformActionListener platformActionListener) {
        if (!mIWXAPI.isWXAppInstalled()) {
            AppContext.showToast(R.string.share_install_wechat_tips);
            return;
        }
        if (mIWXAPI != null) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = SCOPE;
            req.state = STATE;
            mIWXAPI.sendReq(req);
            mPlatformActionListener = platformActionListener;
        }
    }
}
