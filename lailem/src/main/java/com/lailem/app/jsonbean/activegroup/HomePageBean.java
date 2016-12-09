package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-主页一些信息（待定）
 * 
 * @author MD-12
 *
 */
public class HomePageBean extends Result {

	private ArrayList<Ads> adsList;// array<object>
	private ArrayList<HotActive> hotActivityList;// 热门的活动列表 array<object>
	private ArrayList<RecommendActive> recommendActivityList;// 推荐的活动列表
																// array<object>
	private ArrayList<RecommendGroup> recommendGroupList;// 推荐的群组列表
															// array<object>

	public ArrayList<Ads> getAdsList() {
		return adsList;
	}

	public void setAdsList(ArrayList<Ads> adsList) {
		this.adsList = adsList;
	}

	public ArrayList<HotActive> getHotActivityList() {
		return hotActivityList;
	}

	public void setHotActivityList(ArrayList<HotActive> hotActivityList) {
		this.hotActivityList = hotActivityList;
	}

	public ArrayList<RecommendActive> getRecommendActivityList() {
		return recommendActivityList;
	}

	public void setRecommendActivityList(ArrayList<RecommendActive> recommendActivityList) {
		this.recommendActivityList = recommendActivityList;
	}

	public ArrayList<RecommendGroup> getRecommendGroupList() {
		return recommendGroupList;
	}

	public void setRecommendGroupList(ArrayList<RecommendGroup> recommendGroupList) {
		this.recommendGroupList = recommendGroupList;
	}

	public static HomePageBean parse(String json) throws AppException {
		HomePageBean res = new HomePageBean();
		try {
			res = gson.fromJson(json, HomePageBean.class);
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

	public static class HotActive extends Result {
		private String id;// id string
		private String intro;// 简介 string
		private String name;// 名称 string
		private String squareSPicName;// 正方形小图文件名 string

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIntro() {
			return intro;
		}

		public void setIntro(String intro) {
			this.intro = intro;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSquareSPicName() {
			return squareSPicName;
		}

		public void setSquareSPicName(String squareSPicName) {
			this.squareSPicName = squareSPicName;
		}

	}

	public static class RecommendActive extends Result {
		private String id;// id string
		private String name;// 名称 string
		private String sPicName;// 小图文件名 string

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

	public static class RecommendGroup extends Result {
		private String id;// id string
		private String intro;// 简介 string
		private String name;// 名称 string
		private String squareSPicName;// 正方形小图文件名 string
		private String typeName;// 群类型名称 string

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIntro() {
			return intro;
		}

		public void setIntro(String intro) {
			this.intro = intro;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSquareSPicName() {
			return squareSPicName;
		}

		public void setSquareSPicName(String squareSPicName) {
			this.squareSPicName = squareSPicName;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

	}
}
