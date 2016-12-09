package com.lailem.app.jsonbean.dynamic;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

/**
 * 动态-评论与赞-取评论列表
 * 
 * @author MD-12
 *
 */
public class CommentListBean extends Result {

	private ArrayList<Comment> commentList;// 评论列表 array<object>

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}

	public static CommentListBean parse(String json) throws AppException {
		CommentListBean res = new CommentListBean();
		try {
			res = gson.fromJson(json, CommentListBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

}
