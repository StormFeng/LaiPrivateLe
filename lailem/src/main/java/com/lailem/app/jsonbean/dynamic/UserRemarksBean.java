package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 其它-其它-取备注名列表
 * 
 * @author MD-12
 *
 */
public class UserRemarksBean extends Result {

	private ArrayList<Remark> remarkList;// array<object>

	public static UserRemarksBean parse(String json) throws AppException {
		UserRemarksBean res = new UserRemarksBean();
		try {
			res = gson.fromJson(json, UserRemarksBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}
	
	

	public ArrayList<Remark> getRemarkList() {
		return remarkList;
	}



	public void setRemarkList(ArrayList<Remark> remarkList) {
		this.remarkList = remarkList;
	}



	public static class Remark extends Result {
		private String rem;// 备注名 string
		private String remUserId;// 被备注的用户id string

		public String getRem() {
			return rem;
		}

		public void setRem(String rem) {
			this.rem = rem;
		}

		public String getRemUserId() {
			return remUserId;
		}

		public void setRemUserId(String remUserId) {
			this.remUserId = remUserId;
		}

	}
	
	
}