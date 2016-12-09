package com.lailem.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lailem.app.dao.DaoMaster.DevOpenHelper;
import com.lailem.app.jni.JniSharedLibraryWrapper;

public class DaoFactory {

	private static DaoFactory instance;
	DaoSession daoSession;
	private SQLiteDatabase db;

	private DaoFactory(Context context) {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
				JniSharedLibraryWrapper.dbName(), null);
		db = helper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
	}
	
	public static DaoFactory getInstance(Context context) {
		if (instance == null)
			instance = new DaoFactory(context);
		return instance;
	}
	
	public SQLiteDatabase getDb(){
		return db;
	}

	public MessageDao getMessageDao() {
		return daoSession.getMessageDao();
	}

	public GroupDao getGroupDao() {
		return daoSession.getGroupDao();
	}

	public MGroupDao getMGroupDao() {
		return daoSession.getMGroupDao();
	}

	
	public UserDao getUserDao() {
		return daoSession.getUserDao();
	}

	public ConversationDao getConversationDao() {
		return daoSession.getConversationDao();
	}
	
	public RegionDao getRegionDao(){
		return daoSession.getRegionDao();
	}
	public SysPropertyDao getSysPropertyDao(){
		return daoSession.getSysPropertyDao();
	}
	
	public RemarkDao getRemarkDao(){
		return daoSession.getRemarkDao();
	}
	
	public BlacklistIdsDao getBlacklistIdsDao(){
		return daoSession.getBlacklistIdsDao();
	}

}
