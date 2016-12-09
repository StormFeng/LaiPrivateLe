package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-附近活动列表
 * 
 * @author MD-12
 *
 */
public class GroupActiveListBean extends Result {
	private ArrayList<GroupActive> activityList;// 活动列表 array<object>

	public static GroupActiveListBean parse(String json) throws AppException {
		GroupActiveListBean res = new GroupActiveListBean();
		try {
			res = gson.fromJson(json, GroupActiveListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public ArrayList<GroupActive> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<GroupActive> activityList) {
		this.activityList = activityList;
	}

	public static class GroupActive extends Result {
		private String address;// 地址 string 服务器控制显示格式
		private String bPicName;// 大图文件名 string
		private String createTime;// string
		private CreatorInfo creatorInfo;// 创建者信息 object
		private String currCount;// 当前人数 string
		private String id;// id string
		private String intro;// 简介 string
		private String name;// 名称 string
		private String startTime;// 开始时间 string 时间格式见【公共信息】模块里的【时间格式】，服务器控制显示格式
		private String typeId;// 所属分类类型id string
		private String timeStamp;
        private String typeColor;
        private String remark;

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }

        public String getTypeColor() {
            return typeColor;
        }

        public void setTypeColor(String typeColor) {
            this.typeColor = typeColor;
        }

        public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}

		public String getTimeStamp() {
			return timeStamp;
		}

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
