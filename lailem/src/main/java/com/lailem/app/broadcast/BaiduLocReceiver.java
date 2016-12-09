package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by XuYang on 15/11/24.
 */
public class BaiduLocReceiver extends BroadcastReceiver {

    public static final String ACTION_BAIDU_LOC_SUCCESS = "baidu_loc_success";
    public static final String ACTION_BAIDU_LOC_FAILURE = "baidu_loc_failure";

    public static final String BUNDLE_KEY_LAT = "lat";
    public static final String BUNDLE_KEY_LON = "lon";
    public static final String BUNDLE_KEY_PROVINCE = "province";
    public static final String BUNDLE_KEY_CITY = "city";
    public static final String BUNDLE_KEY_ADDRESS = "address";

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
