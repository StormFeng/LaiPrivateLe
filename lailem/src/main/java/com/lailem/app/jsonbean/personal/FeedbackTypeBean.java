package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

public class FeedbackTypeBean extends Result {
	private ArrayList<FeedbackType> feedbackTypeList;

	public ArrayList<FeedbackType> getFeedbackTypeList() {
		return feedbackTypeList;
	}

	public void setFeedbackTypeList(ArrayList<FeedbackType> feedbackTypeList) {
		this.feedbackTypeList = feedbackTypeList;
	}

	public static FeedbackTypeBean parse(String json) throws AppException {
		FeedbackTypeBean res = new FeedbackTypeBean();
		try {
			res = gson.fromJson(json, FeedbackTypeBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class FeedbackType extends Result {
		private String id;// string
		private String item;// 类目名称 string
		private String itemTip;// 反馈提示信息 string

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}

		public String getItemTip() {
			return itemTip;
		}

		public void setItemTip(String itemTip) {
			this.itemTip = itemTip;
		}

	}
}
