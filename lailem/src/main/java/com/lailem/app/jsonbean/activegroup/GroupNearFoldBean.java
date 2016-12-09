package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-附近群组列表-按地址折叠
 * 
 * @author MD-12
 *
 */
public class GroupNearFoldBean extends Result {

	private ArrayList<Address> addressList;// 地址列表 array<object>

	public ArrayList<Address> getAddressList() {
		return addressList;
	}

	public void setAddressList(ArrayList<Address> addressList) {
		this.addressList = addressList;
	}

	public static GroupNearFoldBean parse(String json) throws AppException {
		GroupNearFoldBean res = new GroupNearFoldBean();
		try {
			res = gson.fromJson(json, GroupNearFoldBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Address extends Result {
		private String address;// 地址 string
		private String distance;// 距离 string
								// 距离，单位需由服务器计算好，前端只作显示，距离单位规则见【公共信息】模块下的距离单位规则
		private String groupCount;// 群数量 string
		private GroupNear groupList;// 群列表 array<object>
		private String id;// string

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getGroupCount() {
			return groupCount;
		}

		public void setGroupCount(String groupCount) {
			this.groupCount = groupCount;
		}

		public GroupNear getGroupList() {
			return groupList;
		}

		public void setGroupList(GroupNear groupList) {
			this.groupList = groupList;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

}
