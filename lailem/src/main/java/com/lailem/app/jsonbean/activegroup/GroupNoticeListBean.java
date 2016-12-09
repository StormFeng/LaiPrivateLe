package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.PublisherInfo;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

public class GroupNoticeListBean extends Result {

	private ArrayList<GroupNotice> noticeList;

	public static GroupNoticeListBean parse(String json) throws AppException {
		GroupNoticeListBean res = new GroupNoticeListBean();
		try {
			res = gson.fromJson(json, GroupNoticeListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public ArrayList<GroupNotice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(ArrayList<GroupNotice> noticeList) {
		this.noticeList = noticeList;
	}

	public static class GroupNotice extends Result {
		private String createTime;// string
		private String detail;// 详情 string
		private String id;// string
		private String topic;// 主题 string
		private PublisherInfo publisherInfo;

        public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

		public PublisherInfo getPublisherInfo() {
			return publisherInfo;
		}

		public void setPublisherInfo(PublisherInfo publisherInfo) {
			this.publisherInfo = publisherInfo;
		}
		
		

	}
}
