package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

/**
 * 动态-评论与赞-评论接口
 * 
 * @author MD-12
 *
 */
public class AddCommentBean extends Result {

	private String commentId;// 评论Id string

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public static AddCommentBean parse(String json) throws AppException {
		AddCommentBean res = new AddCommentBean();
		try {
			res = gson.fromJson(json, AddCommentBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}
}
