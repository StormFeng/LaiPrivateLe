package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-称谓-取称谓模版分类列表
 * 
 * @author MD-12
 *
 */
public class CwTypeBean extends Result {

	private ArrayList<TypeList> typeList;// array<object>

	public ArrayList<TypeList> getTypeList() {
		return typeList;
	}

	public void setTypeList(ArrayList<TypeList> typeList) {
		this.typeList = typeList;
	}

	public static CwTypeBean parse(String json) throws AppException {
		CwTypeBean res = new CwTypeBean();
		try {
			res = gson.fromJson(json, CwTypeBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class TypeList extends Result {
		private String id;// string
		private String name;// 类型名称 string

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
