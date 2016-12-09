package com.lailem.app.cache;

import android.content.Context;

import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.User;

import java.util.Collection;
import java.util.List;

/**
 * 仅限于即时聊天使用,二级缓存：内存与数据库
 * 
 * @author leeib
 * 
 */
public class UserCache extends Cache<User> {
	private static UserCache instance;
	Context context;

	private UserCache(Context context) {
		super("getUserId");
		this.context = context;
	}

	public static UserCache getInstance(Context context) {
		if (instance == null)
			instance = new UserCache(context);
		return instance;
	}

	@Override
	public User get(Object userId) {
		try {
			// 缓存中包含用户数据从缓存中取
			if (super.get(userId) != null) {
				return super.get(userId);
				// 缓存中不包含用户数据从数据库中取并加入缓存
			} else {
				User user = DaoOperate.getInstance(context).query(
						userId.toString());
				if (user != null) {
					super.put(user);
					return user;
				}
				return null;
			}

		} catch (NullPointerException ex) {
			return null;
		}
	}

	/**
	 * 加入缓存，数据库没有时会存入数据库,数据库有时会更新记录
	 * 
	 * @param user
	 */
	@Override
	public void put(final User user) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				putSync(user);
			}
		}).start();
	}

	/**
	 * 加入缓存，数据库没有时会存入数据库,数据库有时会更新记录
	 * 
	 * @param user
	 */
	public void putSync(User user) {
		// 无表id
		if (user.getId() == null) {
			User user2 = DaoOperate.getInstance(context)
					.query(user.getUserId());
			// 数据库中无记录
			if (user2 == null) {
				long id = DaoOperate.getInstance(context).insert(user);
				user.setId(id);
			} else {// 有记录
				user.setId(user2.getId());
				DaoOperate.getInstance(context).update(user);
			}
		} else {
			DaoOperate.getInstance(context).update(user);
		}
		UserCache.super.put(user);
	}

	public void init() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<User> users = DaoOperate.getInstance(context).query(
						maxSize);
				for (User user : users) {
					UserCache.super.put(user);
				}

			}
		}).start();
	}

	public void init(final Collection<String> userIds) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<User> users = DaoOperate.getInstance(context).query(
						userIds);
				for (User user : users) {
					UserCache.super.put(user);
				}
			}
		}).start();
	}

}
