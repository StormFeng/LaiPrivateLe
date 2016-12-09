package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class ChangeHeadBean extends Result {
	private String headBPicName;// 大头像文件名 string
	private String headSPicName;// 小头像文件名 string

	public static ChangeHeadBean parse(String json) throws AppException {
		ChangeHeadBean res = new ChangeHeadBean();
		try {
			res = gson.fromJson(json, ChangeHeadBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public String getHeadBPicName() {
		return headBPicName;
	}

	public void setHeadBPicName(String headBPicName) {
		this.headBPicName = headBPicName;
	}

	public String getHeadSPicName() {
		return headSPicName;
	}

	public void setHeadSPicName(String headSPicName) {
		this.headSPicName = headSPicName;
	}

}
