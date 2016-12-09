package com.lailem.app.utils;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.jsonbean.activegroup.GroupBrief;
import com.socks.library.KLog;

public class GroupBriefUtil {

	public static void getGroupBriefFromNetBySync(String groupId, String gType,Context context) {
		try {
			Group group = DaoOperate.getInstance(context).queryGroup(groupId);
			if(group==null){//本地没有则从网络获取
				Result result = ApiClient.getApi().groupBrief(AppContext.getInstance().getLoginUid(), groupId);
				if (result != null && result.isOK()) {
					GroupBrief groupBrief = (GroupBrief) result;
					group = new Group();
					group.setCreateTime(System.currentTimeMillis());
					group.setUpdateTime(System.currentTimeMillis());
					group.setGroupId(groupId);
					group.setName(groupBrief.getGroupInfo().getName());
					group.setSquareSPic(groupBrief.getGroupInfo().getSquareSPicName());
					group.setIntro(groupBrief.getGroupInfo().getIntro());
					group.setGroupType(gType);
					group.setTypeId(groupBrief.getGroupInfo().getTypeId());
                    group.setPermission(Integer.parseInt(groupBrief.getGroupInfo().getPermission()));
					long id = DaoOperate.getInstance(context).insert(group);
					group.setId(id);
					//提前放入缓存中，提高获取时速度
					GroupCache.getInstance(context).put(group);
				} else {
                    KLog.i("获取群简要信息失败");
				}
			}else{
				GroupCache.getInstance(context).put(group);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
