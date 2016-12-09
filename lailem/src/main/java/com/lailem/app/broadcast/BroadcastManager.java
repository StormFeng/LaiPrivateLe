package com.lailem.app.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lailem.app.utils.Const;

public class BroadcastManager {

    /**
     * 发送登陆万历的广播
     *
     * @param context
     */
    public static void sendLoginBroadcast(Context context) {
        Intent intent = new Intent(AccountBroadcastReceiver.ACTION_LOGIN);
        context.sendBroadcast(intent);
    }

    /**
     * 发送注销成功的广播
     *
     * @param context
     */
    public static void sendLogoutBroadcast(Context context) {
        Intent intent = new Intent(AccountBroadcastReceiver.ACTION_LOGOUT);
        context.sendBroadcast(intent);
    }

    /**
     * 发送注册成功的广播
     *
     * @param context
     */
    public static void sendRegisterBroadcast(Context context) {
        Intent intent = new Intent(AccountBroadcastReceiver.ACTION_REGISTER);
        context.sendBroadcast(intent);
    }

    /**
     * 发送活动同步完成的广播
     *
     * @param context
     */
    public static void sendActivitySnycOverBroadcast(Context context) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_ACTIVITY_SNYC_OVER);
        context.sendBroadcast(intent);
    }

    /**
     * 发送群同步完成的广播
     *
     * @param context
     */
    public static void sendGroupSnycOverBroadcast(Context context) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_GROUP_SNYC_OVER);
        context.sendBroadcast(intent);
    }


    /**
     * 发送加群或新建群的广播
     *
     * @param context
     */
    public static void sendJoinGroupBroadcast(Context context) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_JOIN_GROUP);
        context.sendBroadcast(intent);
    }

    /**
     * 发送退或解散群的广播
     *
     * @param context
     */
    public static void sendExitGroupBroadcast(Context context, String groupId) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_EXIT_GROUP);
        intent.putExtra("groupId", groupId);
        context.sendBroadcast(intent);
    }

    /**
     * 发送更新群资料广播
     *
     * @param context
     */
    public static void sendUpdateGroupBroadcast(Context context, String groupId) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_UPDATE_GROUP);
        intent.putExtra("groupId", groupId);
        context.sendBroadcast(intent);
    }

    /**
     * 发送参加活动或新建活动的广播
     *
     * @param context
     */
    public static void sendJoinActivityBroadcast(Context context) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_JOIN_ACTIVITY);
        context.sendBroadcast(intent);
    }

    /**
     * 发送退出活动或解散活动的广播
     *
     * @param context
     */
    public static void sendExitActivityBroadcast(Context context, String groupId) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_EXIT_ACTIVITY);
        intent.putExtra("groupId", groupId);
        context.sendBroadcast(intent);
    }

    /**
     * 发送更新活动的广播
     *
     * @param context
     */
    public static void sendUpdateActivityBroadcast(Context context, String groupId) {
        Intent intent = new Intent(GroupBroadcastReceiver.ACTION_UPDATE_ACTIVITY);
        intent.putExtra("groupId", groupId);
        context.sendBroadcast(intent);
    }

    /**
     * 注册帐户相关操作的广播：登陆、注册、注销
     *
     * @param context
     * @param accountBroadcastReceiver
     */
    public static void registerAccountBroadcastReceiver(Context context, AccountBroadcastReceiver accountBroadcastReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AccountBroadcastReceiver.ACTION_LOGIN);
        filter.addAction(AccountBroadcastReceiver.ACTION_LOGOUT);
        filter.addAction(AccountBroadcastReceiver.ACTION_REGISTER);
        context.registerReceiver(accountBroadcastReceiver, filter);
    }


    /**
     * 注销帐户相关操作的广播：登陆、注册、注销
     *
     * @param context
     * @param accountBroadcastReceiver
     */
    public static void unRegisterAccountBroadcastReceiver(Context context, AccountBroadcastReceiver accountBroadcastReceiver) {
        context.unregisterReceiver(accountBroadcastReceiver);
    }

    /**
     * 注册group相关操作的广播：群同步、活动同步
     *
     * @param context
     * @param groupBroadcastReceiver
     */
    public static void registerGroupBroadcastReceiver(Context context, GroupBroadcastReceiver groupBroadcastReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GroupBroadcastReceiver.ACTION_ACTIVITY_SNYC_OVER);
        filter.addAction(GroupBroadcastReceiver.ACTION_GROUP_SNYC_OVER);
        filter.addAction(GroupBroadcastReceiver.ACTION_JOIN_ACTIVITY);
        filter.addAction(GroupBroadcastReceiver.ACTION_EXIT_ACTIVITY);
        filter.addAction(GroupBroadcastReceiver.ACTION_JOIN_GROUP);
        filter.addAction(GroupBroadcastReceiver.ACTION_EXIT_GROUP);
        filter.addAction(GroupBroadcastReceiver.ACTION_UPDATE_ACTIVITY);
        filter.addAction(GroupBroadcastReceiver.ACTION_UPDATE_GROUP);
        context.registerReceiver(groupBroadcastReceiver, filter);
    }

    /**
     * 注销group相关操作的广播：群同步、活动同步
     *
     * @param context
     * @param groupBroadcastReceiver
     */
    public static void unRegisterGroupBroadcastReceiver(Context context, GroupBroadcastReceiver groupBroadcastReceiver) {
        context.unregisterReceiver(groupBroadcastReceiver);
    }

    /**
     * 注册会话设置广播
     *
     * @param context
     * @param conversationSetBroadcastReceiver
     */
    public static void registerConversationSetBroadcastReceiver(Context context, ConversationSetBroadcastReceiver conversationSetBroadcastReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConversationSetBroadcastReceiver.ACTION_SET_TOP);
        filter.addAction(ConversationSetBroadcastReceiver.ACTION_NO_TIP);
        filter.addAction(ConversationSetBroadcastReceiver.ACTION_CLEAR_MSG_RECORD);
        context.registerReceiver(conversationSetBroadcastReceiver, filter);
    }

    /**
     * 注销会话设置广播
     *
     * @param context
     * @param conversationSetBroadcastReceiver
     */
    public static void unRegisterConversationSetBroadcastReceiver(Context context, ConversationSetBroadcastReceiver conversationSetBroadcastReceiver) {
        context.unregisterReceiver(conversationSetBroadcastReceiver);
    }

    public static void sendSetTopBroadcast(Context context, String conversationId, String isTop) {
        Intent intent = new Intent(ConversationSetBroadcastReceiver.ACTION_SET_TOP);
        intent.putExtra("conversationId", conversationId);
        intent.putExtra("isTop", isTop);
        context.sendBroadcast(intent);
    }

    public static void sendNoTipBroadcast(Context context, String conversationId, String isNoTip) {
        Intent intent = new Intent(ConversationSetBroadcastReceiver.ACTION_NO_TIP);
        intent.putExtra("conversationId", conversationId);
        intent.putExtra("isNoTip", isNoTip);
        context.sendBroadcast(intent);
    }

    public static void sendClearMsgRecordBroadcast(Context context, String conversationId) {
        Intent intent = new Intent(ConversationSetBroadcastReceiver.ACTION_CLEAR_MSG_RECORD);
        intent.putExtra("conversationId", conversationId);
        context.sendBroadcast(intent);
    }

    public static void registerGroupOrActiveNotExistReceiver(Context context, GroupOrActiveNoExistReceiver groupOrActiveExistBroadcastReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GroupOrActiveNoExistReceiver.ACTION_GROUP_NO_EXIST);
        filter.addAction(GroupOrActiveNoExistReceiver.ACTION_ACTIVE_NO_EXIST);
        context.registerReceiver(groupOrActiveExistBroadcastReceiver, filter);
    }

    public static void unRegisterGroupOrActiveNoExistReceiver(Context context, GroupOrActiveNoExistReceiver groupOrActiveExistBroadcastReceiver) {
        context.unregisterReceiver(groupOrActiveExistBroadcastReceiver);
    }

    public static void sendGroupOrActiveNoExistBroadcast(Context context, String action, String groupId) {
        Intent intent = new Intent(ConversationSetBroadcastReceiver.ACTION_CLEAR_MSG_RECORD);
        intent.setAction(action);
        intent.putExtra(Const.BUNDLE_KEY_GROUP_ID, groupId);
        context.sendBroadcast(intent);
    }

    public static void registerDynamicTaskReceiver(Context context, DynamicTaskReceiver dynamicTaskReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(dynamicTaskReceiver.ACTION_ADD_TASK);
        filter.addAction(dynamicTaskReceiver.ACTION_TASK_EMPTY);
        filter.addAction(dynamicTaskReceiver.ACTION_TASK_FAIL);
        context.registerReceiver(dynamicTaskReceiver, filter);
    }

    public static void unRegisterDynamicTaskReceiver(Context context, DynamicTaskReceiver dynamicTaskReceiver) {
        context.unregisterReceiver(dynamicTaskReceiver);
    }

    public static void sendDynamicTaskBroadcast(Context context, String action, String key) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(DynamicTaskReceiver.BUNDLE_KEY_TASK_KEY, key);
        context.sendBroadcast(intent);
    }

    public static void registerBaiduLocReceiver(Context context, BaiduLocReceiver baiduLocReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(baiduLocReceiver.ACTION_BAIDU_LOC_SUCCESS);
        filter.addAction(baiduLocReceiver.ACTION_BAIDU_LOC_FAILURE);
        context.registerReceiver(baiduLocReceiver, filter);
    }

    public static void unRegisterBaiduLocReceiver(Context context, BaiduLocReceiver baiduLocReceiver) {
        context.unregisterReceiver(baiduLocReceiver);
    }

    public static void sendBaiduLocBroadcast(Context context, String action, String lat, String lon, String provinceName, String cityName, String address) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(BaiduLocReceiver.BUNDLE_KEY_LAT, lat);
        intent.putExtra(BaiduLocReceiver.BUNDLE_KEY_LON, lon);
        intent.putExtra(BaiduLocReceiver.BUNDLE_KEY_PROVINCE, provinceName);
        intent.putExtra(BaiduLocReceiver.BUNDLE_KEY_CITY, cityName);
        intent.putExtra(BaiduLocReceiver.BUNDLE_KEY_ADDRESS, address);
        context.sendBroadcast(intent);
    }
}
