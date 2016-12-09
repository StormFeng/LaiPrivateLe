package com.lailem.app.chat.handler;

import android.content.Context;
import android.os.PowerManager;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.cache.UserCache;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.ChatUtil;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MNotificationManager;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.chat.util.MessageFactory;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.dao.User;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.StringUtils;
import com.lailem.app.utils.TimeUtil;
import com.lailem.app.utils.UUIDUtils;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler {
    protected Message message;
    protected Context context;

    public MessageHandler(Message message, Context context) {
        this.message = message;
        this.context = context;
    }

    /**
     * 处理即时聊天用户的头像与昵称
     *
     * @param message
     */
    private void dealUser(Message message) {
        //TODO 有可优化的空间，可以减少数据库查询的次数
        User user = UserCache.getInstance(context).get(message.getFId());
        String fHead = message.getFHead();
        String fNick = message.getFNick();
        KLog.i("UserContainer get:::" + user);
        if (user == null) {
            // 本地为空或消息中不包含发送者的头像或昵称就需要从网络取用户数据
            if (StringUtils.isEmpty(fHead) || StringUtils.isEmpty(fNick)) {
                String t = ApiClient.getApi().getUserBrief(message.getFId(), AppContext.getInstance().getLoginUid());
                KLog.i("userInfo:::" + t);
                if (StringUtils.isEmpty(t)) {
                    KLog.i("fail:get user info");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        if (Const.value_success.equals(jsonObject.get(Const.key_ret))) {
                            JSONObject userInfo = jsonObject.getJSONObject(Const.key_userInfo);
                            user = new User();
                            user.setUserId(message.getFId());
                            user.setHead(userInfo.getString(Const.key_headSPicName));
                            user.setNickname(userInfo.getString(Const.key_nickname));
                            //用异步会导致第一次收到消息时，头像可能无法显示
                            UserCache.getInstance(context).putSync(user);
                        } else {
                            KLog.i("fail:get user info");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {//本地没有用户信息，头像与昵称都不为空，插入新用户
                user = new User();
                user.setUserId(message.getFId());
                user.setHead(fHead);
                user.setNickname(fNick);
                UserCache.getInstance(context).putSync(user);
            }
        } else {// 本地有用户信息，消息中包含发送者的头像或昵称信息就需要更新本地的用户信息
            boolean needUpdateUser = false;
            if (!StringUtils.isEmpty(fHead)) {
                user.setHead(fHead);
                needUpdateUser = true;
            }
            if (!StringUtils.isEmpty(fNick)) {
                user.setNickname(fNick);
                needUpdateUser = true;
            }
            if (needUpdateUser)
                UserCache.getInstance(context).putSync(user);
        }

    }

    protected void dealConversationMessage(Message message, boolean needDownload, boolean needDealTime) {
        // 有会话的消息才需要处理用户头像与昵称
        dealUser(message);
        Conversation conversation = DaoOperate.getInstance(context).queryConversation(getTIdForConversation(message));
        if (conversation == null) {
            conversation = MessageFactory.getFactory().createForIn(getTIdForConversation(message), message.getMType());
            conversation.setTipMsg(ChatUtil.getTipMsg(message));
            conversation.setUpdateTime(message.getSTime());
            String conversationId = UUIDUtils.uuid32();
            conversation.setConversationId(conversationId);
            long cId = DaoOperate.getInstance(context).insert(conversation);
            conversation.setId(cId);
            message.setIsRead(Constant.value_no);
            //未读数量+1
            MessageCountManager.getInstance().addOne(MessageCountManager.KEY_NO_READ_COUNT_FOR_CHAT);
        } else {
            KLog.i("tipMsg:::" + ChatUtil.getTipMsg(message));
            conversation.setTipMsg(ChatUtil.getTipMsg(message));
            conversation.setUpdateTime(message.getSTime());
            // 当前正在聊天，未读数量置为0
            if (getTIdForConversation(message).equals(MessageFactory.getFactory().gettIdForChating())) {
                conversation.setNoReadCount(0);
                // 正在聊天消息置为已读
                message.setIsRead(Constant.value_yes);
            } else {// 否则未读数量+1
                conversation.setNoReadCount(conversation.getNoReadCount() + 1);
                message.setIsRead(Constant.value_no);
                // 未读才把消息数量加1
                MessageCountManager.getInstance().addOne(MessageCountManager.KEY_NO_READ_COUNT_FOR_CHAT);
            }
            DaoOperate.getInstance(context).update(conversation);
        }
        message.setConversationId(conversation.getConversationId());
        message.setUserId(AppContext.getInstance().getLoginUid());
        // 消息入库前决定是否需要插入时间
        if (needDealTime)
            dealTime(message.getConversationId(), message.getMType(), message.getFId(), message.getTId(), message.getSTime());
        long messageId = DaoOperate.getInstance(context).insert(message);
        message.setId(messageId);
        if (needDownload) {
        } else {
            ChatListenerManager.getInstance().noticeConversationAndChatListenserForChatMessageIn(conversation, message, context);
        }
    }

    /**
     * 处理时间插入记录, 【注意】需要在conversation已入库，message未入库的情况下调用
     */
    private void dealTime(String conversationId, String mType, String fId, String tId, long sTime) {
        Long lastSTime = DaoOperate.getInstance(context).queryLastSTime(conversationId);
        if (lastSTime == null) {// 表示没有消息记录
            lastSTime = Long.parseLong("0");// 当前时间的值肯定大于timeSpace
        }
        if (TimeUtil.getInstance().needRecordTime(lastSTime, MessageFactory.timeSpace)) {
            Message message = new Message();
            message.setUserId(AppContext.getInstance().getLoginUid());
            message.setTId(tId);
            message.setFId(fId);
            message.setCreateTime(System.currentTimeMillis());
            message.setIsRead(Constant.value_yes);
            message.setSTime(sTime);
            message.setMType(mType);
            message.setSType(Constant.sType_time);
            message.initMsgObj(message.getMsg(), message.getFId(), message.getSType());
            message.setConversationId(conversationId);
            long id = DaoOperate.getInstance(context).insert(message);
            message.setId(id);
            ChatListenerManager.getInstance().noticeConversationAndChatListenserForChatMessageIn(null, message, context);
        }
    }

    /**
     * 在会话列表里群聊tId与单聊的tId取自不同的字段
     *
     * @param message
     * @return
     */
    public static String getTIdForConversation(Message message) {
        if (Constant.mType_gc.equals(message.getMType())) {
            return message.getTId();
        } else {
            return message.getFId();
        }

    }

    /**
     * 提醒用户，显示通知或震动
     */
    public void remaind() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        MNotificationManager.getInstance(context).dealNotification(message);
    }
}
