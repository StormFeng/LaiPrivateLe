package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 个人中心-取黑名单接口
 * 
 * @author MD-12
 *
 */
public class BlacklistBean extends Result {
	private ArrayList<BlackBean> blacklistList;// 黑名单列表 array<object>

	public ArrayList<BlackBean> getBlacklistList() {
		return blacklistList;
	}

	public void setBlacklistList(ArrayList<BlackBean> blacklistList) {
		this.blacklistList = blacklistList;
	}

	public static BlacklistBean parse(String json) throws AppException {
		BlacklistBean res = new BlacklistBean();
		try {
			res = gson.fromJson(json, BlacklistBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class BlackBean extends Result {
		private String blackUserId;// 黑名单用户id
		private String headSPicName;// 小头像文件名 string
		private String nickname;// 昵称 string
        private String remark;

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }

        public BlackBean(String nickname) {
			this.nickname = nickname;
		}

		public String getBlackUserId() {
			return blackUserId;
		}

		public void setBlackUserId(String blackUserId) {
			this.blackUserId = blackUserId;
		}

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
