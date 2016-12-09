package com.lailem.app.chat.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.lailem.app.AppContext;
import com.lailem.app.chat.runnable.MessageInRunnable;
import com.lailem.app.chat.runnable.SendRunnable;
import com.lailem.app.chat.runnable.UploadRunnable;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.TimeUtil;
import com.lailem.app.utils.UUIDUtils;

public class MessageFactory {
    public static long timeSpace = 2 * 60 * 1000;

    private static MessageFactory factory;

    /**
     * 当前正在聊天的用户id或群id
     */
    String tIdForChating;
    /**
     * 当前正在聊天的会话id
     */
    String conversationIdForChating;
    /**
     * 当前正在聊天的会话类型
     */
    String cTypeForChating;

    Context context;


    private MessageFactory() {
    }

    public static MessageFactory getFactory() {
        if (factory == null)
            factory = new MessageFactory();
        return factory;
    }

    public void init(Context context) {
        this.context = context;
    }


    public void destory() {
        factory = null;
    }

    public void onMessage(String body) {
        PoolFactory.getFactory().getPoolForMessageIn().submit(new MessageInRunnable(body, context));
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void send(Message message) {
        String sType = message.getSType();
        if (Constant.sType_pic.equals(sType) || Constant.sType_voice.equals(sType) || Constant.sType_video.equals(sType)) {
            PoolFactory.getFactory().getPoolForUpload().submit(new UploadRunnable(message, context));
        } else {
            PoolFactory.getFactory().getPoolForSend().submit(new SendRunnable(message, context));
        }
    }

    /**
     * 重发消息
     *
     * @param message
     */
    public void reSend(Message message) {
        String sType = message.getSType();
        if (Constant.sType_pic.equals(sType) || Constant.sType_voice.equals(sType) || Constant.sType_video.equals(sType)) {
            String status = message.getStatus();
            if (Constant.status_uploadFail.equals(status)) {
                PoolFactory.getFactory().getPoolForUpload().submit(new UploadRunnable(message, context));
            } else if (Constant.status_sendFail.equals(status)) {
                PoolFactory.getFactory().getPoolForSend().submit(new SendRunnable(message, context));
            }
        } else {
            PoolFactory.getFactory().getPoolForSend().submit(new SendRunnable(message, context));
        }
    }

    // -------------------------------------------创建消息----------------------------------------------------------
    public Message createMessageText(String text) {
        Message message = createForSend(tIdForChating);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.sType_text, text);
        message.setMType(cTypeForChating);
        message.setSType(Constant.sType_text);
        message.setMsg(jsonObject.toString());
        message.setStatus(Constant.status_sending);
        dealConversationForSend(tIdForChating, cTypeForChating, message);
        return message;
    }

