package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DynamicTaskReceiver extends BroadcastReceiver {
    public static final String BUNDLE_KEY_TASK_KEY = "task";

    public static final String ACTION_ADD_TASK = "ADD_TASK";
    public static final String ACTION_TASK_FAIL = "TASK_FAIL";
    public static final String ACTION_TASK_EMPTY = "TASK_EMPTY";

    @Override
    public void onReceive(Context content, Intent intent) {

    }

}
