package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.DynamicBean;

import java.util.ArrayList;

/**
 * 个人中心-我的收藏列表
 * 
 * @author MD-12
 *
 */
public class CollectBean extends Result {

	private ArrayList<Collect> collectList;

	public static CollectBean parse(String json) throws AppException {
		CollectBean res = new CollectBean();
		try {
			res = gson.fromJson(json, CollectBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public void setCollectList(ArrayList<Collect> collectList) {
		this.collectList = collectList;
	}

	public ArrayList<Collect> getCollectList() {
		return collectList;
	}

	public static final class Collect extends Base {
		private ActiveInfo activityInfo;
		private String collectType;// 收藏类型 string 1（动态）、2（活动）
		private String createTime;// string
		private String id;

		private DynamicBean dynamic;// 动态列表 array<object>

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setActivityInfo(ActiveInfo activityInfo) {
			this.activityInfo = activityInfo;
		}

		public ActiveInfo getActivityInfo() {
			return activityInfo;
		}

		public void setDynamic(DynamicBean dynamic) {
			this.dynamic = dynamic;
		}

		public DynamicBean getDynamic() {
			return dynamic;
		}

		public String getCollectType() {
			return collectType;
		}

		public void setCollectType(String collectType) {
			this.collectType = collectType;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
	}

	public static final class ActiveInfo extends Base {
		private String address;// 地址 string
		private String bPicName;// 大图文件名 string
		private String id;// id string
		private String intro;// 简介 string
		private String name;// 名称 string
		private String typeId;// 活动类型Id string

		private String startTime;

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

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

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

	}
}
