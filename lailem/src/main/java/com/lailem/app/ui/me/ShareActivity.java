package com.lailem.app.ui.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.share_ex.model.IShareManager;
import com.lailem.app.share_ex.model.ShareContentWebpage;
import com.lailem.app.share_ex.qq.QQShareManager;
import com.lailem.app.share_ex.wechat.WechatShareManager;
import com.lailem.app.share_ex.weibo.WeiboLoginManager;
import com.lailem.app.share_ex.weibo.WeiboShareManager;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {
    public static final String BUNDLE_KEY_SHARE_TITLE = "title";
    public static final String BUNDLE_KEY_SHARE_CONTENT = "content";
    public static final String BUNDLE_KEY_SHARE_DATAURL = "dataUrl";
    public static final String BUNDLE_KEY_SHARE_IMAGEURL = "imageUrl";
    public static final String BUNDLE_KEY_SHARE_SMS_CONTENT = "sms_content";
    public static final String BUNDLE_KEY_SHARE_EMAIL_CONTENT = "email_content";
    public static final String BUNDLE_KEY_SHARE_EMAIL_TOPIC = "email_topic";
    public static final String BUNDLE_KEY_SHARE_WEIXINMOMENT_TITLE = "weixinmoment_title";
    public static final String BUNDLE_KEY_SHARE_WEIBO_CONTENT = "weibo_content";

    @Bind(R.id.topbar)
    TopBarView topbar;

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
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initView();

        if (!ac.isInitShareReady) {
            showWaitDialog();
            ac.initShare();
            hideWaitDialog();
        }

        wechatShareManager = new WechatShareManager(this);
        weiboShareManager = new WeiboShareManager(this);
        qqShareManager = new QQShareManager(this);

        title = _Bundle.getString(BUNDLE_KEY_SHARE_TITLE);
        content = _Bundle.getString(BUNDLE_KEY_SHARE_CONTENT);
        dataUrl = _Bundle.getString(BUNDLE_KEY_SHARE_DATAURL);
        imageUrl = _Bundle.getString(BUNDLE_KEY_SHARE_IMAGEURL);
        smsContent = _Bundle.getString(BUNDLE_KEY_SHARE_SMS_CONTENT);
        emailContent = _Bundle.getString(BUNDLE_KEY_SHARE_EMAIL_CONTENT);
        emailTopic = _Bundle.getString(BUNDLE_KEY_SHARE_EMAIL_TOPIC);
        weixinMomentTitle = _Bundle.getString(BUNDLE_KEY_SHARE_WEIXINMOMENT_TITLE);
        weiboContent = _Bundle.getString(BUNDLE_KEY_SHARE_WEIBO_CONTENT);

    }

    private void initView() {
        topbar.setTitle("分享给好友").setLeftText("取消", UIHelper.finish(this));
    }

    @OnClick(R.id.wechatMoment)
    public void clickWechatMoment() {
        wechatShareManager.share(new ShareContentWebpage(weixinMomentTitle, weixinMomentTitle, dataUrl,
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
                    showWaitDialog();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    hideWaitDialog();
                    File file = BitmapUtil.saveBitmap(bitmap, _activity);
                    weiboShareManager.share(new ShareContentWebpage(title, weiboContent, dataUrl,
                            file.getAbsolutePath()), WeiboShareManager.WEIBO_SHARE_TYPE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    hideWaitDialog();
                    AppContext.showToast("图片获取失败");
                }
            });
        } else {
            weiboShareManager.share(new ShareContentWebpage(title, weiboContent, dataUrl,
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

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SsoHandler mSsoHandler = WeiboLoginManager.getSsoHandler();
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
