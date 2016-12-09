package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-称谓- 取称谓模版详情
 * 
 * @author MD-12
 *
 */
public class CwDetailBean extends Result {

	private ArrayList<CwDetailList> cwDetailList;// 称谓详情列表 array<object>

	public ArrayList<CwDetailList> getCwDetailList() {
		return cwDetailList;
	}

	public void setCwDetailList(ArrayList<CwDetailList> cwDetailList) {
		this.cwDetailList = cwDetailList;
	}

	public static CwDetailBean parse(String json) throws AppException {
		CwDetailBean res = new CwDetailBean();
		try {
			res = gson.fromJson(json, CwDetailBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class CwDetailList extends Result {
		private String id;// string
		private String name;// 称谓名称 string

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
