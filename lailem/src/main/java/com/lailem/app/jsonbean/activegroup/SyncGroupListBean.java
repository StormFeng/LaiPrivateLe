package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-我的活动与群组--我的群组列表
 * 
 * @author MD-12
 * 
 */
public class SyncGroupListBean extends Result {

	private ArrayList<Group> groupList;// 群组列表 array<object>

	public ArrayList<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<Group> groupList) {
		this.groupList = groupList;
	}

	public static SyncGroupListBean parse(String json) throws AppException {
		SyncGroupListBean res = new SyncGroupListBean();
		try {
			res = gson.fromJson(json, SyncGroupListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Group extends Result {
		private String id;// id string
		private String gType;// 类型 string
        private String perm;

        public void setPerm(String perm) {
            this.perm = perm;
        }

        public String getPerm() {
            return perm;
        }

        public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getgType() {
			return gType;
		}

		public void setgType(String gType) {
			this.gType = gType;
		}

	}

	@Override
	public String toString() {
		return "SyncGroupListBean [groupList=" + groupList + "]";
	}

}
