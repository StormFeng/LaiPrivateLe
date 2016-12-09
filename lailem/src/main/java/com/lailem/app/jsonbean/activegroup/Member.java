package com.lailem.app.jsonbean.activegroup;

import com.lailem.app.bean.Result;

public class Member extends Result {
	private String headSPicName;// 小头像文件名 string
	private String memberId;// 成员用户id string

	public String getHeadSPicName() {
		return headSPicName;
	}

	public void setHeadSPicName(String headSPicName) {
		this.headSPicName = headSPicName;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}