package com.lailem.app.chat.util;

import android.app.Activity;
import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.listener.OnChatListener;
import com.lailem.app.chat.listener.OnComListener;
import com.lailem.app.chat.listener.OnConversationListener;
import com.lailem.app.chat.listener.OnGroupListener;
import com.lailem.app.chat.listener.OnMessageCountListener;
import com.lailem.app.chat.listener.OnSendListener;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.MGroup;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.StringUtils;

import java.util.ArrayList;

public class ChatListenerManager {

	OnChatListener onChatListener;
	OnConversationListener onConversationListener;
	ArrayList<OnComListener> onComListeners;
	ArrayList<OnGroupListener> onGroupListeners;
	ArrayList<OnSendListener> onSendListeners;
	ArrayList<OnMessageCountListener> onMessageListeners;
	Activity activity = new Activity();

	private static ChatListenerManager instance;

	private ChatListenerManager() {

	}

	public static ChatListenerManager getInstance() {
		if (instance == null)
			instance = new ChatListenerManager();
		return instance;
	}

	public void destory() {
		onChatListener = null;
		onConversationListener = null;
		if (onComListeners != null && onComListeners.size() > 0) {
			onComListeners.clear();
		}
		onComListeners = null;
		if (onGroupListeners != null && onGroupListeners.size() > 0) {
			onGroupListeners.clear();
		}
		onGroupListeners = null;
		if (onSendListeners != null && onSendListeners.size() > 0) {
			onSendListeners.clear();
		}
		onSendListeners = null;
		instance = null;
	}

	// ----------------------------------------监听器-------------------------------------------
	public void registerOnChatListener(OnChatListener onChatListener) {
		this.onChatListener = onChatListener;
	}

	public void unRegisterOnChatListener() {
		this.onChatListener = null;
	}

	public void registerOnConversationListener(OnConversationListener onConversationListener) {
		this.onConversationListener = onConversationListener;
	}

	public void unRegisterOnConversationListener() {
		this.onConversationListener = null;
	}

	public void registerOnComListener(OnComListener onComListener) {
		if (onComListeners == null)
			onComListeners = new ArrayList<OnComListener>();
		this.onComListeners.add(onComListener);
	}

	public void unRegisterOnComListener(OnComListener onComListener) {
        if(this.onComListeners!=null) {
            this.onComListeners.remove(onComListener);
        }
	}

	public void registerOnGroupListener(OnGroupListener onGroupListener) {
		if (onGroupListeners == null)
			onGroupListeners = new ArrayList<OnGroupListener>();
		this.onGroupListeners.add(onGroupListener);
	}

	public void unRegisterOnGroupListener(OnGroupListener onGroupListener) {
        if(this.onGroupListeners!=null) {
            this.onGroupListeners.remove(onGroupListener);
        }
	}

	public void registerOnSendListener(OnSendListener onSendListener) {
		if (onSendListeners == null)
			onSendListeners = new ArrayList<OnSendListener>();
		this.onSendListeners.add(onSendListener);
	}

	public void unRegisterOnSendListener(OnSendListener onSendListener) {
        if(this.onSendListeners!=null) {
            this.onSendListeners.remove(onSendListener);
        }
	}

	public void registerOnMessageListener(OnMessageCountListener onMessageListener){
	   if(onMessageListeners==null)
		   onMessageListeners = new ArrayList<OnMessageCountListener>();
	   onMessageListeners.add(onMessageListener);
	}
	
	public void unRegisterOnMessageListener(OnMessageCountListener onMessageListener){
        if(this.onMessageListeners!=null) {
            onMessageListeners.remove(onMessageListener);
        }
	}
	


	/**
	 * 进来的聊天消息
	 * 
	 * @param conversation
	 * @param message
	 * @param context
	 */
	public void noticeConversationAndChatListenserForChatMessageIn(final Conversation conversation, final Message message, final Context context) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 消息发送与消息进来更新会话列表
				if (conversation != null && onConversationListener != null) {
					onConversationListener.onConversation(conversation);
				}
				// 正在进行聊天且该消息与正聊天的人匹配,会话id不等于空才是聊天消息
				if (!StringUtils.isEmpty(MessageFactory.getFactory().gettIdForChating()) && onChatListener != null && MessageFactory.getFactory().gettIdForChating().equals(MessageHandler.getTIdForConversation(message))
						&& message.getConversationId() != null) {
					onChatListener.onChat(message);
				}

			}
		});
	}

	/**
	 * 聊天发送消息时，通知会话列表更新
	 * 
	 * @param conversation
	 */
	public void noticeConversationListenerForChatMessageSend(Conversation conversation) {
		if (onConversationListener != null)
			onConversationListener.onConversation(conversation);
	}

	/**
	 * 进来的评论点赞消息
	 * 
	 * @param message
	 */
	public void noticeComListenerForComMessageIn(final Message message) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (onComListeners != null && onComListeners.size() > 0) {
					for (OnComListener onComListener : onComListeners)
						onComListener.onCom(message);
				}
			}
		});

	}

	public void noticeGroupListenerForNGPubMessageIn(final MGroup mGroup) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (onGroupListeners != null && onGroupListeners.size() > 0)
					for (OnGroupListener onGroupListener : onGroupListeners)
						onGroupListener.onGroup(mGroup);
			}
		});
	}

	/**
	 * 发送成功
	 * 
	 * @param message
	 */
	public void noticeSendListenerForSended(final Message message) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (onSendListeners != null && onSendListeners.size() > 0) {
					for (OnSendListener onSendListener : onSendListeners) {
						onSendListener.onSended(message);
					}
				}

			}
		});
	}

	/**
	 * 发送失败
	 * 
	 * @param message
	 */
	public void noticeSendListenerForSendFail(final Message message) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (onSendListeners != null && onSendListeners.size() > 0) {
					for (OnSendListener onSendListener : onSendListeners) {
						onSendListener.onSendFail(message);
					}
				}

			}
		});
	}

	/**
	 * 文件上传中
	 * 
	 * @param message
	 * @param progress
	 */
	public void noticeSendListenerForSendUploading(final Message message, final String progress) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (onSendListeners != null && onSendListeners.size() > 0) {
					for (OnSendListener onSendListener : onSendListeners) {
						onSendListener.onSendUploading(message, progress);
					}
				}

			}
		});
	}
	
	

}
