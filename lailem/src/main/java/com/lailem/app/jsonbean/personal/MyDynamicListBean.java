package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.DynamicBean;

import java.util.ArrayList;

/**
 * 个人中心-我的动态列表
 * 
 * @author MD-12
 *
 */
public class MyDynamicListBean extends Result {

	private ArrayList<DynamicBean> dynamicList;// 动态列表 array<object>

	public ArrayList<DynamicBean> getDynamicList() {
		return dynamicList;
	}

	public void setDynamicList(ArrayList<DynamicBean> dynamicList) {
		this.dynamicList = dynamicList;
	}

	public static MyDynamicListBean parse(String json) throws AppException {
		MyDynamicListBean res = new MyDynamicListBean();
		try {
			res = gson.fromJson(json, MyDynamicListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}
}
