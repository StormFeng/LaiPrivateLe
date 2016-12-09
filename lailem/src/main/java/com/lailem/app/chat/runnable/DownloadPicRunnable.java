package com.lailem.app.chat.runnable;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.Message;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DownloadPicRunnable implements Runnable {
	Conversation conversation;
	Message message;
	Context context;

	public DownloadPicRunnable(Conversation conversation, Message message,Context context) {
		this.conversation = conversation;
		this.message = message;
		this.context = context;
	}

	@Override
	public void run() {

		AppContext.getInstance().imageLoader.loadImage("", new SimpleImageLoadingListener() {

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				ChatListenerManager.getInstance().noticeConversationAndChatListenserForChatMessageIn(conversation, message,context);
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				ChatListenerManager.getInstance().noticeConversationAndChatListenserForChatMessageIn(conversation, message,context);
			}

		});

	}

}
