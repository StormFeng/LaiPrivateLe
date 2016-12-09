package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 动态-评论与赞-取点赞列表
 * 
 * @author MD-12
 *
 */
public class LikeListBean extends Result {
	private ArrayList<Like> likeList;// 点赞列表 array<object>

	public ArrayList<Like> getLikeList() {
		return likeList;
	}

	public void setLikeList(ArrayList<Like> likeList) {
		this.likeList = likeList;
	}

	public static LikeListBean parse(String json) throws AppException {
		LikeListBean res = new LikeListBean();
		try {
			res = gson.fromJson(json, LikeListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Like extends Result {
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
