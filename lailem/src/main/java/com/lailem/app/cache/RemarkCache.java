package com.lailem.app.cache;

import android.content.Context;

import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Remark;
import com.socks.library.KLog;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author leeib
 * 
 */
public class RemarkCache extends Cache<Remark> {
	private static RemarkCache instance;
	Context context;

	private RemarkCache(Context context) {
		super("getToUserId");
		this.context = context;
	}

	public static RemarkCache getInstance(Context context) {
		if (instance == null)
			instance = new RemarkCache(context);
		return instance;
	}

	@Override
	public Remark get(Object userId) {
		try {
			// 缓存中包含用户数据从缓存中取
			if (super.get(userId)!=null) {
				return super.get(userId);
				// 缓存中不包含用户数据从数据库中取并加入缓存
			} else {
				Remark remark = DaoOperate.getInstance(context).queryRemark(userId.toString());
				if (remark != null) {
					super.put(remark);
					return remark;
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
	 * @param remark
	 */
	@Override
	public void put(final Remark remark) {
		new Thread(new Runnable() {

			@Override
			public void run() {

                KLog.i("进行放入备注名");
				// 无表id
				if (remark.getId() == null) {

					Remark remark2 = DaoOperate.getInstance(context).queryRemark(remark.getToUserId());
					// 数据库中无记录
					if (remark2 == null) {
                        KLog.i("备注名不在库，执行放入");
						long id = DaoOperate.getInstance(context).insert(remark);
						remark.setId(id);
					} else {// 有记录
                        KLog.i("备注名在库，进行更新");
						remark.setId(remark2.getId());
						DaoOperate.getInstance(context).update(remark);
					}
				} else {
                    KLog.i("备注名更新");
					DaoOperate.getInstance(context).update(remark);
				}
                KLog.i("将备注名加入容器");
				RemarkCache.super.put(remark);

			}
		}).start();
	}

	public void init() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<Remark> remarks = DaoOperate.getInstance(context).queryRemarks(maxSize);
				for (Remark remark : remarks) {
					RemarkCache.super.put(remark);
				}

			}
		}).start();
	}

	public void init(final Collection<String> userIds) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<Remark> remarks = DaoOperate.getInstance(context).queryRemarks(userIds);
				for (Remark remark : remarks) {
					RemarkCache.super.put(remark);
				}
			}
		}).start();
	}

}
