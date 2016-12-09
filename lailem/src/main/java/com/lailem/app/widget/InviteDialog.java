package com.lailem.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiCallbackAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.GroupBrief;
import com.lailem.app.share_ex.model.IShareManager;
import com.lailem.app.share_ex.model.ShareContentWebpage;
import com.lailem.app.share_ex.qq.QQShareManager;
import com.lailem.app.share_ex.wechat.WechatShareManager;
import com.lailem.app.share_ex.weibo.WeiboShareManager;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.ClipboardUtil;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.InviteInfoManager;
import com.lailem.app.utils.InviteInfoManager.OnInviteInfoListener;
import com.lailem.app.utils.UIHelper;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteDialog extends Dialog {
    private static final int DEFAULT_THEME = R.style.confirm_dialog;

    private Context context;
    private BaseActivity _activity;

    @Bind(R.id.title)
    TextView title_tv;
    @Bind(R.id.content)
    TextView content_tv;

    private IShareManager wechatShareManager;
    private IShareManager weiboShareManager;
    private IShareManager qqShareManager;
    private SsoHandler mSsoHandler;

    private String title;
    private String content;
    private String dataUrl;
    private String imageUrl;
    private String smsContent;
    private String emailContent;
    private String emailTopic;
    private String weixinMomentTitle;
    private String weiboContent;
    private int type;


    public InviteDialog(Context context, String groupId, String groupName, String picName, final String startTime, final String address, final String intro, int type) {
        super(context, DEFAULT_THEME);
        init(context, groupId, groupName, picName, startTime, address, intro, type);
    }

    private void init(final Context context, final String groupId, final String groupName, final String picName, final String startTime, final String address, final String intro, final int type) {
        this.context = context;
        this._activity = (BaseActivity) context;
        this.type = type;

        if (!AppContext.getInstance().isInitShareReady) {
            _activity.showWaitDialog();
            AppContext.getInstance().initShare();
            _activity.hideWaitDialog();
        }

        wechatShareManager = new WechatShareManager(context);
        weiboShareManager = new WeiboShareManager(context);
        qqShareManager = new QQShareManager(context);

        InviteInfoManager inviteInfo = new InviteInfoManager((BaseActivity) context);
        inviteInfo.setOnInviteInfoListener(new OnInviteInfoListener() {

            @Override
            public void onInviteInfoSuccess(final String inviteUrl) {

                final String nickName = AppContext.getInstance().getProperty(Const.USER_NICKNAME);

                if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(address)) {
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
                                buileShareProperties(inviteUrl, nickName, groupName, picName, brief.getGroupInfo().getStartTime(), brief.getGroupInfo().getAddress(), intro, type);
                                showSelf(context);
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
                    }, ac.getLoginUid(), groupId);
                } else {
                    _activity.hideWaitDialog();
                    buileShareProperties(inviteUrl, nickName, groupName, picName, startTime, address, intro, type);
                    showSelf(context);
                }


            }

            @Override
            public void onInviteInfoFail() {
                dismiss();
                AppContext.showToast("邀请函获取失败！");
            }
        }, groupId);
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

    private void showSelf(Context context) {
        this.context = context;
        this._activity = (BaseActivity) context;
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        w.setAttributes(lp);
        this.setCanceledOnTouchOutside(true);
        View contentView = View.inflate(context, R.layout.dialog_invite, null);
        this.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        if (type == Const.INVITE_TYPE_ACTIVE) {
            title_tv.setText("邀请好友参与活动");
            content_tv.setText("邀请的好友可以通过手机网页参与活动哟！");
        } else if (type == Const.INVITE_TYPE_GROUP) {
            title_tv.setText("邀请好友加入群组");
            content_tv.setText("邀请的好友可以通过手机网页加入群组哟！");
        }
        super.show();
    }

    /**
     * 取消默认显示
     */
    @Override
    public void show() {

    }

    @OnClick(R.id.close)
    public void close() {
        dismiss();
    }

    @OnClick(R.id.wechatMoment)
    public void clickWechatMoment() {
        wechatShareManager.share(new ShareContentWebpage(weixinMomentTitle, weixinMomentTitle, dataUrl,
                imageUrl), WechatShareManager.WEIXIN_SHARE_TYPE_FRENDS);
        dismiss();
    }

    @OnClick(R.id.wechat)
    public void clickWechat() {
        wechatShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                imageUrl), WechatShareManager.WEIXIN_SHARE_TYPE_TALK);
        dismiss();
    }

    @OnClick(R.id.qq)
    public void clickQQ() {
        qqShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                imageUrl), QQShareManager.TALK_SHARE_TYPE);
        dismiss();
    }

    @OnClick(R.id.qzone)
    public void clickQzone() {
        qqShareManager.share(new ShareContentWebpage(title, content, dataUrl,
                imageUrl), QQShareManager.QZONE_SHARE_TYPE);
        dismiss();
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
                    weiboShareManager.share(new ShareContentWebpage(title, weiboContent, dataUrl,
                            file.getAbsolutePath()), WeiboShareManager.WEIBO_SHARE_TYPE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    _activity.hideWaitDialog();
                    AppContext.showToast("图片获取失败");
                }
            });
        } else {
            weiboShareManager.share(new ShareContentWebpage(title, weiboContent, dataUrl,
                    imageUrl), WeiboShareManager.WEIBO_SHARE_TYPE);
        }
        dismiss();
    }

    @OnClick(R.id.sms)
    public void clickSms() {
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        sendIntent.putExtra("sms_body", smsContent);
        sendIntent.setType("vnd.android-dir/mms-sms");
        _activity.startActivity(sendIntent);
        dismiss();
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
        _activity.startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
        dismiss();
    }

    @OnClick(R.id.copyLink)
    public void clickCopyLink() {
        ClipboardUtil.copyText(_activity, dataUrl);
        AppContext.showToast("复制成功");
        dismiss();
    }

    @OnClick(R.id.qrcode)
    public void clickQrCode() {
        UIHelper.showQrCode(_activity, dataUrl, Const.QRCODE_TYPE_INVITE);
        dismiss();
    }

}
