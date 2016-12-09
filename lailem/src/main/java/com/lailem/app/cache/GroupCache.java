package com.lailem.app.cache;

import android.content.Context;

import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;

public class GroupCache extends Cache<Group> {
	
	private static GroupCache instance;
	Context context;
	
	private GroupCache(Context context){
		super("getGroupId");
		this.context = context;
	}
	
	public static GroupCache getInstance(Context context){
		if(instance==null)
			instance = new GroupCache(context);
		return instance;
	}
	
	@Override
	public Group get(Object groupId) {
		if(super.get(groupId)==null){
			Group group = DaoOperate.getInstance(context).queryGroup(groupId.toString());
			super.put(group);
			return group;
		}
		return super.get(groupId);
	}
}
