package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 其它-其它-根据用户id取用户头像与昵称
 * 
 * @author MD-12
 *
 */
public class GetUserBriefBean extends Result {

	private UserInfo userInfo;// 用户信息 object

	public static GetUserBriefBean parse(String json) throws AppException {
		GetUserBriefBean res = new GetUserBriefBean();
		try {
			res = gson.fromJson(json, GetUserBriefBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class UserInfo extends Result {
		private String headSPicName;// 小头像文件名 string
		private String nickname;// 用户昵称 string

		public String getHeadSPicName() {
			return headSPicName;
		}

		public void setHeadSPicName(String headSPicName) {
			this.headSPicName = headSPicName;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

	}
}
