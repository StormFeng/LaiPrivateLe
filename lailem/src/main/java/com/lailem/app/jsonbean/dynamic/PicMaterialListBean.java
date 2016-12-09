package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 其他-图片素材-取某一分类下的图片列表
 * 
 * @author MD-12
 *
 */
public class PicMaterialListBean extends Result {

	private ArrayList<PicMaterialBean> picMaterialList;// 图片素材列表 array<object>

	public static PicMaterialListBean parse(String json) throws AppException {
		PicMaterialListBean res = new PicMaterialListBean();
		try {
			res = gson.fromJson(json, PicMaterialListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}
	
	public ArrayList<PicMaterialBean> getPicMaterialList() {
		return picMaterialList;
	}
	
	public void setPicMaterialList(ArrayList<PicMaterialBean> picMaterialList) {
		this.picMaterialList = picMaterialList;
	}
}
