package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 活动与群组-称谓-取group称谓接口
 * 
 * @author MD-12
 *
 */
public class CwInfoBean extends Result {

	private CwInfo cwInfo;// 称谓信息 object

	public CwInfo getCwInfo() {
		return cwInfo;
	}

	public void setCwInfo(CwInfo cwInfo) {
		this.cwInfo = cwInfo;
	}

	public static CwInfoBean parse(String json) throws AppException {
		CwInfoBean res = new CwInfoBean();
		try {
			res = gson.fromJson(json, CwInfoBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class CwInfo extends Result {
		private ArrayList<CwDetailInfo> cwDetailInfo;// 称谓详情信息 array<object>
		private String id;// string
		private String templateId;// 称谓模版id string
		private String topic;// 称谓主题 string

		public ArrayList<CwDetailInfo> getCwDetailInfo() {
			return cwDetailInfo;
		}

		public void setCwDetailInfo(ArrayList<CwDetailInfo> cwDetailInfo) {
			this.cwDetailInfo = cwDetailInfo;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTemplateId() {
			return templateId;
		}

		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}

		public String getTopic() {
			return topic;
		}

		public void setTopic(String topic) {
			this.topic = topic;
		}

	}

	public static class CwDetailInfo extends Result {
		private String id;// string
		private String name;// 称谓详情名称（称谓名称）string

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
