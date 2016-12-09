package com.lailem.app.chat.runnable;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.chat.handler.comment.AComMessageHandler;
import com.lailem.app.chat.handler.comment.ALikeMessageHandler;
import com.lailem.app.chat.handler.comment.DComMessageHandler;
import com.lailem.app.chat.handler.comment.DLikeMessageHandler;
import com.lailem.app.chat.handler.communication.PicMessageHandler;
import com.lailem.app.chat.handler.communication.PositionMessageHandler;
import com.lailem.app.chat.handler.communication.TextMessageHandler;
import com.lailem.app.chat.handler.communication.TipMessageHandler;
import com.lailem.app.chat.handler.communication.VideoMessageHandler;
import com.lailem.app.chat.handler.communication.VoiceMessageHandler;
import com.lailem.app.chat.handler.notification.AGApplyMessageHandler;
import com.lailem.app.chat.handler.notification.ExitGMessageHandler;
import com.lailem.app.chat.handler.notification.GApplyMessageHandler;
import com.lailem.app.chat.handler.notification.GMissMessageHandler;
import com.lailem.app.chat.handler.notification.GNoticeMessageHandler;
import com.lailem.app.chat.handler.notification.JoinGMessageHandler;
import com.lailem.app.chat.handler.notification.RemoveGMessageHandler;
import com.lailem.app.chat.handler.notification.SyncCreateGMessageHandler;
import com.lailem.app.chat.handler.notification.SyncExitGMessageHandler;
import com.lailem.app.chat.handler.notification.SyncGMissMessageHandler;
import com.lailem.app.chat.handler.notification.SysPMessageHandler;
import com.lailem.app.chat.handler.other.GUpdateMessageHandler;
import com.lailem.app.chat.handler.other.NGPubMessageHandler;
import com.lailem.app.chat.util.ChatUtil;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.IMManager;
import com.lailem.app.dao.Message;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageInRunnable implements Runnable {
    static String callbackReceiverId = JniSharedLibraryWrapper.callbackReceiverId();
    String body;
    Context context;

    public MessageInRunnable(String body, Context context) {
        this.body = body;
        this.context = context;
    }

    @Override
    public void run() {
        KLog.i("body:::" + body);
        Message message = ChatUtil.gson.fromJson(body, Message.class);
        if (message == null)
            return;
        KLog.i("message:::" + message);
        // 消息回调
        callback(message);
        // 初始化消息体
        message.initMsgObj(message.getMsg(), message.getFId(), message.getSType());
        message.setUserId(AppContext.getInstance().getLoginUid());
        // 有些消息需要操作才能置为已读
        message.setIsReadOne(Constant.value_no);
        //设置未读
        message.setIsRead(Constant.value_no);
        String mType = message.getMType();
        String sType = message.getSType();
        // 群聊或单聊
        if (Constant.mType_gc.equals(mType) || Constant.mType_sc.equals(mType)) {

            if (Constant.sType_text.equals(sType)) {
                new TextMessageHandler(message, context);
            } else if (Constant.sType_pic.equals(sType)) {
                new PicMessageHandler(message, context);
            } else if (Constant.sType_voice.equals(sType)) {
                new VoiceMessageHandler(message, context);
            } else if (Constant.sType_video.equals(sType)) {
                new VideoMessageHandler(message, context);
            } else if (Constant.sType_position.equals(sType)) {
                new PositionMessageHandler(message, context);
            } else if (Constant.sType_tip.equals(sType)) {
                new TipMessageHandler(message, context);
            } else {
                KLog.i("未知的消息");
            }

        } else if (Constant.mType_p.equals(mType)) {

            if (Constant.sType_sysP.equals(sType)) {
                new SysPMessageHandler(message, context);
            } else if (Constant.sType_gNotice.equals(sType)) {
                new GNoticeMessageHandler(message, context);
            } else if (Constant.sType_gApply.equals(sType)) {
                new GApplyMessageHandler(message, context);
            } else if (Constant.sType_exitG.equals(sType)) {
                new ExitGMessageHandler(message, context);
            } else if (Constant.sType_aGApply.equals(sType)) {
                new AGApplyMessageHandler(message, context);
            } else if (Constant.sType_gMiss.equals(sType)) {
                new GMissMessageHandler(message, context);
            } else if (Constant.sType_dCom.equals(sType)) {
                new DComMessageHandler(message, context);
            } else if (Constant.sType_dLike.equals(sType)) {
                new DLikeMessageHandler(message, context);
            } else if (Constant.sType_aCom.equals(sType)) {
                new AComMessageHandler(message, context);
            } else if (Constant.sType_aLike.equals(sType)) {
                new ALikeMessageHandler(message, context);
            } else if (Constant.sType_nGPub.equals(sType)) {
                new NGPubMessageHandler(message, context);
            } else if (Constant.sType_gUpdate.equals(sType)) {
                new GUpdateMessageHandler(message, context);
            } else if (Constant.sType_joinG.equals(sType)) {
                new JoinGMessageHandler(message, context);
            } else if (Constant.sType_removeG.equals(sType)) {
                new RemoveGMessageHandler(message, context);
            } else {
                KLog.i("未知的消息");
            }

        } else if (Constant.mType_sync.equals(mType)) {
            if (Constant.sType_sync_createG.equals(sType)) {
                new SyncCreateGMessageHandler(message, context);
            } else if (Constant.sType_sync_exitG.equals(sType)) {
                new SyncExitGMessageHandler(message, context);
            } else if (Constant.sType_sync_GMiss.equals(sType)) {
                new SyncGMissMessageHandler(message, context);
            } else {
                KLog.i("未知的消息");
            }
        } else {
            KLog.i("未知的消息");
        }

    }

    /**
     * 消息回调
     *
     * @param message
     */
    private void callback(Message message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.key_mType, Constant.mType_cb);
            jsonObject.put(Constant.key_mId, message.getMId());
            org.jivesoftware.smack.packet.Message sendMessage = new org.jivesoftware.smack.packet.Message();
            sendMessage.setBody(jsonObject.toString());
            sendMessage.setFrom(message.getFId() + SendRunnable.domainSubDomain);
            sendMessage.setTo(callbackReceiverId + SendRunnable.domainSubDomain);
            IMManager.getInstance().sendMessage(sendMessage);
            KLog.i("消息回调成功：mId=" + message.getMId());
        } catch (JSONException e) {
            KLog.i("消息回调失败：mId=" + message.getMId());
            e.printStackTrace();
        }
    }

}
