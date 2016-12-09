package com.lailem.app.chat.handler.comment;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;

public class DLikeMessageHandler extends MessageHandler {

	public DLikeMessageHandler(Message message, Context context) {
		super(message, context);
		// 评论消息进来一定是未读的，因为用户在看评论消息时不需要即时更新界面（新消息插入顶部）
		message.setIsRead(Constant.value_no);
		long messageId = DaoOperate.getInstance(context).insert(message);
		message.setId(messageId);
		ChatListenerManager.getInstance().noticeComListenerForComMessageIn(message);
		MessageCountManager.getInstance().addOne(MessageCountManager.KEY_NO_READ_COUNT_FOR_COM);
		remaind();
	}

}
