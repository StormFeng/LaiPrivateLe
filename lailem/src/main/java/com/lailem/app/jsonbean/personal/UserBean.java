package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 个人中心-注册
 * 
 * @author MD-12
 *
 */
public class UserBean extends Result {

	private UserInfo userInfo;// 用户信息 object

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public static UserBean parse(String json) throws AppException {
		UserBean res = new UserBean();
		try {
			res = gson.fromJson(json, UserBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class UserInfo extends Result {
		private String age;// 年龄
		private String birthday;// 生日
		private String sex;// 性别
		private String area;// 所在区名称 string
		private String city;// 所在城市名称 string
		private String email;// 邮箱 string
		private String headBPicName;// 大头像文件名 string
		private String headSPicName;// 小头像文件名 string
		private String id;// 用户id string
		private String name;// 姓名 string
		private String nickname;// 用户昵称 string
		private String personalizedSignature;// 个性签名 string
		private String phone;// 手机号 string
		private String province;// 所在省名称 string
		private String registerType;// 注册类型 string 注册类型：1（手机号注册）、2（微信注册）
		private String token;// token值 string
								// token的生成规则见【公共信息】模块里的【安全信息】页面下【token生成规则】的接口描述
		private String userType;// 用户类型 string 用户类型：1（普通用户）、2（个人认证用户）、3（企业认证用户）
		private String username;// 用户名 string
								// 由字母、数字、下划线、短横线组成，6-32位，由用户设置，全局唯一，可用来登陆
		private String collectCount;
		private String dynamicCount;

        private String dataIntact;//1，完整 2，不完整

        public void setDataIntact(String dataIntact) {
            this.dataIntact = dataIntact;
        }

        public String getDataIntact() {
            return dataIntact;
        }

		public void setDynamicCount(String dynamicCount) {
			this.dynamicCount = dynamicCount;
		}

		public String getDynamicCount() {
			return dynamicCount;
		}

		public String getCollectCount() {
			return collectCount;
		}

		public void setCollectCount(String collectCount) {
			this.collectCount = collectCount;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getRegisterType() {
			return registerType;
		}

		public void setRegisterType(String registerType) {
			this.registerType = registerType;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

	}
}
