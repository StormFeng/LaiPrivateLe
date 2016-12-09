package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 其它-版本更新-检测新版本-安卓
 * 
 * @author MD-12
 *
 */
public class VersionCheckBean extends Result {

	private String apkFileName;// apk文件名 string
	private String isForce;// 是否强制更新 string 1（强制更新）、0（否）
	private String vCode;// 版本号 string app最新的版本号

	public String getApkFileName() {
		return apkFileName;
	}

	public void setApkFileName(String apkFileName) {
		this.apkFileName = apkFileName;
	}

	public String getIsForce() {
		return isForce;
	}

	public void setIsForce(String isForce) {
		this.isForce = isForce;
	}

	public String getvCode() {
		return vCode;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}

	public static VersionCheckBean parse(String json) throws AppException {
		VersionCheckBean res = new VersionCheckBean();
		try {
			res = gson.fromJson(json, VersionCheckBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}
}
