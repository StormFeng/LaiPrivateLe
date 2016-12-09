package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GroupOrActiveNoExistReceiver extends BroadcastReceiver {

    public static final String ACTION_GROUP_NO_EXIST = "groupNoExist";
    public static final String ACTION_ACTIVE_NO_EXIST = "activeNoExist";

    @Override
    public void onReceive(Context context, Intent intent) {

    }

}
