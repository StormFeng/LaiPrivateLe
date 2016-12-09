package com.lailem.app.jsonbean.activegroup;

import com.lailem.app.bean.Result;

public class RecentNotice extends Result {
	private String createTime;// string
	private String detail;// 详情 string
	private String id;//
	private String topic;// 主题 string

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

}