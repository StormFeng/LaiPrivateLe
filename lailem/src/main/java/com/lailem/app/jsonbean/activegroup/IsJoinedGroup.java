package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class IsJoinedGroup extends Result {

	private String isJoined;

	public String getIsJoined() {
		return isJoined;
	}
	
	public static IsJoinedGroup parse(String json) throws AppException {
		IsJoinedGroup res = null;
		try {
			res = gson.fromJson(json, IsJoinedGroup.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public void setIsJoined(String isJoined) {
		this.isJoined = isJoined;
	}
	
	public boolean isJoined(){
		return "1".equals(isJoined);
	}

}
