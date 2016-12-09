package com.lailem.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lailem.app.service.LLService;
import com.lailem.app.utils.NetStateManager;
import com.socks.library.KLog;

public class LLBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
        KLog.i("action:::" + intent.getAction());
		if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {// 网络状态改变
			if (NetStateManager.getInstance().isNeedReconn()) {//需要重新连接
                KLog.i("需要重新连接");
				LLService.start(context,LLService.FLAG_LOGIN_IM);
			}else{
                KLog.i("不需要重新连接");
			}
		}else if(intent.getAction().equals("android.intent.action.USER_PRESENT")){//解锁屏幕
			if(NetStateManager.getInstance().isAvailableForNow()){
				LLService.start(context,LLService.FLAG_LOGIN_IM);
			}
		}else if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){//开机启动
			if(NetStateManager.getInstance().isAvailableForNow()){
				LLService.start(context,LLService.FLAG_LOGIN_IM);
			}
		}
		
	}

}
