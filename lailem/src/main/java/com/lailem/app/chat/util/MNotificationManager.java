package com.lailem.app.chat.util;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgACom;
import com.lailem.app.chat.model.msg.MsgAGApply;
import com.lailem.app.chat.model.msg.MsgALike;
import com.lailem.app.chat.model.msg.MsgDCom;
import com.lailem.app.chat.model.msg.MsgDLike;
import com.lailem.app.chat.model.msg.MsgExitG;
import com.lailem.app.chat.model.msg.MsgGApply;
import com.lailem.app.chat.model.msg.MsgGMiss;
import com.lailem.app.chat.model.msg.MsgGNotice;
import com.lailem.app.chat.model.msg.MsgJoinG;
import com.lailem.app.chat.model.msg.MsgNGPub;
import com.lailem.app.chat.model.msg.MsgRemoveG;
import com.lailem.app.chat.model.msg.MsgSysP;
import com.lailem.app.chat.model.msg.MsgText;
import com.lailem.app.chat.model.msg.MsgTip;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.ui.main.MainActivity;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.StringUtils;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.HashMap;

public class MNotificationManager {
    public static Gson gson = new Gson();
    private static MNotificationManager instance;
    Context context;
    ActivityManager activityManager;
    PowerManager powerManager;
    String packageName;
    boolean haveNotification;
    String appName;
    NotificationManager notificationManager;
    int count;
    boolean isRemind;
    static final String key_isRemind = "notificationIsRemind";

    private int notifyId;
    private HashMap<String, IdModel> chatNotifyIds = new HashMap<String, IdModel>();//保存聊天通知id
    private HashMap<String, IdModel> nGPubNotifyIds = new HashMap<String, IdModel>();//保存活动或群组新发表提醒通知id


    private MNotificationManager(Context context) {
        this.context = context;
        initNotificationDeal();
    }

    public static MNotificationManager getInstance(Context context) {
        if (instance == null)
            instance = new MNotificationManager(context);
        return instance;
    }

    private void initNotificationDeal() {
        if (activityManager == null || powerManager == null || packageName == null || appName == null) {
            activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            packageName = context.getPackageName();
            appName = context.getString(R.string.app_name);
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Constant.value_yes.equals(AppContext.getInstance().getProperty(key_isRemind))) {
            isRemind = true;
        } else {
            isRemind = false;
        }
        //如果是第一启动
        if (TextUtils.isEmpty(AppContext.getInstance().getProperty(key_isRemind))) {
            AppContext.getInstance().setProperty(key_isRemind, Constant.value_yes);
            isRemind = true;
        }
    }

    /**
     * 发起通知
     */
    public void dealNotification(Message message) {
        if (!isRemind) {
            return;
        }

        long when = System.currentTimeMillis();
        if (when - IMManager.loginSuccessTime > 5000) {
            //如果处于app页面
            if (powerManager.isScreenOn() && packageName.equals(activityManager.getRunningTasks(1).get(0).topActivity.getPackageName())) {
                //正在聊天，不用提醒
                if (MessageHandler.getTIdForConversation(message).equals(MessageFactory.getFactory().gettIdForChating()) && powerManager.isScreenOn()) {
                    return;
                }
                //震动提醒
                VibratorManager.getInstance(context).vibrate();
                return;
            }
            haveNotification = true;
            handleNotification(message);
        }
    }

    /**
     *
     */
    public void cancelNotification() {
        if (haveNotification && notificationManager != null) {
            haveNotification = false;
            notificationManager.cancelAll();
            count = 0;
            notifyId = 0;
            chatNotifyIds.clear();
            nGPubNotifyIds.clear();
        }

    }

    /**
     * true 新消息通知；false 新消息不通知
     *
     * @param flag
     */
    public void setIsRemind(boolean flag) {
        isRemind = flag;
        if (flag) {
            AppContext.getInstance().setProperty(key_isRemind, Constant.value_yes);
        } else {
            AppContext.getInstance().setProperty(key_isRemind, Constant.value_no);
        }
    }

    /**
     * 获取新消息是否显示通知
     *
     * @return
     */
    public boolean getIsRemind() {
        return isRemind;
    }

    /**
     * 显示新的通知
     */
    private void handleNotification(Message message) {

        String sType = message.getSType();

        //聊天
        if (Constant.sType_text.equals(sType)
                || Constant.sType_pic.equals(sType)
                || Constant.sType_voice.equals(sType)
                || Constant.sType_video.equals(sType)
                || Constant.sType_position.equals(sType)
                || Constant.sType_tip.equals(sType)) {
            handleChatNotification(message);
        }
        //通知消息
        else if (Constant.sType_sysP.equals(sType)
                || Constant.sType_gNotice.equals(sType)
                || Constant.sType_gApply.equals(sType)
                || Constant.sType_aGApply.equals(sType)
                || Constant.sType_exitG.equals(sType)
                || Constant.sType_gMiss.equals(sType)
                || Constant.sType_joinG.equals(sType)
                || Constant.sType_removeG.equals(sType)) {
            handleNoticeNotification(message);
        }
        // 评论与点赞
        else if (Constant.sType_dCom.equals(sType)
                || Constant.sType_dLike.equals(sType)
                || Constant.sType_aCom.equals(sType)
                || Constant.sType_aLike.equals(sType)) {
            handleDynamicNotification(message);
        }
        //group新发表提醒
        else if (Constant.sType_nGPub.equals(sType)) {
            handleNewPublishNotification(message);
        }

    }

