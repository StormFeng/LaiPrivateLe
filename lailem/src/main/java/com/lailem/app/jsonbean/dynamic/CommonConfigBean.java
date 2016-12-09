package com.lailem.app.jsonbean.dynamic;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 其它-系统配置-系统公共配置
 * 
 * @author MD-12
 *
 */
public class CommonConfigBean extends Result {

	private String activityPLimit;// 活动人数上限 string 正整数，0表示无上限
	private String groupPLimit;// 群人数上限 string 正整数，0表示无上限
	private String userJGLimit;// 用户参与群组数上限
	private String msgSwitch;// CIA短信验证开关 string 1开 2关
	private String vCode;// 当前版本

	private ArrayList<ActivityType> activityTypeList;// array<object>
	private ArrayList<GroupTag> groupTagList;// 群标签列表 array<object>
	private ArrayList<GroupType> groupTypeList;// 群类型列表 array<object>
	private ArrayList<RegionBean> regionList;// 地区列表 array<object>

	public String getMsgSwitch() {
		return msgSwitch;
	}

	public void setMsgSwitch(String msgSwitch) {
		this.msgSwitch = msgSwitch;
	}

	public String getUserJGLimit() {
		return userJGLimit;
	}

	public void setUserJGLimit(String userJGLimit) {
		this.userJGLimit = userJGLimit;
	}

	public String getActivityPLimit() {
		return activityPLimit;
	}

	public void setActivityPLimit(String activityPLimit) {
		this.activityPLimit = activityPLimit;
	}

	public String getGroupPLimit() {
		return groupPLimit;
	}

	public void setGroupPLimit(String groupPLimit) {
		this.groupPLimit = groupPLimit;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}

	public String getvCode() {
		return vCode;
	}

	public ArrayList<ActivityType> getActivityTypeList() {
		return activityTypeList;
	}

	public void setActivityTypeList(ArrayList<ActivityType> activityTypeList) {
		this.activityTypeList = activityTypeList;
	}

	public ArrayList<GroupTag> getGroupTagList() {
		return groupTagList;
	}

	public void setGroupTagList(ArrayList<GroupTag> groupTagList) {
		this.groupTagList = groupTagList;
	}

	public ArrayList<GroupType> getGroupTypeList() {
		return groupTypeList;
	}

	public void setGroupTypeList(ArrayList<GroupType> groupTypeList) {
		this.groupTypeList = groupTypeList;
	}

	public ArrayList<RegionBean> getRegionList() {
		return regionList;
	}

	public void setRegionList(ArrayList<RegionBean> regionList) {
		this.regionList = regionList;
	}

	public static CommonConfigBean parse(String json) throws AppException {
		CommonConfigBean res = new CommonConfigBean();
		try {
			res = gson.fromJson(json, CommonConfigBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class ActivityType extends Result {
		private String color;// 颜色值 string 格式：#FFFFFF
		private String iconName;// icon文件名 string
		private String id;// string
		private String name;// 类型名称 string

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getIconName() {
			return iconName;
		}

		public void setIconName(String iconName) {
			this.iconName = iconName;
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

        @Override
        public String toString() {
            return "ActivityType{" +
                    "color='" + color + '\'' +
                    ", iconName='" + iconName + '\'' +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

	public static class GroupTag extends Result {
		private String id;// 标签分类id string
		private String name;// 标签分类名称 string
		private ArrayList<Tag> tagList;// 标签列表 array<object>

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

		public void setTagList(ArrayList<Tag> tagList) {
			this.tagList = tagList;
		}

		public ArrayList<Tag> getTagList() {
			return tagList;
		}
	}

	public static class Tag extends Result {
		private String id;// 标签id string
		private String name;// 标签名称 string

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

		@Override
		public boolean equals(Object o) {
			if (TextUtils.isEmpty(id)) {
				return false;
			}
			return id.equals(((Tag) o).getId());
		}

	}

	public static class GroupType extends Result {
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

	public static class RegionBean extends Result {
		private String name;// 河北
		private String pId;// 1
		private String rId;// 10
		private String rType;//

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getpId() {
			return pId;
		}

		public void setpId(String pId) {
			this.pId = pId;
		}

		public String getrId() {
			return rId;
		}

		public void setrId(String rId) {
			this.rId = rId;
		}

		public String getrType() {
			return rType;
		}

		public void setrType(String rType) {
			this.rType = rType;
		}

	}
}
