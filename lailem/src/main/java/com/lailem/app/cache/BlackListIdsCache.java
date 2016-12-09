package com.lailem.app.cache;

import android.content.Context;

import com.lailem.app.dao.BlacklistIds;
import com.lailem.app.dao.DaoOperate;
import com.socks.library.KLog;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author XuYang
 *
 */
public class BlackListIdsCache extends Cache<BlacklistIds> {
	private static BlackListIdsCache instance;
	Context context;

	private BlackListIdsCache(Context context) {
		super("getBlackUserId");
		this.context = context;
	}

	public static BlackListIdsCache getInstance(Context context) {
		if (instance == null)
			instance = new BlackListIdsCache(context);
		return instance;
	}

	@Override
	public BlacklistIds get(Object userId) {
		try {
			// 缓存中包含用户数据从缓存中取
			if (super.get(userId) != null) {
				return super.get(userId);
				// 缓存中不包含用户数据从数据库中取并加入缓存
			} else {
				BlacklistIds blacklistIds = DaoOperate.getInstance(context).queryBlacklistIds(userId.toString());
				if (blacklistIds != null) {
					super.put(blacklistIds);
					return blacklistIds;
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
	 * @param blacklistIds
	 */
	@Override
	public void put(final BlacklistIds blacklistIds) {
		new Thread(new Runnable() {

			@Override
			public void run() {

                KLog.i("进行放入黑名单");
				// 无表id
				if (blacklistIds.getId() == null) {

					BlacklistIds blacklistIds2 = DaoOperate.getInstance(context).queryBlacklistIds(blacklistIds.getBlackUserId());
					// 数据库中无记录
					if (blacklistIds2 == null) {
                        KLog.i("黑名单不在库，执行放入");
						long id = DaoOperate.getInstance(context).insert(blacklistIds);
						blacklistIds.setId(id);
					} else {// 有记录
                        KLog.i("黑名单在库，进行更新");
						blacklistIds.setId(blacklistIds2.getId());
						DaoOperate.getInstance(context).update(blacklistIds);
					}
				} else {
                    KLog.i("黑名单更新");
					DaoOperate.getInstance(context).update(blacklistIds);
				}
                KLog.i("将黑名单加入容器");
				BlackListIdsCache.super.put(blacklistIds);

			}
		}).start();
	}

	public void init() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<BlacklistIds> blacklistIdsList = DaoOperate.getInstance(context).queryBlacklistIds(maxSize);
				for (BlacklistIds blacklistIds : blacklistIdsList) {
					BlackListIdsCache.super.put(blacklistIds);
				}

			}
		}).start();
	}

	public void init(final Collection<String> userIds) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<BlacklistIds> blacklistIdsList = DaoOperate.getInstance(context).queryBlacklistIds(userIds);
				for (BlacklistIds blacklistIds : blacklistIdsList) {
					BlackListIdsCache.super.put(blacklistIds);
				}
			}
		}).start();
	}

}
