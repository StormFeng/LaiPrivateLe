package com.lailem.app.chat.util;

import android.text.TextUtils;

import com.lailem.app.chat.model.msg.MsgPic;
import com.lailem.app.chat.model.msg.MsgVideo;
import com.lailem.app.chat.model.msg.MsgVoice;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.FileUtils;

import java.util.List;

public class MessageFileManger {

	private static MessageFileManger instance;
	
	private MessageFileManger(){
		
	}
	
	public static MessageFileManger getInstance(){
		if(instance==null)
			instance = new MessageFileManger();
		return instance;
	}
	
	public void delete(List<Message> messages) {
		for (Message message : messages) {
			delete(message);
		}
	}

	public void delete(Message message) {
		String sType = message.getSType();
		if (Constant.sType_pic.equals(sType)) {
			MsgPic msgObj = message.getMsgObj();
			if (!TextUtils.isEmpty(msgObj.getLocalPath())) {
				FileUtils.delete(msgObj.getLocalPath());
			}
		} else if (Constant.sType_voice.equals(sType)) {
			MsgVoice msgObj = message.getMsgObj();
			if (!TextUtils.isEmpty(msgObj.getLocalPath())) {
				FileUtils.delete(msgObj.getLocalPath());
			}
		} else if (Constant.sType_video.equals(sType)) {
			MsgVideo msgObj = message.getMsgObj();
			if (!TextUtils.isEmpty(msgObj.getLocalPath())) {
				FileUtils.delete(msgObj.getLocalPath());
			}
			if (!TextUtils.isEmpty(msgObj.getLocalPicPath())) {
				FileUtils.delete(msgObj.getLocalPicPath());
			}
		}
	}

}