    /**
     * 处理聊天消息
     */
    private void handleChatNotification(Message message) {
        String ticker = "";
        String title = "";
        String content = "";
        String avatarUrl = "";

        String userName = "";
        String conversionName = context.getString(R.string.app_name);
        String text = "";
        IdModel idModel = null;

        if (chatNotifyIds.containsKey(message.getConversationId())) {
            idModel = chatNotifyIds.get(message.getConversationId());
            idModel.count = idModel.count + 1;
        } else {
            notifyId++;
            idModel = new IdModel(notifyId, 1);
            chatNotifyIds.put(message.getConversationId(), idModel);
        }

        userName = Func.getRemark(context, message.getFId());

        if (Constant.cType_gc.equals(message.getMType())) {
            //群聊
            Group group = GroupCache.getInstance(context).get(message.getTId());
            if (group != null) {
                avatarUrl = StringUtils.getUri(group.getSquareSPic());
                conversionName = group.getName();
            }
        } else if (Constant.cType_sc.equals(message.getMType())) {
            avatarUrl = Func.getHead(context, message.getFId());
        }

        String sType = message.getSType();
        if (Constant.sType_text.equals(sType)) {
            MsgText msgText = message.getMsgObj();
            text = msgText.getText();
        } else if (Constant.sType_pic.equals(sType)) {
            text = "[图片]";
        } else if (Constant.sType_voice.equals(sType)) {
            text = "[语音]";
        } else if (Constant.sType_video.equals(sType)) {
            text = "[视频]";
        } else if (Constant.sType_position.equals(sType)) {
            text = "[位置]";
        } else if (Constant.sType_tip.equals(sType)) {
            MsgTip msgObj = message.getMsgObj();
            text = msgObj.getTip();
        }


        if (Constant.cType_gc.equals(message.getMType())) {
            //群聊
            if (Constant.sType_tip.equals(sType)) {
                //tip消息
                ticker = text;
            } else {
                //聊天消息
                ticker = userName + ":" + text;
            }
            title = conversionName;
        } else {
            ticker = userName + ":" + text;
            title = userName;
        }
        if (idModel.count <= 1) {
            if (Constant.cType_gc.equals(message.getMType())) {
                //群聊
                if (Constant.sType_tip.equals(sType)) {
                    //tip消息
                    content = userName + "：" + text;
                } else {
                    //聊天消息
                    content = userName + "：" + text;
                }
            } else {
                content = text;
            }
        } else {
            content = "[" + idModel.count + "条]" + userName + "：" + text;
        }

        showNotification(title, content, ticker, avatarUrl, idModel);

    }

    /**
     * 处理互动消息
     *
     * @param message
     */
    private void handleDynamicNotification(Message message) {
        String ticker = "";
        String title = "";
        String content = "";
        String avatarUrl = "";
        String userName = "";

        notifyId++;
        IdModel idModel = new IdModel(notifyId, 1);


        String sType = message.getSType();
        if (Constant.sType_dCom.equals(sType)) {
            MsgDCom msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            String com = msgObj.getCom();
            if (TextUtils.isEmpty(msgObj.gettCom())) {
                content = "评论了你的动态：" + com;
            } else {
                content = "回复：" + com;
            }
            ticker = userName + ":" + content;
        } else if (Constant.sType_dLike.equals(sType)) {
            MsgDLike msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            content = "给你的动态点了一个赞";
            ticker = userName + ":" + content;
        } else if (Constant.sType_aCom.equals(sType)) {
            MsgACom msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            String com = msgObj.getCom();
            if (TextUtils.isEmpty(msgObj.gettCom())) {
                content = "评论了你的活动：" + com;
            } else {
                content = "回复：" + com;
            }
            ticker = userName + ":" + content;
        } else if (Constant.sType_aLike.equals(sType)) {
            MsgALike msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            content = "给你的活动点了一个赞";
            ticker = userName + ":" + content;
        }

        showNotification(title, content, ticker, avatarUrl, idModel);
    }

