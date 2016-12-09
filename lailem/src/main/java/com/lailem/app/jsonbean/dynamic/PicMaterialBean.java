package com.lailem.app.jsonbean.dynamic;

import com.lailem.app.bean.Result;

public class PicMaterialBean extends Result {
	private String bPicName;// 大图文件名 string
	private String id;// id string
	private String sPicName;// 小图文件名 string
	private String squareSPicName;// 正方形小图文件名 string

	public String getbPicName() {
		return bPicName;
	}

	public void setbPicName(String bPicName) {
		this.bPicName = bPicName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getsPicName() {
		return sPicName;
	}

	public void setsPicName(String sPicName) {
		this.sPicName = sPicName;
	}

	public String getSquareSPicName() {
		return squareSPicName;
	}

	public void setSquareSPicName(String squareSPicName) {
		this.squareSPicName = squareSPicName;
	}

}