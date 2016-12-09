package com.lailem.app.ui.create_old;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.GroupBrief;
import com.lailem.app.jsonbean.activegroup.InviteCode;
import com.lailem.app.share_ex.model.IShareManager;
import com.lailem.app.share_ex.model.ShareContentWebpage;
import com.lailem.app.share_ex.qq.QQShareManager;
import com.lailem.app.share_ex.wechat.WechatShareManager;
import com.lailem.app.share_ex.weibo.WeiboLoginManager;
import com.lailem.app.share_ex.weibo.WeiboShareManager;
import com.lailem.app.ui.webview.WebViewActivity;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.ClipboardUtil;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.InviteInfoManager;
import com.lailem.app.utils.UIHelper;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteActivity extends BaseActivity {

    public static final String BUNDLE_KEY_INVITE_TYPE = "invite_type";
    public static final String BUNDLE_KEY_INVITE_PICNAME = "pic_name";


    private IShareManager wechatShareManager;
    private IShareManager weiboShareManager;
    private IShareManager qqShareManager;

    private String title;
    private String content;
    private String dataUrl;
    private String imageUrl;
    private String smsContent;
    private String emailContent;
    private String emailTopic;
    private String weixinMomentTitle;
    private String weiboContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        setTranslucentStatus(true, R.color.window_bg);
        ButterKnife.bind(this);

        if(!ac.isInitShareReady){
            showWaitDialog();
            ac.initShare();
            hideWaitDialog();
        }

        wechatShareManager = new WechatShareManager(this);
        weiboShareManager = new WeiboShareManager(this);
        qqShareManager = new QQShareManager(this);

        initInviteInfo();
    }

    /**
     * 初始化邀请函
     */
    private void initInviteInfo() {
        InviteInfoManager inviteInfo = new InviteInfoManager(this);
        inviteInfo.setOnInviteInfoListener(new InviteInfoManager.OnInviteInfoListener() {

            @Override
            public void onInviteInfoSuccess(final String inviteUrl) {
                hideWaitDialog();
                final int type = _Bundle.getInt(BUNDLE_KEY_INVITE_TYPE);
                final String groupName = _Bundle.getString(Const.BUNDLE_KEY_GROUP_NAME);
                final String nickName = ac.getProperty(Const.USER_NICKNAME);
                final String picName = _Bundle.getString(BUNDLE_KEY_INVITE_PICNAME);


                AppContext ac = AppContext.getInstance();
                ApiClient.getApi().groupBrief(new ApiCallbackAdapter() {
                    @Override
                    public void onApiStart(String tag) {
                        super.onApiStart(tag);
                        _activity.showWaitDialog();
                    }

                    @Override
                    public void onApiSuccess(Result res, String tag) {
                        super.onApiSuccess(res, tag);
                        _activity.hideWaitDialog();
                        if (res.isOK()) {
                            GroupBrief brief = (GroupBrief) res;
                            buileShareProperties(inviteUrl, nickName, groupName, picName, brief.getGroupInfo().getStartTime(), brief.getGroupInfo().getAddress(), brief.getGroupInfo().getIntro(), type);
                        } else {
                            AppContext.showToast("活动资料获取失败！");
                        }
                    }

                    @Override
                    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
                        super.onApiFailure(t, errorNo, strMsg, tag);
                        _activity.hideWaitDialog();
                        AppContext.showToast("活动资料获取失败！");
                    }
                }, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));

            }

            @Override
            public void onInviteInfoFail() {
                UIHelper.showMainWithClearTop(_activity);
                AppContext.showToast("获取邀请函失败！");
            }
        }, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));

    }

    @OnClick(R.id.wechatMoment)
    public void clickWechatMoment() {
        wechatShareManager.share(new ShareContentWebpage(content, content, dataUrl,
                imageUrl), WechatShareManager.WEIXIN_SHARE_TYPE_FRENDS);
    }

    @OnClick(R.id.wechat)
    public void clickWechat() {
        wechatShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                imageUrl), WechatShareManager.WEIXIN_SHARE_TYPE_TALK);
    }

    @OnClick(R.id.qq)
    public void clickQQ() {
        qqShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                imageUrl), QQShareManager.TALK_SHARE_TYPE);
    }

    @OnClick(R.id.qzone)
    public void clickQzone() {
        qqShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                imageUrl), QQShareManager.QZONE_SHARE_TYPE);
    }

    @OnClick(R.id.sinaWeibo)
    public void clickSinaWeibo() {
        //如果是图片网址
        if (imageUrl.contains("http")) {
            AppContext.getInstance().imageLoader.loadImage(imageUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    _activity.showWaitDialog();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    _activity.hideWaitDialog();
                    File file = BitmapUtil.saveBitmap(bitmap, _activity);
                    weiboShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                            file.getAbsolutePath()), WeiboShareManager.WEIBO_SHARE_TYPE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    _activity.hideWaitDialog();
                    AppContext.showToast("图片获取失败");
                }
            });
        } else {
            weiboShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                    imageUrl), WeiboShareManager.WEIBO_SHARE_TYPE);
        }
    }

    @OnClick(R.id.sms)
    public void clickSms() {
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        sendIntent.putExtra("sms_body", smsContent);
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
    }

    @OnClick(R.id.email)
    public void clickEmail() {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
        email.setType("plain/text");
        //设置邮件默认地址
        email.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
        //设置邮件默认标题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTopic);
        //设置要默认发送的内容
        email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
        //调用系统的邮件系统
        startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
    }

    @OnClick(R.id.copyLink)
    public void clickCopyLink() {
        UIHelper.showMainWithClearTop(this);
        ClipboardUtil.copyText(this, dataUrl);
        AppContext.showToast("复制成功");
    }

    @OnClick(R.id.qrcode)
    public void clickQrCode() {
        UIHelper.showQrCode(_activity, dataUrl, Const.QRCODE_TYPE_INVITE);
    }

    @OnClick(R.id.submit)
    public void submit() {
        UIHelper.showMainWithClearTop(this);
    }

    @OnClick(R.id.previewInvite)
    public void clickPreviewInvite() {
        ApiClient.getApi().groupInviteCode(new ApiCallbackAdapter() {
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
                    InviteCode inviteCode = (InviteCode) res;
                    String url = Const.URL_INVITE_LETTER + inviteCode.getInviteCode();
                    UIHelper.showWebView(_activity, WebViewActivity.TYPE_INVITE_LEETER, url);
                } else {
                    ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
                }
            }

            @Override
            protected void onApiError(String tag) {
                super.onApiError(tag);
                hideWaitDialog();
            }
        }, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));

    }

    @Override
    public void onBackPressed() {
        UIHelper.showMainWithClearTop(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SsoHandler mSsoHandler = WeiboLoginManager.getSsoHandler();
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    private void buileShareProperties(final String inviteUrl, final String nickName, final String groupName, final String picName, final String startTime, final String address, final String intro, final int type) {
        if (type == Const.INVITE_TYPE_ACTIVE) {
            title = nickName + "邀请你参加活动：" + groupName;
            dataUrl = inviteUrl;
            imageUrl = ApiClient.getFileUrl(picName);
            content = groupName + "，时间：" + startTime + "，地址：" + address;
            smsContent = "我在【来了】上创建了活动：" + groupName + "，时间：" + startTime + "，地址：" + address + "，点按邀请函查看详情。邀请函：" + inviteUrl;
            emailContent = "我在【来了】上创建了活动：" + groupName + "，时间：" + startTime + "，地址：" + address + "，点按邀请函查看详情。邀请函：" + inviteUrl;
            emailTopic = nickName + "邀请你参加活动：" + groupName;
            weixinMomentTitle = nickName + "邀请你参加活动：" + groupName;
            weiboContent = nickName + "邀请你参加活动：" + groupName + "，时间：" + startTime + "，地址：" + address + inviteUrl;

        } else if (type == Const.INVITE_TYPE_GROUP) {
            title = nickName + "邀请你加入群组：" + groupName;
            dataUrl = inviteUrl;
            imageUrl = ApiClient.getFileUrl(picName);
            content = intro;
            smsContent = "我在【来了】上创建了群组：" + groupName + "，点按邀请函查看详情。邀请函：" + inviteUrl;
            emailContent = "我在【来了】上创建了群组：" + groupName + "，点按邀请函查看详情。邀请函：" + inviteUrl;
            emailTopic = nickName + "邀请你加入群组：" + groupName;
            weixinMomentTitle = nickName + "邀请你加入群组：" + groupName;
            weiboContent = nickName + "邀请你加入群组：" + groupName + "，" + intro;
        }
    }
}
