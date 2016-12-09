package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class GroupApplyBean extends Result {
	private String applyStatus;// 申请状态

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public static GroupApplyBean parse(String json) throws AppException {
		GroupApplyBean res = new GroupApplyBean();
		try {
			res = gson.fromJson(json, GroupApplyBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

}
