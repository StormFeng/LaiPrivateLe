package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 其它-系统配置-系统公共配置版本
 * 
 * @author MD-12
 *
 */
public class VCodeBean extends Result {

	private String vCode;// 版本号 string 数字，从1开始

	public String getvCode() {
		return vCode;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}

	public static VCodeBean parse(String json) throws AppException {
		VCodeBean res = new VCodeBean();
		try {
			res = gson.fromJson(json, VCodeBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}
}
