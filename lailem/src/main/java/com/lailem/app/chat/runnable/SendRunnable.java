package com.lailem.app.chat.runnable;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.lailem.app.AppContext;
import com.lailem.app.chat.model.msg.MsgPic;
import com.lailem.app.chat.model.msg.MsgVideo;
import com.lailem.app.chat.model.msg.MsgVoice;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.ChatUtil;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.IMManager;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.jni.JniSharedLibraryWrapper;
import com.lailem.app.utils.Const;
import com.socks.library.KLog;

/**
 * 消息状态：uploading（文件上传中）、uploadFail（上传失败）、sending（发送中）、sended（已发送）、sendFail（
 * 发送失败）、downloading（文件下载中）、downloadFail（下载失败）、received（已接收）
 * 
 */
public class SendRunnable implements Runnable {
	Message message;
	public static String domainSubDomain = JniSharedLibraryWrapper.domainForChat() + JniSharedLibraryWrapper.subDomainForChat();
	Context context;

	public SendRunnable(Message message,Context context) {
		this.message = message;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			org.jivesoftware.smack.packet.Message sendMessage = new org.jivesoftware.smack.packet.Message();
			sendMessage.setFrom(message.getFId() + domainSubDomain);
			sendMessage.setTo(message.getTId() + domainSubDomain);
			KLog.i("消息发送前:::" + ChatUtil.gson.toJson(message));
			sendMessage.setBody(getBodyForSend(message));
			IMManager.getInstance().sendMessage(sendMessage);
			message.setStatus(Constant.status_sended);
			DaoOperate.getInstance(context).update(message);
			ChatListenerManager.getInstance().noticeSendListenerForSended(message);
            if(!TextUtils.isEmpty(message.getFHead())){
                Conversation conversation = DaoOperate.getInstance(context).queryConversation(message.getTId());
                conversation.setNeedSendHead(Constant.value_no);
                DaoOperate.getInstance(context).update(conversation);
            }
            if(!TextUtils.isEmpty(message.getFNick())){
                Conversation conversation = DaoOperate.getInstance(context).queryConversation(message.getTId());
                conversation.setNeedSendNick(Constant.value_no);
                DaoOperate.getInstance(context).update(conversation);
            }
            KLog.i("消息发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			message.setStatus(Constant.status_sendFail);
			DaoOperate.getInstance(context).update(message);
			ChatListenerManager.getInstance().noticeSendListenerForSendFail(message);
			KLog.i("消息发送失败");
			//重新连接
			IMManager.getInstance().loginIM();
			
		}
		
	}

	private String getBodyForSend(Message message) {
		String sType = message.getSType();
		String msg = null;
		if (Constant.sType_pic.equals(sType)) {
			JsonObject msgJsonObject = new JsonObject();
			MsgPic msgPic = message.getMsgObj();
			msgJsonObject.addProperty(Constant.key_pic, msgPic.getPic());
			msgJsonObject.addProperty(Constant.key_tPic, msgPic.gettPic());
			msg = msgJsonObject.toString();
		} else if (Constant.sType_voice.equals(sType)) {
			JsonObject msgJsonObject = new JsonObject();
			MsgVoice msgVoice = message.getMsgObj();
			msgJsonObject.addProperty(Constant.key_d, msgVoice.getD());
			msgJsonObject.addProperty(Constant.key_voice, msgVoice.getVoice());
			msg = msgJsonObject.toString();
		} else if (Constant.sType_video.equals(sType)) {
			JsonObject msgJsonObject = new JsonObject();
			MsgVideo msgVideo = message.getMsgObj();
			msgJsonObject.addProperty(Constant.key_d, msgVideo.getD());
			msgJsonObject.addProperty(Constant.key_video, msgVideo.getVideo());
			msgJsonObject.addProperty(Constant.key_pic, msgVideo.getPic());
			msg = msgJsonObject.toString();
		} else {
			msg = message.getMsg();
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(Constant.key_fId, message.getFId());
		jsonObject.addProperty(Constant.key_mType, message.getMType());
		jsonObject.addProperty(Constant.key_msg, msg);
		jsonObject.addProperty(Constant.key_sType, sType);
		jsonObject.addProperty(Constant.key_tId, message.getTId());
		
		Conversation conversation = DaoOperate.getInstance(context).queryConversation(message.getTId());
		String needSendHead = conversation.getNeedSendHead();
		String needSendNick = conversation.getNeedSendNick();
        if(Constant.value_yes.equals(needSendHead)){
            String sHead = AppContext.getInstance().getProperty(Const.USER_SHEAD);
            message.setFHead(sHead);
        	jsonObject.addProperty(Constant.key_fHead, sHead);
        }
        if(Constant.value_yes.equals(needSendNick)){
            String nickName = AppContext.getInstance().getProperty(Const.USER_NICKNAME);
            message.setFNick(nickName);
        	jsonObject.addProperty(Constant.key_fNick, nickName);
        }
		String body = jsonObject.toString();
		KLog.i("消息发送中:::" + body);
		return body;
	}

}
