package com.lailem.app.utils;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.jsonbean.activegroup.IsJoinedGroup;
import com.lailem.app.jsonbean.personal.InviteInfoBean;
import com.lailem.app.widget.AcceptInviteDialog;
import com.socks.library.KLog;

public class AcceptInviteManager {
    private static AcceptInviteManager instance;
    AcceptInviteInfo acceptInviteInfo;

    private AcceptInviteManager() {

    }

    public static AcceptInviteManager getInstance() {
        if (instance == null) {
            instance = new AcceptInviteManager();
        }

        return instance;
    }

    /**
     * 使用intent信息初始化
     *
     * @param activity
     * @param intent
     */
    public void init(BaseActivity activity, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent.getData();
                if (uri != null) {
                    acceptInviteInfo = new AcceptInviteInfo();
                    acceptInviteInfo.gId = uri.getQueryParameter("gId");
                    acceptInviteInfo.gType = uri.getQueryParameter("gType");
                    acceptInviteInfo.gName = uri.getQueryParameter("gName");
                    acceptInviteInfo.gSquareSPic = uri.getQueryParameter("gSquareSPic");
                    acceptInviteInfo.inviteCode = uri.getQueryParameter("inviteCode");
                    acceptInviteInfo.uId = uri.getQueryParameter("uId");
                    acceptInviteInfo.uHead = uri.getQueryParameter("uHead");
                    acceptInviteInfo.uNick = uri.getQueryParameter("uNick");
                    acceptInviteInfo.perm = uri.getQueryParameter("perm");
                }
            }
            show(activity);
        }
    }

    public void init(final BaseActivity baseActivity, String inviteCode) {
        ApiClient.getApi().getInviteInfo(new ApiCallbackAdapter() {
            @Override
            public void onApiStart(String tag) {
                super.onApiStart(tag);
                baseActivity.showWaitDialog();
            }

            @Override
            public void onApiSuccess(Result res, String tag) {
                super.onApiSuccess(res, tag);
                if (res.isOK()) {
                    InviteInfoBean bean = (InviteInfoBean) res;
                    acceptInviteInfo = new AcceptInviteInfo();
                    acceptInviteInfo.gId = bean.getInviteInfo().getGroupInfo().getId();
                    acceptInviteInfo.gType = bean.getInviteInfo().getGroupInfo().getGroupType();
                    acceptInviteInfo.gName = bean.getInviteInfo().getGroupInfo().getName();
                    acceptInviteInfo.gSquareSPic = bean.getInviteInfo().getGroupInfo().getSquareSPicName();
                    acceptInviteInfo.inviteCode = bean.getInviteInfo().getInviteCode();
                    acceptInviteInfo.uId = bean.getInviteInfo().getUserInfo().getId();
                    acceptInviteInfo.uHead = bean.getInviteInfo().getUserInfo().getHeadSPicName();
                    acceptInviteInfo.uNick = Func.formatNickName(baseActivity, bean.getInviteInfo().getUserInfo().getId(), bean.getInviteInfo().getUserInfo().getNickname());
                    acceptInviteInfo.perm = bean.getInviteInfo().getGroupInfo().getPermission();
                    if (isOk()) {
                        show(baseActivity);
                    }
                } else {
                    AppContext.getInstance().handleErrorCode(baseActivity, res.errorCode, res.errorInfo);
                }
            }
        }, inviteCode);
    }

    private void show(final BaseActivity baseActivity) {
        if (isOk()) {
            AppContext.getInstance().initLogin();
            if (AppContext.getInstance().isLogin()) {
                //检查是否已加入group,未加入则弹接受邀请框
                ApiClient.getApi().isJoinedGroup(new ApiCallbackAdapter() {

                    @Override
                    public void onApiSuccess(Result res, String tag) {
                        baseActivity.hideWaitDialog();
                        if (res.isOK()) {
                            IsJoinedGroup isJoinedGroup = (IsJoinedGroup) res;
                            if (isJoinedGroup.isJoined()) {
                                if (acceptInviteInfo.isActivity()) {
                                    AppContext.getInstance().showToast("你已加入该活动");
                                } else {
                                    AppContext.getInstance().showToast("你已加入该群组");
                                }
                            } else {
                                AcceptInviteDialog dialog = new AcceptInviteDialog(baseActivity, acceptInviteInfo);
                                dialog.show();
                            }

                        } else {
                            AppContext.getInstance().handleErrorCode(baseActivity, res.errorCode, res.errorInfo);
                        }
                        acceptInviteInfo = null;
                    }

                    @Override
                    public void onApiStart(String tag) {
                        baseActivity.showWaitDialog();
                    }

                    @Override
                    protected void onApiError(String tag) {
                        super.onApiError(tag);
                        baseActivity.hideWaitDialog();
                        acceptInviteInfo = null;
                        KLog.i("onError:::" + tag);
                    }
                }, AppContext.getInstance().getLoginUid(), acceptInviteInfo.gId);
            } else {
                UIHelper.showLogin(baseActivity, false);
            }
        }
    }

    private boolean isOk() {
        if (acceptInviteInfo == null || TextUtils.isEmpty(acceptInviteInfo.gId) || TextUtils.isEmpty(acceptInviteInfo.uId) || TextUtils.isEmpty(acceptInviteInfo.gType)
                || TextUtils.isEmpty(acceptInviteInfo.inviteCode)) {
            return false;
        }
        return true;
    }

    public class AcceptInviteInfo {
        public String gId;
        public String gType;
        public String gName;
        public String gIntro;
        public String gSquareSPic;
        public String inviteCode;
        public String uId;
        public String uHead;
        public String uNick;
        public String perm;

        public boolean isActivity() {
            return Constant.gType_activity.equals(gType);
        }

    }

}
