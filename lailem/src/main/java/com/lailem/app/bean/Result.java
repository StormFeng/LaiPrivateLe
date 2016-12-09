package com.lailem.app.bean;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;

public class Result extends Base {
	public String ret = "";
	public String errorCode = "";
	public String errorInfo = "";

	public static Gson gson = new Gson();

	public Result() {

	}

	public Result(int itemViewType) {
		this.itemViewType = itemViewType;
	}

	public boolean isOK() {
		return "success".equals(ret);
	}
	
	public boolean isNotOK(){
		return !isOK();
	}

	public static Result parse(String json) throws AppException {
		Result res = new Result();
		try {
			res = gson.fromJson(json, Result.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

}
