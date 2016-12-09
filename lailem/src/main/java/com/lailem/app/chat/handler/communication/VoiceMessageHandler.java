package com.lailem.app.chat.handler.communication;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.dao.Message;

public class VoiceMessageHandler extends MessageHandler {

	public VoiceMessageHandler(Message message, Context context) {
		super(message, context);
		dealConversationMessage(message, false, true);
		remaind();
	}


}
