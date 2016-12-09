package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.dao.Message;

public class JoinGMessageHandler extends MessageHandler {

    public JoinGMessageHandler(Message message, Context context) {
        super(message, context);
        dealConversationMessage(message, false, false);
        remaind();
    }


}
