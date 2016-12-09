package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

public class BlacklistIdsBean extends Result {
	private ArrayList<String> blacklistIds;

	public static BlacklistIdsBean parse(String json) throws AppException {
		BlacklistIdsBean res = new BlacklistIdsBean();
		try {
			res = gson.fromJson(json, BlacklistIdsBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public ArrayList<String> getBlacklistIds() {
		return blacklistIds;
	}

	public void setBlacklistIds(ArrayList<String> blacklistIds) {
		this.blacklistIds = blacklistIds;
	}

}
