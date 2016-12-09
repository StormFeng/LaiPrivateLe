package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 其他-图片素材-获取图片素材类型
 * 
 * @author MD-12
 *
 */
public class PicMaterialTypeBean extends Result {
	private ArrayList<PicMaterialType> picMaterialSpecialList;// 图片素材专题列表
																// array<object>
	private ArrayList<PicMaterialType> picMaterialTypeList;// 图片素材类型列表
															// array<object>

	public ArrayList<PicMaterialType> getPicMaterialSpecialList() {
		return picMaterialSpecialList;
	}

	public void setPicMaterialSpecialList(ArrayList<PicMaterialType> picMaterialSpecialList) {
		this.picMaterialSpecialList = picMaterialSpecialList;
	}

	public ArrayList<PicMaterialType> getPicMaterialTypeList() {
		return picMaterialTypeList;
	}

	public void setPicMaterialTypeList(ArrayList<PicMaterialType> picMaterialTypeList) {
		this.picMaterialTypeList = picMaterialTypeList;
	}

	public static PicMaterialTypeBean parse(String json) throws AppException {
		PicMaterialTypeBean res = new PicMaterialTypeBean();
		try {
			res = gson.fromJson(json, PicMaterialTypeBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class PicMaterialType extends Result {
		private String bPicName;// 大图文件名 string
		private String description;// 描述 string
		private String id;// id string
		private String name;// 名称 string
		private String sPicName;// 小图文件名 string

		public String getbPicName() {
			return bPicName;
		}

		public void setbPicName(String bPicName) {
			this.bPicName = bPicName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

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

		public String getsPicName() {
			return sPicName;
		}

		public void setsPicName(String sPicName) {
			this.sPicName = sPicName;
		}

	}
}
