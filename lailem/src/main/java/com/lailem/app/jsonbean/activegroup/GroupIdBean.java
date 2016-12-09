package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 活动与群组-我的活动与群组-新建group
 * 
 * @author MD-12
 *
 */
public class GroupIdBean extends Result {

	private GroupInfo groupInfo;// 刚新建的groupId string

	public GroupInfo getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}

	public static GroupIdBean parse(String json) throws AppException {
		GroupIdBean res = new GroupIdBean();
		try {
			res = gson.fromJson(json, GroupIdBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class GroupInfo extends Result {
		private String id;// string
		private String name;// 名称 string
		private String squareSPicName;//

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSquareSPicName() {
			return squareSPicName;
		}

		public void setSquareSPicName(String squareSPicName) {
			this.squareSPicName = squareSPicName;
		}

	}
}
