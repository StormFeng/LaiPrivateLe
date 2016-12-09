package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

public class MemberInfoBean extends Result {

	public UserInfo userInfo;

	public static MemberInfoBean parse(String json) throws AppException {
		MemberInfoBean res = new MemberInfoBean();
		try {
			res = gson.fromJson(json, MemberInfoBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public static class UserInfo extends Result {
		private String age;// 年龄 string 年单位
		private String area;// 所在区名称 string
		private String birthday;// 生日 string 1989-08-16
		private String city;// 所在城市名称 string
		private String headBPicName;// 大头像文件名 string
		private String headSPicName;// 小头像文件名 string
		private String id;// 用户id string
		private String isBlacklist;// 是否被当前用户设置了黑名单 string 1 是 2 否
		private String name;// 姓名 string
		private String nickname;// 用户昵称 string
		private String personalizedSignature;// 个性签名 string
		private String province;// 所在省名称 string
		private String sex;// 性别 string 1（男）、2（女）
		private String username;// 用户名 string
								// 由字母、数字、下划线、短横线组成，6-32位，由用户设置，全局唯一，可用来登陆，一经设置不得修改

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getHeadBPicName() {
			return headBPicName;
		}

		public void setHeadBPicName(String headBPicName) {
			this.headBPicName = headBPicName;
		}

		public String getHeadSPicName() {
			return headSPicName;
		}

		public void setHeadSPicName(String headSPicName) {
			this.headSPicName = headSPicName;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getIsBlacklist() {
			return isBlacklist;
		}

		public void setIsBlacklist(String isBlacklist) {
			this.isBlacklist = isBlacklist;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getPersonalizedSignature() {
			return personalizedSignature;
		}

		public void setPersonalizedSignature(String personalizedSignature) {
			this.personalizedSignature = personalizedSignature;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

	}

}
