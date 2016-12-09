package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.Contact;

import java.util.ArrayList;

/**
 * 活动与群组-我的活动与群组--取group日程
 * 
 * @author MD-12
 *
 */
public class GroupScheduleBean extends Result {

	private ArrayList<GroupSchedule> scheduleList;// 日程列表 array<object>

	public ArrayList<GroupSchedule> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(ArrayList<GroupSchedule> scheduleList) {
		this.scheduleList = scheduleList;
	}

	public static GroupScheduleBean parse(String json) throws AppException {
		GroupScheduleBean res = new GroupScheduleBean();
		try {
			res = gson.fromJson(json, GroupScheduleBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class GroupSchedule extends Result {
		private String addTime;// string
		private String address;// 地址 string
		private String content;// 通知内容 string
		private CreatorInfo creatorInfo;// 创建者信息 object
		private String id;// string
		private String lat;// 纬度 string
		private String lon;// 经度 string
		private String startTime;// 开始时间 string
		private String topic;// 主题 string

		private ArrayList<Contact> contact;

		public void setContact(ArrayList<Contact> contact) {
			this.contact = contact;
		}

		public ArrayList<Contact> getContact() {
			return contact;
		}

		public String getAddTime() {
			return addTime;
		}

		public void setAddTime(String addTime) {
			this.addTime = addTime;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public CreatorInfo getCreatorInfo() {
			return creatorInfo;
		}

		public void setCreatorInfo(CreatorInfo creatorInfo) {
			this.creatorInfo = creatorInfo;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLon() {
			return lon;
		}

		public void setLon(String lon) {
			this.lon = lon;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

	}

	public static class CreatorInfo extends Result {
		private String headSPicName;// 小头像文件名 string
		private String id;// 创建者id string
		private String nickname;// 昵称 string
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

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
