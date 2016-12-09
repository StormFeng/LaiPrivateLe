package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AccountBroadcastReceiver extends BroadcastReceiver {
	/**
	 * 登陆
	 */
	public static String ACTION_LOGIN = "com.lailem.app.login";
	/**
	 * 注销
	 */
	public static String ACTION_LOGOUT = "com.lailem.app.logout";
	/**
	 * 注册
	 */
	public static String ACTION_REGISTER = "com.lailem.app.register";

	@Override
	public void onReceive(Context arg0, Intent intent) {

	}

}
