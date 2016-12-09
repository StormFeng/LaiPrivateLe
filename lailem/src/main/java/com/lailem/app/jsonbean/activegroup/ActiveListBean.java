package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-我的活动与群组-我的活动列表
 * 
 * @author MD-12
 *
 */
public class ActiveListBean extends Result {

	private ArrayList<Active> activityList;// 活动列表 array<object>

	public ArrayList<Active> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<Active> activityList) {
		this.activityList = activityList;
	}

	public static ActiveListBean parse(String json) throws AppException {
		ActiveListBean res = new ActiveListBean();
		try {
			res = gson.fromJson(json, ActiveListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Active extends Result {
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

		public void setSquareSPicName(String squareSPicName) {
			this.squareSPicName = squareSPicName;
		}

		@Override
		public String toString() {
			return "Active [id=" + id + ", name=" + name + ", squareSPicName=" + squareSPicName + "]";
		}

	}

	@Override
	public String toString() {
		return "ActiveListBean [activityList=" + activityList + "]";
	}
	
	
}
