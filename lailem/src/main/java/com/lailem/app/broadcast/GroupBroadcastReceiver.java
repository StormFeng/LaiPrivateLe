package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GroupBroadcastReceiver extends BroadcastReceiver {

	public static String ACTION_ACTIVITY_SNYC_OVER = "com.lailem.app.activity_snyc_over";
	public static String ACTION_GROUP_SNYC_OVER = "com.lailem.app.group_snyc_over";
	public static String ACTION_JOIN_GROUP = "com.lailem.app.join_group";
	public static String ACTION_EXIT_GROUP = "com.lailem.app.exit_group";
	public static String ACTION_JOIN_ACTIVITY = "com.lailem.app.join_activity";
	public static String ACTION_EXIT_ACTIVITY = "com.lailem.app.exit_activity";
	public static String ACTION_UPDATE_ACTIVITY = "com.lailem.app.update_activity";
	public static String ACTION_UPDATE_GROUP = "com.lailem.app.update_group";

	@Override
	public void onReceive(Context context, Intent intent) {

	}

}
