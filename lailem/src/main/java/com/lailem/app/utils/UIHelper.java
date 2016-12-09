package com.lailem.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.AppManager;
import com.lailem.app.AppStart;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter.ImageBean;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.User;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.personal.UserBean;
import com.lailem.app.service.UpdateService;
import com.lailem.app.ui.active.ActiveDetailActivity;
import com.lailem.app.ui.active.ActiveDynamicListActivity;
import com.lailem.app.ui.active.ActiveInfoDetailActivity;
import com.lailem.app.ui.active.ActiveLocActivity;
import com.lailem.app.ui.active.ActiveMemberListActivity;
import com.lailem.app.ui.active.ActiveNearListActivity;
import com.lailem.app.ui.active.ActiveSettingActivity;
import com.lailem.app.ui.active.ApplyActiveTextActivity;
import com.lailem.app.ui.active.CreateVoteActiveActivity;
import com.lailem.app.ui.active.GroupActiveListActivity;
import com.lailem.app.ui.active.VoteActiveDetailActivity;
import com.lailem.app.ui.chat.ChatActivity;
import com.lailem.app.ui.chat.NotificationListActivity;
import com.lailem.app.ui.common.ComplainActivity;
import com.lailem.app.ui.common.CrashActivity;
import com.lailem.app.ui.common.ImageViewerActivity;
import com.lailem.app.ui.common.QrCodeActivity;
import com.lailem.app.ui.common.VerifyWayForActiveActivity;
import com.lailem.app.ui.common.VerifyWayForGroupActivity;
import com.lailem.app.ui.create.NewCreateDynamicActivity;
import com.lailem.app.ui.create_old.CreateActiveActivity;
import com.lailem.app.ui.create_old.CreateActiveTypeActivity;
import com.lailem.app.ui.create_old.CreateGroupActivity;
import com.lailem.app.ui.create_old.CreateNameActiveOrGroupActivity;
import com.lailem.app.ui.create_old.CreateNoticeActivity;
import com.lailem.app.ui.create_old.CreateTypeActivity;
import com.lailem.app.ui.create_old.InviteActivity;
import com.lailem.app.ui.create_old.MaterialListActivity;
import com.lailem.app.ui.create_old.MaterialListTwoActivity;
import com.lailem.app.ui.create_old.dynamic.ChooseVoteSelectCountActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicPreviewActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicScheduleActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicTextActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicVoiceActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicVoteActivity;
import com.lailem.app.ui.create_old.dynamic.ScheduleActivity;
import com.lailem.app.ui.create_old.dynamic.model.ScheduleModel;
import com.lailem.app.ui.databank.DataBankActivity;
import com.lailem.app.ui.dynamic.DynamicDetailActivity;
import com.lailem.app.ui.group.ApplyGroupTextActivity;
import com.lailem.app.ui.group.ApplyGroupVoiceActivity;
import com.lailem.app.ui.group.GroupHomeActivity;
import com.lailem.app.ui.group.GroupInfoActivity;
import com.lailem.app.ui.group.GroupInfoForChatActivity;
import com.lailem.app.ui.group.GroupInfoForCreatorActivity;
import com.lailem.app.ui.group.GroupMemberListActivity;
import com.lailem.app.ui.group.GroupNearListActivity;
import com.lailem.app.ui.group.GroupNotificationActivity;
import com.lailem.app.ui.group.GroupScheduleListActivity;
import com.lailem.app.ui.group.GroupTagsActivity;
import com.lailem.app.ui.group.MemberInfoActivity;
import com.lailem.app.ui.group.MemberInfoAloneActivity;
import com.lailem.app.ui.group.MemberInfoForChatActivity;
import com.lailem.app.ui.group.ModifyGroupIntroActivity;
import com.lailem.app.ui.group.ModifyGroupNameActivity;
import com.lailem.app.ui.group.ModifyMemberRemarkActivity;
import com.lailem.app.ui.group.RemindTypeActivity;
import com.lailem.app.ui.group.SelectGroupTagsActivity;
import com.lailem.app.ui.group.SelectLocActivity;
import com.lailem.app.ui.main.MainActivity;
import com.lailem.app.ui.me.AboutActivity;
import com.lailem.app.ui.me.BlackListActivity;
import com.lailem.app.ui.me.ChooseCityOneActivity;
import com.lailem.app.ui.me.ChooseCityTwoActivity;
import com.lailem.app.ui.me.ContactsActivity;
import com.lailem.app.ui.me.DraftBoxListActivity;
import com.lailem.app.ui.me.FeedbackActivity;
import com.lailem.app.ui.me.FeedbackTypeOneActivity;
import com.lailem.app.ui.me.FeedbackTypeTwoActivity;
import com.lailem.app.ui.me.MeDynamicActivity;
import com.lailem.app.ui.me.MeFavoriteListActivity;
import com.lailem.app.ui.me.MeMessageListActivity;
import com.lailem.app.ui.me.ModifyActiveIdActivity;
import com.lailem.app.ui.me.ModifyAgeActivity;
import com.lailem.app.ui.me.ModifyNameActivity;
import com.lailem.app.ui.me.ModifySexActivity;
import com.lailem.app.ui.me.ModifySignActivity;
import com.lailem.app.ui.me.SettingActivity;
import com.lailem.app.ui.me.ShareActivity;
import com.lailem.app.ui.me.UserInfoActivity;
import com.lailem.app.ui.qupai.VideoPlayActivity;
import com.lailem.app.ui.qupai.VideoRecordActivity;
import com.lailem.app.ui.user.BindPhoneActivity;
import com.lailem.app.ui.user.EditProfileActivity;
import com.lailem.app.ui.user.FindPwdActivity;
import com.lailem.app.ui.user.LoginActivity;
import com.lailem.app.ui.user.ModifyPwdActivity;
import com.lailem.app.ui.user.RegisterAvatarActivity;
import com.lailem.app.ui.user.RegisterInfoActivity;
import com.lailem.app.ui.user.RegisterVerifyActivity;
import com.lailem.app.ui.user.ThirdLoginActivity;
import com.lailem.app.ui.webview.WebViewActivity;
import com.lailem.app.zxing.CaptureActivity;

