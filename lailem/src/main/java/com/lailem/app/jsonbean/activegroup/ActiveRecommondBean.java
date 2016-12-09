package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-推荐或热门活动列表
 * 
 * @author MD-12
 *
 */
public class ActiveRecommondBean extends Result {
	private ArrayList<ActiveList> activityList;// 活动列表 array<object>

	public ArrayList<ActiveList> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<ActiveList> activityList) {
		this.activityList = activityList;
	}

	public static ActiveRecommondBean parse(String json) throws AppException {
		ActiveRecommondBean res = new ActiveRecommondBean();
		try {
			res = gson.fromJson(json, ActiveRecommondBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class ActiveList extends Result {
		private String address;// 地址 string
		private String bPicName;// 大图文件名 string
		private String createTime;// string
		private CreatorInfo creatorInfo;// 创建者信息 object
		private String currCount;// 当前人数 string
		private String distance;// 距离 string
								// 距离，单位需由服务器计算好，前端只作显示，距离单位规则见【公共信息】模块下的距离单位规则
		private String id;// id string
		private String intro;// 简介 string
		private String name;// 名称 string
		private String startTime;// 开始时间 string 格式：2015-05-26 16:43:43
		private String typeId;// 所属分类类型id string

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getbPicName() {
			return bPicName;
		}

		public void setbPicName(String bPicName) {
			this.bPicName = bPicName;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public CreatorInfo getCreatorInfo() {
			return creatorInfo;
		}

		public void setCreatorInfo(CreatorInfo creatorInfo) {
			this.creatorInfo = creatorInfo;
		}

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

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

	}

	public static class CreatorInfo extends Result {
		private String headSPicName;// 小头像文件名 string
		private String id;// 创建者id string
		private String nickname;// 昵称 string

		public String getHeadSPicName() {
			return headSPicName;
		}

		public void setHeadSPicName(String headSPicName) {
			this.headSPicName = headSPicName;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

	}
}
