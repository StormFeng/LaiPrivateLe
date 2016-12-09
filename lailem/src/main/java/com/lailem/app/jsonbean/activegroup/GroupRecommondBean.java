package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 活动与群组-推荐或热门活动列表
 * 
 * @author MD-12
 *
 */
public class GroupRecommondBean extends Result {
	private GroupList groupList;// 群组列表 array<object>

	public GroupList getGroupList() {
		return groupList;
	}

	public void setGroupList(GroupList groupList) {
		this.groupList = groupList;
	}

	public static GroupRecommondBean parse(String json) throws AppException {
		GroupRecommondBean res = new GroupRecommondBean();
		try {
			res = gson.fromJson(json, GroupRecommondBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class GroupList extends Result {
		private String currCount;// 当前人数 string
		private String distance;// 距离 string
								// 距离，单位需由服务器计算好，前端只作显示，距离单位规则见【公共信息】模块下的距离单位规则
		private String id;// id string
		private String intro;// 简介 string
		private String name;// 名称 string
		private String squareSPicName;// 正方形小图文件名 string
		private String typeName;// 群类型名称 string

		public String getCurrCount() {
			return currCount;
		}

		public void setCurrCount(String currCount) {
			this.currCount = currCount;
		}

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIntro() {
			return intro;
		}

		public void setIntro(String intro) {
			this.intro = intro;
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

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

	}
}