import java.util.ArrayList;

//import com.lailem.app.ui.video.play.PlayVideoActivity;
//import com.lailem.app.ui.video.record.MediaRecorderActivity;

/**
 * 界面帮助类
 *
 * @author XuYang
 */
public class UIHelper {

    /**
     * 打开系统中的浏览器
     *
     * @param context
     * @param url
     */
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    /**
     * 发送App异常崩溃报告
     *
     * @param context
     */
    public static void sendAppCrashReport(final Context context) {

        DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 退出
                System.exit(-1);
            }
        }).show();
    }

    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    AppContext.showToastShort("缓存清除成功");
                } else {
                    AppContext.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 点击返回监听事件
     */
    public static View.OnClickListener finish(final Activity activity) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        };
    }

    /**
     * 显示启动页面
     *
     * @param context
     */
    public static void showAppStart(Context context, boolean isClearTask) {
        Intent intent = new Intent(context, AppStart.class);
        if (isClearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 显示登录页面
     *
     * @param context
     */
    public static void showLogin(Context context, boolean isClearTask) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        if (isClearTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 显示第三方登录页面
     *
     * @param context
     */
    public static void showLoginThird(Context context) {
        Intent intent = new Intent(context, ThirdLoginActivity.class);
        ((Activity) context).startActivityForResult(intent, LoginActivity.REQUEST_LOGIN_THIRD);
    }


    public static void showLoginThird(Context context, int type) {
        Intent intent = new Intent(context, ThirdLoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ThirdLoginActivity.LOGIN_TYPE, type);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, LoginActivity.REQUEST_LOGIN_THIRD);
    }

    /**
     * 显示主页面
     *
     * @param context
     */
    public static void showMain(Context context, boolean isClearTask) {
        Intent intent = new Intent(context, MainActivity.class);

        if (isClearTask) {
            AppManager.getAppManager().finishAllActivity();
        }
        context.startActivity(intent);
    }

    /**
     * 显示主页面
     *
     * @param context
     */
    public static void showMain(Context context, boolean isClearTask, boolean fromAppStart) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.BUNDLE_KEY_FROM_APPSTART, fromAppStart);

        if (isClearTask) {
            AppManager.getAppManager().finishAllActivity();
        }
        context.startActivity(intent);
    }

    /**
     * 显示主页面clear_top
     *
     * @param context
     */
    public static void showMainWithClearTop(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 显示忘记密码页面
     *
     * @param context
     */
    public static void showForgetPwd(Context context) {
        Intent intent = new Intent(context, FindPwdActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示修改密码页面
     *
     * @param context
     */
    public static void showModifyPwd(Context context) {
        Intent intent = new Intent(context, ModifyPwdActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示注册之后验证手机页面
     *
     * @param context
     * @param pwd
     * @param phone
     */
    public static void showRegisterVerify(Context context, String phone, String pwd, String transId) {
        Intent intent = new Intent(context, RegisterVerifyActivity.class);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PHONE, phone);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PASSWORD, pwd);
        intent.putExtra(LoginActivity.BUNDLE_KEY_TRANSID, transId);
        context.startActivity(intent);
    }

    /**
     * 显示注册之后完善个人资料页面
     *
     * @param context
     * @param validCode
     * @param pwd
     * @param phone
     */
    public static void showRegisterInfo(Context context, String phone, String pwd, String validCode, String transId) {
        Intent intent = new Intent(context, RegisterInfoActivity.class);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PHONE, phone);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PASSWORD, pwd);
        intent.putExtra(LoginActivity.BUNDLE_KEY_VALIDCODE, validCode);
        intent.putExtra(LoginActivity.BUNDLE_KEY_TRANSID, transId);
        context.startActivity(intent);
    }

    /**
     * 显示注册之后上传头像页面
     *
     * @param context
     */
    public static void showRegisterAvatar(Context context, String phone, String pwd, String validCode, String transId, String nickName, String birthday, String sex, String provinceId, String cityId) {
        Intent intent = new Intent(context, RegisterAvatarActivity.class);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PHONE, phone);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PASSWORD, pwd);
        intent.putExtra(LoginActivity.BUNDLE_KEY_VALIDCODE, validCode);
        intent.putExtra(LoginActivity.BUNDLE_KEY_TRANSID, transId);
        intent.putExtra(LoginActivity.BUNDLE_KEY_NICKNAME, nickName);
        intent.putExtra(LoginActivity.BUNDLE_KEY_BIRTHDAY, birthday);
        intent.putExtra(LoginActivity.BUNDLE_KEY_SEX, sex);
        intent.putExtra(LoginActivity.BUNDLE_KEY_PROVINCEID, provinceId);
        intent.putExtra(LoginActivity.BUNDLE_KEY_CITYID, cityId);
        context.startActivity(intent);
    }

    /**
     * 显示个人信息页面
     *
     * @param context
     */
    public static void showUserInfo(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示个人信息-修改姓名页面
     *
     * @param context
     */
    public static void showModifyName(Context context, String name) {
        Intent intent = new Intent(context, ModifyNameActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ModifyNameActivity.BUNDLE_KEY_NAME, name);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, UserInfoActivity.REQUEST_NAME);
    }

    /**
     * 显示个人信息-修改活动号页面
     *
     * @param context
     */
    public static void showModifyActiveId(Context context, String activeId) {
        Intent intent = new Intent(context, ModifyActiveIdActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ModifyActiveIdActivity.BUNDLE_KEY_ACTIVEID, activeId);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, UserInfoActivity.REQUEST_ACTIVEID);
    }

    /**
     * 显示个人信息-修改性别页面
     *
     * @param context
     */
    public static void showModifySex(Context context, String sex) {
        Intent intent = new Intent(context, ModifySexActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ModifySexActivity.BUNDLE_KEY_SEX, sex);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, UserInfoActivity.REQUEST_SEX);
    }

    /**
     * 显示个人信息-修改年龄
     *
     * @param context
     */
    public static void showModifyAge(Context context, String birthday) {
        Intent intent = new Intent(context, ModifyAgeActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ModifyAgeActivity.BUNDLE_KEY_BIRTHDAY, birthday);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, UserInfoActivity.REQUEST_SEX);
    }

    /**
     * 显示个人信息-选择城市1页面
     *
     * @param context
     */
    public static void showChooseCityOne(Context context, String cityName) {
        Intent intent = new Intent(context, ChooseCityOneActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ChooseCityTwoActivity.BUNDLE_KEY_CITYNAME, cityName);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, UserInfoActivity.REQUEST_ADDRESS);
    }

    /**
     * 显示个人信息-选择城市2页面
     *
     * @param context
     */
    public static void showChooseCityTwo(Context context, String provinceName, String provinceId, String cityName) {
        Intent intent = new Intent(context, ChooseCityTwoActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ChooseCityTwoActivity.BUNDLE_KEY_PROVINCENAME, provinceName);
        extras.putString(ChooseCityTwoActivity.BUNDLE_KEY_PROVINCEID, provinceId);
        extras.putString(ChooseCityTwoActivity.BUNDLE_KEY_CITYNAME, cityName);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, ChooseCityOneActivity.REQUEST_CITY);
    }

    /**
     * 显示个人信息-修改个性签名页面
     *
     * @param context
     */
    public static void showModifySign(Context context, String sign) {
        Intent intent = new Intent(context, ModifySignActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ModifySignActivity.BUNDLE_KEY_SIGN, sign);
        intent.putExtras(extras);
        ((Activity) context).startActivityForResult(intent, UserInfoActivity.REQUEST_SIGN);
    }

    /**
     * 显示联系人页面
     *
     * @param context
     */
    public static void showContactList(Context context) {
        Intent intent = new Intent(context, ContactsActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示消息列表
     *
     * @param context
     */
    public static void showMessageList(Context context) {
        Intent intent = new Intent(context, MeMessageListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示我的收藏页面
     *
     * @param context
     */
    public static void showFavoriteList(Context context) {
        Intent intent = new Intent(context, MeFavoriteListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示设置页面
     *
     * @param context
     */
    public static void showSetting(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示黑名单页面
     *
     * @param context
     */
    public static void showBlackList(Context context) {
        Intent intent = new Intent(context, BlackListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示关于我们页面
     *
     * @param context
     */
    public static void showAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示意见反馈页面
     *
     * @param context
     */
    public static void showFeedback(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示反馈类型1页面
     *
     * @param context
     */
    public static void showFeedbackTypeOne(Context context) {
        Intent intent = new Intent(context, FeedbackTypeOneActivity.class);
        ((Activity) context).startActivityForResult(intent, FeedbackActivity.REQUEST_TYPE);
    }

    /**
     * 显示反馈类型2页面
     *
     * @param context
     * @param id
     */
    public static void showFeedbackTypeTwo(Context context, String id) {
        Intent intent = new Intent(context, FeedbackTypeTwoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FeedbackTypeTwoActivity.BUNDLE_KEY_ID, id);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, FeedbackTypeOneActivity.REQUEST_SPECIAL);
    }


    /**
     * 显示分享页面
     *
     * @param context
     */
    @Deprecated
    public static void showShare(Context context, String title, String content, String dataUrl, String imageUrl, String smsContent, String emailContent, String emailTopic) {
        Intent intent = new Intent(context, ShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_TITLE, title);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_CONTENT, content);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_DATAURL, dataUrl);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_IMAGEURL, imageUrl);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_SMS_CONTENT, smsContent);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_EMAIL_CONTENT, emailContent);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_EMAIL_TOPIC, emailTopic);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 显示分享页面
     *
     * @param context
     */
    public static void showShare(Context context, String title, String content, String dataUrl, String imageUrl, String smsContent, String emailContent, String emailTopic, String weixinMomentTitle, String weiboContent) {
        Intent intent = new Intent(context, ShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_TITLE, title);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_CONTENT, content);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_DATAURL, dataUrl);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_IMAGEURL, imageUrl);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_SMS_CONTENT, smsContent);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_EMAIL_CONTENT, emailContent);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_EMAIL_TOPIC, emailTopic);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_WEIXINMOMENT_TITLE, weixinMomentTitle);
        bundle.putString(ShareActivity.BUNDLE_KEY_SHARE_WEIBO_CONTENT, weiboContent);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 查看图片
     *
     * @param context
     * @param beans
     * @param startIndex
     */
    public static void showImages(Context context, ArrayList<ImageBean> beans, int startIndex) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImageViewerActivity.BUNDLE_KEY_IMAGE_BEANS, beans);
        bundle.putInt(ImageViewerActivity.BUNDLE_KEY_START_INDEX, startIndex);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ImageViewerActivity.REQUEST_CODE_IMAGE_VIEWER);
    }

    /**
     * 查看图片
     *
     * @param context
     * @param beans
     * @param startIndex
     * @param isCheckMode
     */
    public static void showImages(Context context, ArrayList<ImageBean> beans, int startIndex, boolean isCheckMode) {
        Intent intent = new Intent(context, ImageViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImageViewerActivity.BUNDLE_KEY_IMAGE_BEANS, beans);
        bundle.putInt(ImageViewerActivity.BUNDLE_KEY_START_INDEX, startIndex);
        bundle.putBoolean(ImageViewerActivity.BUNDLE_KEY_IS_CHECK_MODE, isCheckMode);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ImageViewerActivity.REQUEST_CODE_IMAGE_VIEWER);
    }

    /**
     * 显示附近活动列表页面
     */
    public static void showAroundActiveList(Context context) {
        Intent intent = new Intent(context, ActiveNearListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    /**
     * 显示附近群组筛选条件页面
     */
    public static void showGroupTags(Context context) {
        Intent intent = new Intent(context, GroupTagsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示选择群标签页面
     */
    public static void showSelectGroupTags(Context context, ArrayList<String> tagNames) {
        Intent intent = new Intent(context, SelectGroupTagsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SelectGroupTagsActivity.BUNDLE_KEY_TAG_NAMES, tagNames);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, SelectGroupTagsActivity.REQUEST_SELECT_TAGS);
    }

    /**
     * 显示附近群组列表页面
     *
     * @param tagName
     * @param tagId
     */
    public static void showAroundGroupList(Context context, String tagId, String tagName) {
        Intent intent = new Intent(context, GroupNearListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GroupTagsActivity.BUNDLE_KEY_TAG_ID, tagId);
        bundle.putString(GroupTagsActivity.BUNDLE_KEY_TAG_NAME, tagName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示活动详情页面
     */
    public static ObjectWrapper showActiveDetail(Context context, String groupId) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ActiveDetailActivity.REQUEST_CODE);
        return null;
    }

    /**
     * 显示活动详情页面
     */
    public static void showActiveDetailWithComment(Context context, String groupId, Comment comment) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putSerializable(ActiveDetailActivity.BUNDLE_KEY_COMMENT, comment);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ActiveDetailActivity.REQUEST_CODE);
    }

    /**
     * 显示活动详情页面
     */
    public static void showActiveDetail(Context context, String groupId, boolean isNoAnimation) {
        Intent intent = new Intent(context, ActiveDetailActivity.class);
        if (isNoAnimation) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ActiveDetailActivity.REQUEST_CODE);
    }

    /**
     * 显示投票活动详情页面
     */
    public static void showVoteActiveDetail(Context context, String groupId) {
        Intent intent = new Intent(context, VoteActiveDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, VoteActiveDetailActivity.REQUEST_CODE);
    }

    /**
     * 显示投票活动详情页面
     */
    public static void showVoteActiveDetail(Context context, String groupId, boolean isNoAnimation) {
        Intent intent = new Intent(context, VoteActiveDetailActivity.class);
        if (isNoAnimation) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).startActivityForResult(intent, VoteActiveDetailActivity.REQUEST_CODE);
    }

    /**
     * 显示邀请好友页面
     *
     * @param context
     */
    public static void showInvite(Context context, String groupName, String picName, int type, String groupId) {
        Intent intent = new Intent(context, InviteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_NAME, groupName);
        bundle.putString(InviteActivity.BUNDLE_KEY_INVITE_PICNAME, picName);
        bundle.putInt(InviteActivity.BUNDLE_KEY_INVITE_TYPE, type);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateTypeActivity.REQUEST_NEXT);
    }

    /**
     * 显示创建群组-群设置界面
     *
     * @param context
     */
    public static void showCreateGroup(Context context, String groupType, String typeId, String typeName, String picLocal, String picMaterialId, String name) {
        Intent intent = new Intent(context, CreateGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_GROUP_TYPE, groupType);
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID, typeId);
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_NAME, typeName);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_LOCAL, picLocal);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_MATERIALID, picMaterialId);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME, name);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateTypeActivity.REQUEST_NEXT);
    }

    /**
     * 显示创建活动-活动设置界面
     *
     * @param context
     */
    public static void showCreateActive(Context context, String groupType, String typeId, String typeName, String picLocal, String picMaterialId, String name, String parentId) {
        Intent intent = new Intent(context, CreateActiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_GROUP_TYPE, groupType);
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID, typeId);
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_NAME, typeName);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, parentId);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_LOCAL, picLocal);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_MATERIALID, picMaterialId);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME, name);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateTypeActivity.REQUEST_NEXT);
    }

    /**
     * 显示素材中心页面
     *
     * @param context
     */
    public static void showMaterialList(Context context, String checkedPicId) {
        Intent intent = new Intent(context, MaterialListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_ID, checkedPicId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, MaterialListActivity.REQUEST_CODE_MATERIAL_TYPE);
    }

    /**
     * 显示素材中心页面2
     *
     * @param context
     */
    public static void showMaterialListTwo(Context context, String id, String checkedPicId) {
        Intent intent = new Intent(context, MaterialListTwoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_TYPE_ID, id);
        bundle.putString(MaterialListTwoActivity.BUNDLE_KEY_CHECKED_PIC_ID, checkedPicId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, MaterialListTwoActivity.REQUEST_CODE_MATERIAL_PIC);
    }

    /**
     * 显示创建活动/创建群组选择类型页面
     *
     * @param context
     */
    public static void showCreateType(Context context, int index) {
        Intent intent = new Intent(context, CreateTypeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CreateTypeActivity.BUNDLE_INDEX, index);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateTypeActivity.REQUEST_CREATE_TYPE);
        ((Activity) context).overridePendingTransition(R.anim.activity_open, 0);
    }

    /**
     * 显示创建群组或者活动-编辑名字
     *
     * @param context
     */
    public static void showCreateNameActiveOrGroup(Context context, String groupType, String typeId, String typeName, String parentId) {
        Intent intent = new Intent(context, CreateNameActiveOrGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_GROUP_TYPE, groupType);
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID, typeId);
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_NAME, typeName);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, parentId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateTypeActivity.REQUEST_NEXT);
    }

    /**
     * 显示群组主页
     *
     * @param context
     */
    public static void showGroupHome(Context context, String groupId) {
        Intent intent = new Intent(context, GroupHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, GroupHomeActivity.REQUEST_CODE);
    }

    /**
     * 显示群组主页
     *
     * @param context
     */
    public static void showGroupHome(Context context, String groupId, boolean isNoAnimation) {
        Intent intent = new Intent(context, GroupHomeActivity.class);
        if (isNoAnimation) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, GroupHomeActivity.REQUEST_CODE);
    }

    /**
     * 显示群二维码
     *
     * @param context
     */
    public static void showQrCode(Context context, String title, String content, String imageUrl, String groupId, int type) {
        Intent intent = new Intent(context, QrCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(QrCodeActivity.BUNDLE_KEY_TITLE, title);
        bundle.putString(QrCodeActivity.BUNDLE_KEY_CONTENT, content);
        bundle.putString(QrCodeActivity.BUNDLE_KEY_IMAGEURL, imageUrl);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putInt(QrCodeActivity.BUNDLE_KEY_TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转到二维码页面
     *
     * @param context
     * @param inviteUrl
     * @param type
     */
    public static void showQrCode(Context context, String inviteUrl, int type) {
        Intent intent = new Intent(context, QrCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(QrCodeActivity.BUNDLE_INVITE_URL, inviteUrl);
        bundle.putInt(QrCodeActivity.BUNDLE_KEY_TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示发表通知页面
     *
     * @param context
     * @param groupId
     */
    public static void showCreateNotice(Context context, String groupId) {
        Intent intent = new Intent(context, CreateNoticeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示资料库页面
     *
     * @param context
     * @param groupId
     */
    public static void showDataBank(Context context, String groupId) {
        Intent intent = new Intent(context, DataBankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示聊天窗口:会话列表进入时调用
     *
     * @param context
     * @param tId     用户id或群id
     */
    public static void showChat(Context context, String tId, String conversationId, String cType) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tId", tId);
        bundle.putString("conversationId", conversationId);
        bundle.putString("cType", cType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示聊天窗口：其它页面进入时调用
     *
     * @param context
     * @param tId     用户id或群id
     * @param name    用户昵称或群名字
     * @param image   用户小头像或群方形图片文件名
     * @param cType   聊天类型
     */
    public static void showChat(Context context, String tId, String name, String image, String cType) {
        // 第一次发起聊天时，用户缓存可能没有信息
        if (Constant.cType_sc.equals(cType)) {
            User user = new User();
            user.setUserId(tId);
            user.setNickname(name);
            user.setHead(image);
            UserCache.getInstance(context).put(user);
        }
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tId", tId);
        bundle.putString("conversationId", null);
        bundle.putString("cType", cType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示群资料
     *
     * @param context
     */
    public static void showGroupInfo(Context context, String groupId) {
        Intent intent = new Intent(context, GroupInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, GroupHomeActivity.REQUEST_GROUP_INFO);
    }

    /**
     * 显示群资料编辑
     *
     * @param context
     * @param groupId
     */
    public static void showGroupInfoForCreator(Context context, String groupId) {
        Intent intent = new Intent(context, GroupInfoForCreatorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, GroupInfoForCreatorActivity.REQUEST_INFO_EDIT);
    }

    /**
     * 显示群成员信息
     *
     * @param context
     * @param roleType
     */
    public static void showMemberInfo(Context context, String groupName, String groupId, String memberId, String roleType, String myRoleType) {
        Intent intent = new Intent(context, MemberInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_NAME, groupName);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putString(MemberInfoActivity.BUNDLE_KEY_ID, memberId);
        bundle.putString(MemberInfoActivity.BUNDLE_KEY_ROLE_TYPE, roleType);
        bundle.putString(MemberInfoActivity.BUNDLE_KEY_MY_ROLE_TYPE, myRoleType);
        intent.putExtras(bundle);
        ((Activity) (context)).startActivityForResult(intent, MemberInfoActivity.REQUEST_CODE);
    }

    /**
     * 显示成员资料页面
     *
     * @param context
     * @param userId
     */
    public static void showMemberInfoAlone(Context context, String userId) {
        Intent intent = new Intent(context, MemberInfoAloneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MemberInfoAloneActivity.BUNDLE_KEY_ID, userId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示单聊个人信息
     *
     * @param context
     */
    public static void showMemberInfoForChat(Context context, String memberId, String conversationId) {
        Intent intent = new Intent(context, MemberInfoForChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MemberInfoForChatActivity.BUNDLE_KEY_ID, memberId);
        bundle.putString("conversationId", conversationId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示群成员
     *
     * @param context
     */
    public static void showGroupMemberList(Context context, String groupName, String groupId, String roleType) {
        Intent intent = new Intent(context, GroupMemberListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_NAME, groupName);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putString(GroupMemberListActivity.BUNDLE_KEY_ROLETYPE, roleType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示群成员
     *
     * @param context
     */
    public static void showActiveMemberList(Context context, String groupName, String groupId, String roleType, String joinNeedContact) {
        Intent intent = new Intent(context, ActiveMemberListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_NAME, groupName);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putString(ActiveMemberListActivity.BUNDLE_KEY_ROLETYPE, roleType);
        bundle.putString(ActiveMemberListActivity.BUNDLE_KEY_JOIN_NEED_CONTACT, joinNeedContact);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示群组活动列表
     *
     * @param context
     * @param groupId
     */
    public static void showGroupActiveList(Context context, String groupId, boolean isShowAdd) {
        Intent intent = new Intent(context, GroupActiveListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putBoolean(GroupActiveListActivity.BUNDLE_KEY_SHOW_ADD, isShowAdd);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示群组活动列表
     *
     * @param context
     */
    public static void showGroupInfoForChat(Context context, String groupId, String conversationId) {
        Intent intent = new Intent(context, GroupInfoForChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putString("conversationId", conversationId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 活动通知
     *
     * @param context
     */
    public static void showGroupNoticeList(Context context) {
        Intent intent = new Intent(context, MeMessageListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 通知类消息：包含群/活动通知、群申请、系统通知等
     *
     * @param context
     */
    public static void showNotificationList(Context context, String tId, String conversationId, String cType) {
        Intent intent = new Intent(context, NotificationListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tId", tId);
        bundle.putString("conversationId", conversationId);
        bundle.putString("cType", cType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示选择位置
     *
     * @param context
     */
    public static void showSelectLoc(Context context) {
        Intent intent = new Intent(context, SelectLocActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, SelectLocActivity.REQUEST_SELECT_LOC);
    }


    /**
     * 显示位置地图页面
     *
     * @param context
     * @param lat
     * @param lon
     * @param address
     */
    public static void showActiveLoc(Context context, String lat, String lon, String address, int type) {
        Intent intent = new Intent(context, ActiveLocActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ActiveLocActivity.BUNDLE_KEY_LAT, lat);
        bundle.putString(ActiveLocActivity.BUNDLE_KEY_LON, lon);
        bundle.putString(ActiveLocActivity.BUNDLE_KEY_ADDRESS, address);
        bundle.putInt(ActiveLocActivity.BUNDLE_KEY_TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示通知提醒选择页面
     *
     * @param context
     */
    public static void showRemindType(Context context) {
        Intent intent = new Intent(context, RemindTypeActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示活动日程页面
     *
     * @param context
     */
    public static void showSchedule(Context context, ScheduleModel model) {
        Intent intent = new Intent(context, ScheduleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ScheduleActivity.BUNDLE_KEY_SCHEDULE, model);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示活动日程列表
     *
     * @param context
     */
    public static void showGroupScheduleList(Context context, String groupId, boolean isShowAdd) {
        Intent intent = new Intent(context, GroupScheduleListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putBoolean(GroupScheduleListActivity.BUNDLE_KEY_SHOW_ADD, isShowAdd);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示创建群动态或者活动动态页面
     *
     * @param context
     * @param groupId
     * @param type
     */
    public static void showCreateDyanmic(Context context, String groupId, int type) {
        Intent intent = new Intent(context, CreateDynamicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putInt(CreateDynamicActivity.BUNDLE_KEY_TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showNewCreateDynamic(Context context, String groupId) {
        Intent intent = new Intent(context, NewCreateDynamicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示发表文本页面
     *
     * @param context
     */
    public static void showCreateDynamicText(Context context) {
        Intent intent = new Intent(context, CreateDynamicTextActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateDynamicActivity.REQUEST_TEXT);
    }

    /**
     * 显示发表语音页面
     *
     * @param context
     */
    public static void showCreateDynamicVoice(Context context) {
        Intent intent = new Intent(context, CreateDynamicVoiceActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateDynamicVoiceActivity.REQUEST_VOICE);
    }

    /**
     * 显示选择位置页面
     *
     * @param context
     */
    public static void showCreateDynamicAddress(Context context) {
        Intent intent = new Intent(context, SelectLocActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, SelectLocActivity.REQUEST_SELECT_LOC);
    }

    /**
     * 显示创建动态-日程页面
     *
     * @param context
     */
    public static void showCreateDynamicSchedule(Context context) {
        Intent intent = new Intent(context, CreateDynamicScheduleActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateDynamicActivity.REQUEST_SCHEDULE);
    }

    /**
     * 显示创建日程页面
     *
     * @param context
     */
    public static void showCreateDynamicSchedule(Context context, String groupId, boolean isDirectPublish) {
        Intent intent = new Intent(context, CreateDynamicScheduleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putBoolean(CreateDynamicScheduleActivity.BUNDLE_KEY_DIRECT_PUBLISH, isDirectPublish);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 显示创建动态-投票页面
     *
     * @param context
     */
    public static void showCreateDynamicVote(Context context) {
        Intent intent = new Intent(context, CreateDynamicVoteActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateDynamicActivity.REQUEST_VOTE);
    }

    /**
     * 创建投票页面
     *
     * @param context
     */
    public static void showCreateDynamicVote(Context context, String groupId, boolean isDirectPublish) {
        Intent intent = new Intent(context, CreateDynamicVoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putBoolean(CreateDynamicScheduleActivity.BUNDLE_KEY_DIRECT_PUBLISH, isDirectPublish);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示发表动态-预览页面
     *
     * @param context
     * @param models
     */
    public static void showCreateDynamicPreview(Context context, ArrayList<Object> models) {
        Intent intent = new Intent(context, CreateDynamicPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CreateDynamicActivity.BUNDLE_KEY_DYNAMICS, models);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示设置投票多选数页面
     *
     * @param context
     * @param selectCount
     * @param count
     */
    public static void showChooseVoteSelectCount(Context context, int count, int selectCount) {
        Intent intent = new Intent(context, ChooseVoteSelectCountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CreateDynamicVoteActivity.BUNDLE_KEY_COUNT, count);
        bundle.putInt(CreateDynamicVoteActivity.BUNDLE_KEY_SELECTCOUNT, selectCount);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateDynamicVoteActivity.REQUEST_SELECT_COUNT);
    }

    /**
     * 显示应用奔溃页面
     *
     * @param context
     */
    public static void showCrash(Context context) {
        Intent intent = new Intent(context, CrashActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示视频录制
     *
     * @param context
     */
    @Deprecated
    public static void showMediaRecord(Activity context) {
//		Intent intent = new Intent(context, MediaRecorderActivity.class);
//		Bundle bundle = new Bundle();
//		intent.putExtras(bundle);
//		context.startActivityForResult(intent, MediaRecorderActivity.REQUEST_CODE_FOR_VIDEO_PATH);
        showVideoRecord(context);
    }

    /**
     * 显示视频录制
     *
     * @param context
     */
    public static void showVideoRecord(Activity context) {
        Intent intent = new Intent(context, VideoRecordActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivityForResult(intent, VideoRecordActivity.REQUEST_CODE_FOR_VIDEO_PATH);
    }


    /**
     * 显示修改群名称页面
     *
     * @param context
     */
    public static void showModifyGroupName(Context context, String name) {
        Intent intent = new Intent(context, ModifyGroupNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ModifyGroupNameActivity.BUNDLE_KEY_NAME, name);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ModifyGroupNameActivity.REQUEST_MODIFY_NAME);
    }

    /**
     * 显示修改备注名页面
     *
     * @param context
     */
    public static void showModifyMemberRemark(Context context, String id, String name) {
        Intent intent = new Intent(context, ModifyMemberRemarkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ModifyMemberRemarkActivity.BUNDLE_KEY_ID, id);
        bundle.putString(ModifyMemberRemarkActivity.BUNDLE_KEY_NAME, name);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ModifyMemberRemarkActivity.REQUEST_MODIFY_NAME);
    }

    /**
     * 扫描二维码
     *
     * @param context
     */
    public static void showCapture(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        // Bundle bundle = new Bundle();
        // intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示动态详情
     *
     * @param context
     * @param dynamicId
     */
    public static void showDynamicDetail(Context context, String dynamicId, int position) {
        Intent intent = new Intent(context, DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_DYNAMIC_ID, dynamicId);
        bundle.putInt(DynamicDetailActivity.BUNDLE_KEY_POSITION, position);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, DynamicDetailActivity.REQUEST_DYNAMIC);
    }


    /**
     * 显示动态详情
     *
     * @param context
     * @param dynamicId
     */
    public static void showDynamicDetailWithComment(Context context, String dynamicId, int position, Comment comment) {
        Intent intent = new Intent(context, DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_DYNAMIC_ID, dynamicId);
        bundle.putInt(DynamicDetailActivity.BUNDLE_KEY_POSITION, position);
        bundle.putSerializable(DynamicDetailActivity.BUNDLE_KEY_COMMENT, comment);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, DynamicDetailActivity.REQUEST_DYNAMIC);
    }

    /**
     * 显示视频播放
     *
     * @param context
     */
    @Deprecated
    public static void showPlayVideo(Activity context, String videoPath, String previewImagePath) {
//		Intent intent = new Intent(context, PlayVideoActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString(MediaRecorderActivity.DATA_FOR_VIDEO_PATH, videoPath);
//		bundle.putString(MediaRecorderActivity.DATA_FOR_PREVIEW_IMAGE_PATH, previewImagePath);
//		intent.putExtras(bundle);
//		context.startActivity(intent);
        showVideoPlay(context, videoPath, previewImagePath);
    }

    /**
     * 显示视频播放
     *
     * @param context
     */
    public static void showVideoPlay(Activity context, String videoPath, String previewImagePath) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VideoRecordActivity.DATA_FOR_VIDEO_PATH, videoPath);
        bundle.putString(VideoRecordActivity.DATA_FOR_PREVIEW_IMAGE_PATH, previewImagePath);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示申请加入群组(文本)
     *
     * @param context
     * @param groupId
     */
    public static void showApplyGroupText(Context context, String groupId) {
        Intent intent = new Intent(context, ApplyGroupTextActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 显示申请加入活动(文本)
     *
     * @param context
     * @param groupId
     */
    public static void showApplActiveText(Context context, String groupId, String joinNeedContact, String joinNeedVerify) {
        Intent intent = new Intent(context, ApplyActiveTextActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putString(ApplyActiveTextActivity.BUNDLE_KEY_JOIN_NEED_CONTACT, joinNeedContact);
        bundle.putString(ApplyActiveTextActivity.BUNDLE_KEY_JOIN_NEED_VERIFY, joinNeedVerify);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示申请加入群组(语音)
     *
     * @param context
     */
    public static void showApplyGroupVoice(Context context, String groupId) {
        Intent intent = new Intent(context, ApplyGroupVoiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showCreateActiveType(Context context, String groupId) {
        Intent intent = new Intent(context, CreateActiveTypeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_open, 0);
    }

    public static void showGroupNotification(Context context, String title, String groupId, boolean isShowAdd) {
        Intent intent = new Intent(context, GroupNotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GroupNotificationActivity.BUNDLE_KEY_TITLE, title);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        bundle.putBoolean(GroupNotificationActivity.BUNDLE_KEY_SHOW_ADD, isShowAdd);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showActiveInfoDetail(Context context, String groupId) {
        Intent intent = new Intent(context, ActiveInfoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示我的动态列表页面
     *
     * @param context
     */
    public static void showMyDynamics(Context context) {
        Intent intent = new Intent(context, MeDynamicActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 公共的网页加载界面
     *
     * @param context
     * @param type    类型，传WebViewActivity的静态常量
     */
    public static void showWebView(Context context, String type) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showWebView(Context context, String type, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.TYPE, type);
        bundle.putString(WebViewActivity.BUNDLE_KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 跳转到发布投票活动页面
     *
     * @param context
     * @param typeId
     * @param picLocal
     * @param picMaterialId
     * @param name
     * @param parentId
     */
    public static void showCreateVoteActive(Context context, String typeId, String picLocal, String picMaterialId, String name, String parentId) {
        Intent intent = new Intent(context, CreateVoteActiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CreateTypeActivity.BUNDLE_KEY_TYPE_ID, typeId);
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, parentId);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_LOCAL, picLocal);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_PIC_MATERIALID, picMaterialId);
        bundle.putString(CreateNameActiveOrGroupActivity.BUNDLE_KEY_NAME, name);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, CreateTypeActivity.REQUEST_NEXT);
    }

    /**
     * 跳转到完善资料页面
     */
    public static void showEditProfileActivity(Context context, UserBean userBean) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EditProfileActivity.BUNDLE_KEY_USERINFO, userBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转到举报页面
     *
     * @param context
     * @param reportId
     * @param reportType
     */
    public static void showComplain(Context context, String reportId, String reportType) {
        Intent intent = new Intent(context, ComplainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ComplainActivity.BUNDLE_KEY_REPORT_ID, reportId);
        bundle.putString(ComplainActivity.BUNDLE_KEY_REPORT_TYPE, reportType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 开启app更新服务
     *
     * @param context
     * @param downUrl
     */
    public static void startUpdateSevice(Context context, String downUrl) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra(UpdateService.BUNDLE_KEY_DOWN_URL, downUrl);
        context.startService(intent);
    }

    /**
     * 显示活动动态列表页面
     *
     * @param context
     * @param groupId
     */
    public static void showActiveDynamicList(Context context, String groupId) {
        Intent intent = new Intent(context, ActiveDynamicListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示草稿箱页面
     *
     * @param context
     */
    public static void showDraftBoxListActivity(Context context) {
        Intent intent = new Intent(context, DraftBoxListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示绑定手机号页面
     *
     * @param context
     */
    public static void showBindPhone(Context context) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示活动设置页面
     *
     * @param context
     * @param groupId
     */
    public static void showActiveSetting(Context context, String groupId) {
        Intent intent = new Intent(context, ActiveSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.BUNDLE_KEY_GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示选择验证方式页面（活动）
     *
     * @param context
     * @param verifyWay
     */
    public static void showVerifyWayForActive(Context context, String verifyWay) {
        Intent intent = new Intent(context, VerifyWayForActiveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VerifyWayForActiveActivity.BUNDLE_KEY_VERIFYWAY, verifyWay);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, VerifyWayForActiveActivity.REQUEST_CODE);
    }

    /**
     * 显示选择验证方式页面(群组)
     *
     * @param context
     * @param verifyWay
     */
    public static void showVerifyWayForGroup(Context context, String verifyWay) {
        Intent intent = new Intent(context, VerifyWayForGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VerifyWayForGroupActivity.BUNDLE_KEY_VERIFYWAY, verifyWay);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, VerifyWayForGroupActivity.REQUEST_CODE);
    }

    /**
     * 显示修改群资料页面
     *
     * @param context
     * @param intro
     */
    public static void showModifyGroupIntro(Context context, String intro) {
        Intent intent = new Intent(context, ModifyGroupIntroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ModifyGroupIntroActivity.BUNDLE_KEY_INTRO, intro);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ModifyGroupIntroActivity.REQUEST_CODE);
    }
}
