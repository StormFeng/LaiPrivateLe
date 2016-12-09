package com.lailem.app.jsonbean.activegroup;

import com.lailem.app.bean.Result;

public class GroupNear extends Result {
	private String currCount;// 当前人数 string
	private String id;// id string
	private String intro;// 简介 string
	private String name;// 名称 string
	private String squareSPicName;// 正方形小图文件名 string
	private String typeName;// 群类型名称 string
	private String distance;
	private String timeStamp;

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistance() {
		return distance;
	}

	public String getCurrCount() {
		return currCount;
	}

	public void setCurrCount(String currCount) {
		this.currCount = currCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSquareSPicName() {
		return squareSPicName;
	}

	public void setSquareSPicName(String squareSPicName) {
		this.squareSPicName = squareSPicName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}