    /**
     * 处理通知消息
     */
    private void handleNoticeNotification(Message message) {
        String ticker = "";
        String title = "";
        String content = "";
        String avatarUrl = "";
        String userName = "";

        notifyId++;
        IdModel idModel = new IdModel(notifyId, 1);

        String sType = message.getSType();
        if (Constant.sType_sysP.equals(sType)) {
            MsgSysP msgObj = message.getMsgObj();
            userName = context.getString(R.string.app_name);
            avatarUrl = "";
            title = userName;
            content = "系统消息：" + msgObj.getTitle();
            ticker = content;
        } else if (Constant.sType_gNotice.equals(sType)) {
            MsgGNotice msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = "群组通知：" + msgObj.getTopic();
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = "活动通知：" + msgObj.getTopic();
            }
            ticker = content;
        } else if (Constant.sType_gApply.equals(sType)) {
            MsgGApply msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            Group group = GroupCache.getInstance(context).get(msgObj.getgInfo().getId());
            String groupName = "";
            if (group != null) {
                groupName = group.getName();
            }
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = userName + "申请加入群组：" + groupName;
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = userName + "申请加入活动：" + groupName;
            }
            ticker = content;
        } else if (Constant.sType_aGApply.equals(sType)) {
            MsgAGApply msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getvInfo().getId(), msgObj.getvInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getvInfo().getsHead());
            title = userName;
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = userName + "同意了你的群组申请";
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = userName + "同意了你的活动申请";
            }
            ticker = content;
        } else if (Constant.sType_exitG.equals(sType)) {
            MsgExitG msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getsHead());
            title = userName;
            Group group = GroupCache.getInstance(context).get(msgObj.getgInfo().getId());
            String groupName = "";
            if (group != null) {
                groupName = group.getName();
            }
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = userName + "退出了群组：" + groupName;
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = userName + "退出了活动：" + groupName;
            }
            ticker = content;
        } else if (Constant.sType_gMiss.equals(sType)) {
            MsgGMiss msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getcInfo().getId(), msgObj.getcInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getcInfo().getsHead());
            title = userName;
            Group group = GroupCache.getInstance(context).get(msgObj.getgInfo().getId());
            String groupName = "";
            if (group != null) {
                groupName = group.getName();
            }
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = userName + "解散了群组：" + groupName;
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = userName + "解散了活动：" + groupName;
            }
            ticker = content;
        } else if (Constant.sType_joinG.equals(sType)) {
            MsgJoinG msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.getuInfo().getId(), msgObj.getuInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.getuInfo().getNick());
            title = userName;
            Group group = GroupCache.getInstance(context).get(msgObj.getgInfo().getId());
            String groupName = "";
            if (group != null) {
                groupName = group.getName();
            }
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = userName + "加入了群组：" + groupName;
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = userName + "报名了活动：" + groupName;
            }
            ticker = content;
        } else if (Constant.sType_removeG.equals(sType)) {
            MsgRemoveG msgObj = message.getMsgObj();
            userName = Func.formatNickName(context, msgObj.gethInfo().getId(), msgObj.gethInfo().getNick());
            avatarUrl = StringUtils.getUri(msgObj.gethInfo().getNick());
            title = userName;
            Group group = GroupCache.getInstance(context).get(msgObj.getgInfo().getId());
            String groupName = "";
            if (group != null) {
                groupName = group.getName();
            }
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {
                content = "您已被" + userName + "移除" + groupName;
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {
                content = "您已被" + userName + "移除" + groupName;
            }
            ticker = content;
        }

        showNotification(title, content, ticker, avatarUrl, idModel);

    }

    /**
     * 处理活动和群组新发表提醒
     */
    private void handleNewPublishNotification(Message message) {
        String ticker = "";
        String title = "";
        String content = "";

        IdModel idModel = null;

        MsgNGPub msgObj = message.getMsgObj();

        if (nGPubNotifyIds.containsKey(msgObj.getgType())) {
            idModel = nGPubNotifyIds.get(msgObj.getgType());
            idModel.count = idModel.count + 1;
        } else {
            notifyId++;
            idModel = new IdModel(notifyId, 1);
            nGPubNotifyIds.put(msgObj.getgType(), idModel);
        }

        if (Constant.gType_group.equals(msgObj.getgType())) {
            title = "群组动态";
            content = idModel.count + "条新动态";
            ticker = title + ":" + content;
        } else if (Constant.gType_activity.equals(msgObj.getgType())) {
            title = "活动动态";
            content = idModel.count + "条新动态";
            ticker = title + ":" + content;
        }

        showNotification(title, content, ticker, "", idModel);
    }


    /**
     * 弹出通知
     */
    private void showNotification(final String title, final String content, final String ticker, final String avatarUrl, IdModel idModel) {
        if (!TextUtils.isEmpty(ticker) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            if (idModel.builder == null) {
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                PendingIntent contentIndent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIndent)
                        .setSmallIcon(R.drawable.ic_laile)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_square))
                        .setTicker(ticker)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_MAX);
                final Notification notification = builder.getNotification();
                idModel.builder = builder;
                final int notifyId = idModel.id;
                if (!TextUtils.isEmpty(avatarUrl)) {
                    AppContext.getInstance().imageLoader.loadImage(avatarUrl, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            builder.setLargeIcon(loadedImage);
                            notificationManager.notify(notifyId, notification);
                        }
                    });
                } else {
                    notificationManager.notify(idModel.id, notification);
                }

            } else {
                idModel.builder.setTicker(ticker);
                idModel.builder.setContentTitle(title);
                idModel.builder.setContentText(content);
                notificationManager.notify(idModel.id, idModel.builder.build());
            }
        }
    }

    /**
     * 用于保存通知id
     */
    private class IdModel {
        public int id;
        public int count;
        public NotificationCompat.Builder builder;

        public IdModel(int id, int count) {
            this.id = id;
            this.count = count;
        }
    }
}
