package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-称谓-取称谓模版列表
 * 
 * @author MD-12
 *
 */
public class CwListBean extends Result {

	private ArrayList<CwList> cwList;// 称谓列表 array<object>

	public static CwListBean parse(String json) throws AppException {
		CwListBean res = new CwListBean();
		try {
			res = gson.fromJson(json, CwListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class CwList extends Result {
		private String id;// string
		private String topic;// 称谓主题 string

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

	}
}
