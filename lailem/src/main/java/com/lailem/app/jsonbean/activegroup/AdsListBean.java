package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-获取轮播图片广告
 * 
 * @author MD-12
 *
 */
public class AdsListBean extends Result {

	private ArrayList<Ads> adsList;// array<object>

	public static AdsListBean parse(String json) throws AppException {
		AdsListBean res = new AdsListBean();
		try {
			res = gson.fromJson(json, AdsListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Ads extends Result {
		private String adsType;// 广告类型 string
								// 广告类型：1（仅展示图片）、2（应用内打开的URL）、3（浏览器打开的URL）、4（应用内活动或群组）、5（应用内企业）
		private String content;// 广告内容 string url\群组id\企业id
		private String id;// id string
		private String picName;// 广告图片文件名 string

		public String getAdsType() {
			return adsType;
		}

		public void setAdsType(String adsType) {
			this.adsType = adsType;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPicName() {
			return picName;
		}

		public void setPicName(String picName) {
			this.picName = picName;
		}

	}

}
