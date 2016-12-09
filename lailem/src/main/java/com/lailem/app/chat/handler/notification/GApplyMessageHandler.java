package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.dao.Message;

public class GApplyMessageHandler extends MessageHandler {

	public GApplyMessageHandler(Message message, Context context) {
		super(message, context);
		dealConversationMessage(message, false, false);
		remaind();
	}


}
