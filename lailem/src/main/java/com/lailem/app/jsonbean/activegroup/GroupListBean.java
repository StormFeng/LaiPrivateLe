package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-我的活动与群组--我的群组列表
 * 
 * @author MD-12
 *
 */
public class GroupListBean extends Result {

	private ArrayList<Group> groupList;// 群组列表 array<object>

	public ArrayList<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<Group> groupList) {
		this.groupList = groupList;
	}

	public static GroupListBean parse(String json) throws AppException {
		GroupListBean res = new GroupListBean();
		try {
			res = gson.fromJson(json, GroupListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Group extends Result {
		private String id;// id string
		private String name;// 名称 string
		private String squareSPicName;// 正方形小图文件名 string

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

		@Override
		public String toString() {
			return "Group [id=" + id + ", name=" + name + ", squareSPicName=" + squareSPicName + "]";
		}

		public void setSquareSPicName(String squareSPicName) {
			this.squareSPicName = squareSPicName;
		}

	}

	@Override
	public String toString() {
		return "GroupListBean [groupList=" + groupList + "]";
	}
	
	
}
