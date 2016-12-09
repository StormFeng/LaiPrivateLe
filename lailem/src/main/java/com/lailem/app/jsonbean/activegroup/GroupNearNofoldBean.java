package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-附近群组列表-不按地址折叠
 * 
 * @author MD-12
 *
 */
public class GroupNearNofoldBean extends Result {

	private ArrayList<GroupNear> groupList;// 群组或活动列表 array<object>

	public ArrayList<GroupNear> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<GroupNear> groupList) {
		this.groupList = groupList;
	}

	public static GroupNearNofoldBean parse(String json) throws AppException {
		GroupNearNofoldBean res = new GroupNearNofoldBean();
		try {
			res = gson.fromJson(json, GroupNearNofoldBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

}
