package com.lailem.app.utils;

import android.content.Context;

import com.lailem.app.runnable.sync.SyncBlacklistIdsRunnable;
import com.lailem.app.runnable.sync.SyncGroupRunnable;
import com.lailem.app.runnable.sync.SyncRemarkRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncUtils {
	ExecutorService pool;

	public SyncUtils() {
		pool = Executors.newFixedThreadPool(1);
	}

	public void start(Context context) {
		// group
		pool.submit(new SyncGroupRunnable(context));
		//备注名
		pool.submit(new SyncRemarkRunnable(context));
		//黑名单
		pool.submit(new SyncBlacklistIdsRunnable(context));
	}

}
