package com.lailem.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;

import com.lailem.app.broadcast.AccountBroadcastReceiver;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.DynamicTaskReceiver;
import com.lailem.app.chat.util.ChatManager;
import com.lailem.app.chat.util.FloatWindowManager;
import com.lailem.app.utils.DynamicTaskUtil;
import com.lailem.app.utils.SyncUtils;
import com.socks.library.KLog;

public class LLService extends Service {
    public final static String FLAG = "flag";
    /**
     * 在onStartCommand方法中，不执行任何操作
     */
    public final static int FLAG_DEFAULT = 0;
    /**
     * 在onStartCommand方法中，登陆IM
     */
    public final static int FLAG_LOGIN_IM = 1;
    ChatManager chatManager;
    PowerManager.WakeLock wakeLock;


    AccountBroadcastReceiver accountBroadcastReceiver = new AccountBroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (AccountBroadcastReceiver.ACTION_LOGIN.equals(action)) {
                chatManager.login(getApplicationContext());
                // 同步
                SyncUtils snycUtils = new SyncUtils();
                snycUtils.start(getApplicationContext());

            } else if (AccountBroadcastReceiver.ACTION_REGISTER.equals(action)) {
                chatManager.login(getApplicationContext());
            } else if (AccountBroadcastReceiver.ACTION_LOGOUT.equals(action)) {
                chatManager.logout(getApplicationContext());
            }
        }

        ;
    };

    private DynamicTaskReceiver dynamicTaskReceiver = new DynamicTaskReceiver() {
        @Override
        public void onReceive(Context content, Intent intent) {
            String key = intent.getStringExtra(dynamicTaskReceiver.BUNDLE_KEY_TASK_KEY);
            //添加新任务
            if (DynamicTaskReceiver.ACTION_ADD_TASK.equals(intent.getAction())) {
                DynamicTaskUtil.getInstance().handleTask(key, DynamicTaskUtil.STATE_ADD);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i("llservice onCreate");
//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "IM_WAKELOCK");
//        wakeLock.acquire();

        Intent intent = new Intent(getApplicationContext(),LLService.class);
        intent.putExtra(FLAG, FLAG_LOGIN_IM);
        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
        long timeSpace = 30*60*1000;
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+timeSpace, timeSpace, pi);

        BroadcastManager.registerAccountBroadcastReceiver(getApplicationContext(), accountBroadcastReceiver);
        chatManager = new ChatManager();
        BroadcastManager.registerDynamicTaskReceiver(getApplicationContext(), dynamicTaskReceiver);

        FloatWindowManager.createFloatWindow(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.i("onStartCommand");
        int flag = intent.getIntExtra(FLAG, FLAG_DEFAULT);
        KLog.i("llservice flag:::" + flag);
        if (FLAG_LOGIN_IM == flag) {
            chatManager.login(getApplicationContext());
        }
//		return super.onStartCommand(intent, Service.START_STICKY, startId);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
        }
        BroadcastManager.unRegisterAccountBroadcastReceiver(getApplicationContext(), accountBroadcastReceiver);

        BroadcastManager.unRegisterDynamicTaskReceiver(getApplicationContext(), dynamicTaskReceiver);

        //处理动态发送中任务
        DynamicTaskUtil.resetDynamicTask(this);

    }

    /**
     * 启动Service
     *
     * @param context
     * @param flag    控制在onStartCommand执行操作的标志
     */
    public static void start(Context context, int flag) {
        Intent intent = new Intent(context, LLService.class);
        intent.putExtra(FLAG, flag);
        context.startService(intent);
    }

    /**
     * 停止Service
     *
     * @param context
     */
    public static void stop(Context context) {
        context.stopService(new Intent(context, LLService.class));
    }

}
