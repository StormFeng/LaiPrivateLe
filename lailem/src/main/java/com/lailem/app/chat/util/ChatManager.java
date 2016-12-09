package com.lailem.app.chat.util;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.cache.UserCache;
import com.lailem.app.utils.NetStateManager;

public class ChatManager {

	public void login(Context context) {
		if (!TextUtils.isEmpty(AppContext.getInstance().getLoginUid()) && NetStateManager.getInstance().isAvailable()) {
			MessageFactory.getFactory().init(context);
			IMManager.getInstance().loginIM();
		}
	}

	public void logout(Context context) {
		IMManager.getInstance().logoutIM();
		MessageFactory.getFactory().destory();
		UserCache.getInstance(context).clearMemory();
		PoolFactory.getFactory().destory();
		ChatListenerManager.getInstance().destory();
		MessageCountManager.getInstance().destory();
	}

}
