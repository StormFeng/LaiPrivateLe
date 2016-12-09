package com.lailem.app.runnable.sync;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.jsonbean.dynamic.UserRemarksBean;
import com.lailem.app.jsonbean.dynamic.UserRemarksBean.Remark;
import com.lailem.app.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class SyncRemarkRunnable implements Runnable {
	Context context;

	public SyncRemarkRunnable(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			Result result = ApiClient.getApi().getRemarks(AppContext.getInstance().getLoginUid());
			if(Const.value_success.equals(result.ret)){
				UserRemarksBean userRemarkBean = (UserRemarksBean) result;
				ArrayList<Remark> remarkList = userRemarkBean.getRemarkList();
				
				if (remarkList != null && remarkList.size() > 0) {
					List<com.lailem.app.dao.Remark> dbRemarkList = DaoOperate.getInstance(context).queryRemarks();
					// 新增的或需要更新的
					ArrayList<com.lailem.app.dao.Remark> addOrUpdateRemarks = new ArrayList<com.lailem.app.dao.Remark>();

					for (Remark remark : remarkList) {
						com.lailem.app.dao.Remark r = null;
						for (com.lailem.app.dao.Remark remark1 : dbRemarkList) {
							if (remark.getRemUserId().equals(remark1.getToUserId())) {
								r = remark1;
								break;
							}
						}

						if (r != null) {// 数据库有
							r.setRemark(remark.getRem());
							// 从dbActives集合中移除已经匹配到的，最后剩下没有匹配的就是需要删除的
							dbRemarkList.remove(r);
						} else {// 本地数据库没有
							r = new com.lailem.app.dao.Remark();
							r.setUserId(AppContext.getInstance().getLoginUid());
							r.setRemark(remark.getRem());
							r.setCreateTime(System.currentTimeMillis());
							r.setUpdateTime(System.currentTimeMillis());
							r.setToUserId(remark.getRemUserId());
						}
						addOrUpdateRemarks.add(r);
					}

					DaoOperate.getInstance(context).insertOrReplaceInTxRemarks(addOrUpdateRemarks);
					// 删除没有匹配的
					if (dbRemarkList.size() > 0) {
						DaoOperate.getInstance(context).delete(dbRemarkList);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}

}
