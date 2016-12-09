package com.lailem.app.runnable.sync;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.jsonbean.personal.BlacklistIdsBean;

import java.util.ArrayList;
import java.util.List;

public class SyncBlacklistIdsRunnable implements Runnable {
	Context context;

	public SyncBlacklistIdsRunnable(Context context) {
		this.context = context;
	}

	@Override
	public void run() {

		try {
			Result result = ApiClient.getApi().blacklistIds(AppContext.getInstance().getLoginUid());
			if (result != null && result.isOK()) {
				BlacklistIdsBean blacklistIdsBean = (BlacklistIdsBean) result;
				ArrayList<String> blacklistIds = blacklistIdsBean.getBlacklistIds();

				if (blacklistIds != null && blacklistIds.size() > 0) {
					List<com.lailem.app.dao.BlacklistIds> dbBlicklistIds = DaoOperate.getInstance(context).queryBlacklistIds();
					// 新增的或需要更新的
					ArrayList<com.lailem.app.dao.BlacklistIds> addOrUpdateBlackListIds = new ArrayList<com.lailem.app.dao.BlacklistIds>();

					for (String blacklistId : blacklistIds) {
						com.lailem.app.dao.BlacklistIds b = null;
						for (com.lailem.app.dao.BlacklistIds blacklistId1 : dbBlicklistIds) {
							if (blacklistId.equals(blacklistId1.getBlackUserId())) {
								b = blacklistId1;
								break;
							}
						}

						if (b != null) {// 数据库有
							// 从dbActives集合中移除已经匹配到的，最后剩下没有匹配的就是需要删除的
							dbBlicklistIds.remove(b);
						} else {// 本地数据库没有
							b = new com.lailem.app.dao.BlacklistIds();
							b.setUserId(AppContext.getInstance().getLoginUid());
							b.setCreateTime(System.currentTimeMillis());
							b.setBlackUserId(blacklistId);
							addOrUpdateBlackListIds.add(b);
						}
					}
				    if(addOrUpdateBlackListIds.size()>0){
				    	DaoOperate.getInstance(context).insertOrReplaceInTxBlacklistIds(addOrUpdateBlackListIds);
				    }
					// 删除没有匹配的
					if (dbBlicklistIds.size() > 0) {
						DaoOperate.getInstance(context).deleteBlacklistIds(dbBlicklistIds);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