    public Message createMessagePic(String path) {
        Message message = createForSend(tIdForChating);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.key_localPath, path);
        message.setMType(cTypeForChating);
        message.setSType(Constant.sType_pic);
        message.setMsg(jsonObject.toString());
        message.setStatus(Constant.status_uploading);
        dealConversationForSend(tIdForChating, cTypeForChating, message);
        return message;
    }

    public Message createMessageVoice(String path, String duration) {
        Message message = createForSend(tIdForChating);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.key_localPath, path);
        jsonObject.addProperty(Constant.key_d, duration);
        message.setMType(cTypeForChating);
        message.setSType(Constant.sType_voice);
        message.setMsg(jsonObject.toString());
        message.setStatus(Constant.status_uploading);
        message.setIsReadOne(Constant.value_yes);
        dealConversationForSend(tIdForChating, cTypeForChating, message);
        return message;
    }


    public Message createMessageVideo(String videoPath, String previewImagePath, String duration) {
        Message message = createForSend(tIdForChating);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.key_localPath, videoPath);
        jsonObject.addProperty(Constant.key_localPicPath, previewImagePath);
        jsonObject.addProperty(Constant.key_d, duration);
        message.setMType(cTypeForChating);
        message.setSType(Constant.sType_video);
        message.setMsg(jsonObject.toString());
        message.setStatus(Constant.status_uploading);
        message.setIsReadOne(Constant.value_yes);
        dealConversationForSend(tIdForChating, cTypeForChating, message);
        return message;
    }

    public Message createMessageAddress(String address, String lon, String lat) {
        Message message = createForSend(tIdForChating);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.key_addr, address);
        jsonObject.addProperty(Constant.key_lon, lon);
        jsonObject.addProperty(Constant.key_lat, lat);
        message.setMType(cTypeForChating);
        message.setSType(Constant.sType_position);
        message.setMsg(jsonObject.toString());
        message.setStatus(Constant.status_sending);
        message.setIsReadOne(Constant.value_yes);
        dealConversationForSend(tIdForChating, cTypeForChating, message);
        return message;
    }

    private void dealConversationForSend(String tId, String cType, Message message) {
        message.initMsgObj(message.getMsg(), message.getFId(), message.getSType());
        Conversation conversation = DaoOperate.getInstance(context).queryConversation(tId);
        if (conversation == null) {
            conversation = createForSend(tId, cType);
            conversation.setTipMsg(ChatUtil.getTipMsg(message));
            long cId = DaoOperate.getInstance(context).insert(conversation);
            conversation.setId(cId);
        } else {
            conversation.setTipMsg(ChatUtil.getTipMsg(message));
            DaoOperate.getInstance(context).update(conversation);
        }
        message.setConversationId(conversation.getConversationId());
        long messageId = DaoOperate.getInstance(context).insert(message);
        message.setId(messageId);
        ChatListenerManager.getInstance().noticeConversationListenerForChatMessageSend(conversation);
    }

    private Message createForSend(String tId) {
        Message message = new Message();
        message.setUserId(AppContext.getInstance().getLoginUid());
        message.setTId(tId);
        message.setFId(AppContext.getInstance().getLoginUid());
        message.setCreateTime(System.currentTimeMillis());
        message.setSTime(System.currentTimeMillis());
        message.setIsRead(Constant.value_yes);
        return message;
    }

    private Conversation createForSend(String tId, String cType) {
        Conversation conversation = new Conversation();
        conversation.setCreateTime(System.currentTimeMillis());
        conversation.setCType(cType);
        conversation.setIsNoTip(Constant.value_no);
        conversation.setIsTop(Constant.value_no);
        conversation.setNeedSendHead(Constant.value_yes);
        conversation.setNeedSendNick(Constant.value_yes);
        conversation.setNoReadCount(0);
        conversation.setTId(tId);
        conversation.setUpdateTime(System.currentTimeMillis());
        conversation.setUserId(AppContext.getInstance().getLoginUid());
        //从非会话列表进入聊天窗口时已经给conversationIdForChating赋值了（包含新建），此处无需再生成conversationId
        conversation.setConversationId(conversationIdForChating);
        return conversation;
    }


    public Conversation createForIn(String tId, String cType) {
        Conversation conversation = new Conversation();
        conversation.setCreateTime(System.currentTimeMillis());
        conversation.setCType(cType);
        conversation.setIsNoTip(Constant.value_no);
        conversation.setIsTop(Constant.value_no);
        conversation.setNeedSendHead(Constant.value_no);
        conversation.setNeedSendNick(Constant.value_no);
        conversation.setNoReadCount(1);
        conversation.setTId(tId);
        conversation.setUserId(AppContext.getInstance().getLoginUid());
        conversation.setConversationId(UUIDUtils.uuid32());
        return conversation;
    }


    // ------------------------------------------其它--------------------------------------------------------------


    /**
     * 当前正在聊天的用户id或群id,在聊天窗口的onCreate(..)方法中调用
     */
    public String onCreate(String tId, String conversationId, String cType) {
        this.tIdForChating = tId;
        //不从会话列表进入是，conversationId为空值
        if (TextUtils.isEmpty(conversationId)) {
            //查询数据库是否存在会话
            Conversation conversation = DaoOperate.getInstance(context).queryConversation(tId);
            //存在即赋值
            if (conversation != null) {
                this.conversationIdForChating = conversation.getConversationId();
                //不存在新建一个consationId并赋值
            } else {
                this.conversationIdForChating = UUIDUtils.uuid32();
            }
        } else {
            this.conversationIdForChating = conversationId;
        }
        this.cTypeForChating = cType;

        return conversationIdForChating;
    }

    /**
     * 在聊天窗口的断onDestory()方法中调用
     */
    public void onDestory() {
        this.tIdForChating = null;
        this.conversationIdForChating = null;
        this.cTypeForChating = null;

    }


    /**
     * 发送消息前调用，检查是否需要插入时间消息
     *
     * @return 需要插入返回对应的时间消息，否则返回null
     */
    public Message checkMessageTimeForSend() {
        Long lastSTime = DaoOperate.getInstance(context).queryLastSTime(conversationIdForChating);
        if (lastSTime == null) {// 表示没有消息记录
            lastSTime = Long.parseLong("0");// 当前时间的值肯定大于timeSpace
        }
        if (TimeUtil.getInstance().needRecordTime(lastSTime, timeSpace) || System.currentTimeMillis() - IMManager.loginSuccessTime < 5000) {
            Message message = new Message();
            message.setUserId(AppContext.getInstance().getLoginUid());
            message.setTId(tIdForChating);
            message.setFId(AppContext.getInstance().getLoginUid());
            message.setCreateTime(System.currentTimeMillis());
            message.setIsRead(Constant.value_yes);
            message.setSTime(System.currentTimeMillis());
            message.setMType(cTypeForChating);
            message.setSType(Constant.sType_time);
            message.setConversationId(conversationIdForChating);
            long id = DaoOperate.getInstance(context).insert(message);
            message.setId(id);
            message.initMsgObj(message.getMsg(), message.getFId(), message.getSType());
            return message;
        }

        return null;
    }

    public String gettIdForChating() {
        return tIdForChating;
    }

    public String getConversationIdForChating() {
        return conversationIdForChating;
    }

    public String getcTypeForChating() {
        return cTypeForChating;
    }


}
