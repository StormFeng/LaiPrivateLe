package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.GInfo;
import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgGNotice extends Msg {
	private String detail;
	private String id;
	private String topic;
	private GInfo gInfo;
	private UInfo uInfo;

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

	public GInfo getgInfo() {
		return gInfo;
	}

	public void setgInfo(GInfo gInfo) {
		this.gInfo = gInfo;
	}

	public UInfo getuInfo() {
		return uInfo;
	}

	public void setuInfo(UInfo uInfo) {
		this.uInfo = uInfo;
	}

}
