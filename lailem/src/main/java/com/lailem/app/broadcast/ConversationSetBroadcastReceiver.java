package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConversationSetBroadcastReceiver extends BroadcastReceiver {
	/**
	 * 置顶
	 */
	public static final String ACTION_SET_TOP = "com.lailem.app.set_top"; 
	/**
	 * 消息免打扰
	 */
	public static final String ACTION_NO_TIP = "com.lailem.app.no_tip"; 
	/**
	 * 清除聊天记录
	 */
	public static final String ACTION_CLEAR_MSG_RECORD = "com.lailem.app.clear_msg_record"; 

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		

	}

}
