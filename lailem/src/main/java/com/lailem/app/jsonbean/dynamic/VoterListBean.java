package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 动态-投票-投票人列表接口
 * 
 * @author MD-12
 *
 */
public class VoterListBean extends Result {

	private ArrayList<Voter> voterList;// 投票人列表 array<object>

	public ArrayList<Voter> getVoterList() {
		return voterList;
	}

	public void setVoterList(ArrayList<Voter> voterList) {
		this.voterList = voterList;
	}

	public static VoterListBean parse(String json) throws AppException {
		VoterListBean res = new VoterListBean();
		try {
			res = gson.fromJson(json, VoterListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Voter extends Result {
		private String headSPicName;// 小头像文件名 string
		private String id;// string
		private String nickname;// 昵称 string
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

	}
}
