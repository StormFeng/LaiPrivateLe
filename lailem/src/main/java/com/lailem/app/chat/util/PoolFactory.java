package com.lailem.app.chat.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolFactory {

	private static PoolFactory factory;

	ExecutorService poolForUpload;
	ExecutorService poolForSend;
	ExecutorService poolForMessageIn;

	private PoolFactory() {
		poolForUpload = Executors.newFixedThreadPool(2);
		poolForSend = Executors.newFixedThreadPool(1);
		poolForMessageIn = Executors.newFixedThreadPool(1);
	}

	public static PoolFactory getFactory() {
		if (factory == null)
			factory = new PoolFactory();
		return factory;
	}

	public ExecutorService getPoolForUpload() {
		return poolForUpload;
	}

	public ExecutorService getPoolForSend() {
		return poolForSend;
	}

	public ExecutorService getPoolForMessageIn() {
		return poolForMessageIn;
	}
	
	public void destory(){
		poolForUpload.shutdown();
		poolForUpload = null;
		poolForSend.shutdown();
		poolForSend=null;
		poolForMessageIn.shutdown();
		poolForMessageIn = null;
		factory = null;
	}

}
