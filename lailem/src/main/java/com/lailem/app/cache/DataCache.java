package com.lailem.app.cache;

public class DataCache extends Cache<Object> {
	
	private static DataCache instance;
	
	private DataCache(){
		super();
	}
	
	public static DataCache getInstance(){
		if(instance==null)
			instance = new DataCache();
		return instance;
	}

}
