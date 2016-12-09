package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.DynamicBean;

import java.util.ArrayList;

/**
 * 动态-发表-取group动态列表
 * 
 * @author MD-12
 *
 */
public class GroupDynamicsBean extends Result {

	private ArrayList<DynamicBean> dynamicList;// 动态列表 array<object>

	public static GroupDynamicsBean parse(String json) throws AppException {
		GroupDynamicsBean res = new GroupDynamicsBean();
		try {
			res = gson.fromJson(json, GroupDynamicsBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public ArrayList<DynamicBean> getDynamicList() {
		return dynamicList;
	}

	public void setDynamicList(ArrayList<DynamicBean> dynamicList) {
		this.dynamicList = dynamicList;
	}

}
