package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.activegroup.DynamicBean;

import java.util.ArrayList;

/**
 * 动态-发表-取动态详情
 * 
 * @author MD-12
 *
 */
public class DynamicDetailBean extends Result {

	private ArrayList<Comment> commentList;// 评论列表 array<object>
	private DynamicBean dynamicInfo;// 动态信息 object
	private ArrayList<Like> likeList;// 点赞列表 array<object>

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}

	public DynamicBean getDynamicInfo() {
		return dynamicInfo;
	}

	public void setDynamicInfo(DynamicBean dynamicInfo) {
		this.dynamicInfo = dynamicInfo;
	}

	public ArrayList<Like> getLikeList() {
		return likeList;
	}

	public void setLikeList(ArrayList<Like> likeList) {
		this.likeList = likeList;
	}

	public static DynamicDetailBean parse(String json) throws AppException {
		DynamicDetailBean res = new DynamicDetailBean();
		try {
			res = gson.fromJson(json, DynamicDetailBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Like extends Result {
		private String headSPicName;// 小头像文件名 string
		private String id;// string
		private String userId;// 用户id string

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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}
}
