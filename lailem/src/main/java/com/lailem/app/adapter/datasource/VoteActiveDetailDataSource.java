package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.VoteActiveDetailBean;
import com.lailem.app.jsonbean.activegroup.VoteActiveDetailBean.ActiveInfo;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.dynamic.CommentListBean;
import com.lailem.app.ui.active.VoteActiveDetailActivity;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class VoteActiveDetailDataSource extends BaseListDataSource<Object> {
    private String activeId;
    private ActiveInfo activeInfo;

    public VoteActiveDetailDataSource(Context context, String activeId) {
        super(context);
        this.activeId = activeId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        if (page == FIRST_PAGE_NO) {
            VoteActiveDetailBean info = (VoteActiveDetailBean) ApiClient.getApi().getVoteActiveDetail(activeId, ac.getLoginUid());

            if (info.isNotOK()) {
                ac.handleErrorCode(_activity, info.errorCode, info.errorInfo);
                return models;
            }

            this.activeInfo = info.getActivityInfo();
            // 添加详情
            if (activeInfo != null) {
                activeInfo.setRemark(Func.formatNickName(_activity, activeInfo.getCreatorInfo().getId(), activeInfo.getCreatorInfo().getNickname()));
                activeInfo.setItemViewType(VoteActiveDetailActivity.TPL_DETAIL);
                models.add(activeInfo);
            }

            // 添加actionbar
            models.add(new ObjectWrapper(activeInfo, VoteActiveDetailActivity.TPL_ACTIONBAR));

            // 添加活动评论
            ArrayList<Comment> commentList = info.getCommentList();
            if (commentList != null && commentList.size() > 0) {
                addCommentList(commentList, models);
                hasMore = true;
            } else {
                // 加载评论为空
                models.add(new ObjectWrapper("没有评论", VoteActiveDetailActivity.TPL_COMMENT_EMPTY));
                hasMore = false;
            }
        } else {
            // 添加活动评论
            // 1(活动评论)、2（动态评论）
            CommentListBean list = (CommentListBean) ApiClient.getApi().commentList(activeId, "1", AppContext.PAGE_SIZE + "", page + "");

            if (list.isNotOK()) {
                ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
                return models;
            }

            ArrayList<Comment> commentList = list.getCommentList();
            if (commentList != null && commentList.size() > 0) {
                addCommentList(commentList, models);
            }
            hasMore = commentList != null && commentList.size() == AppContext.PAGE_SIZE;
        }
        this.page = page;
        return models;
    }

    private void addCommentList(ArrayList<Comment> commentList, ArrayList<Object> models) {
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            comment.setRemark(Func.formatNickName(_activity, comment.getUserId(), comment.getNickname()));
            Comment.ToCommentInfo toCommentInfo = comment.getToCommentInfo();
            if (toCommentInfo != null && !TextUtils.isEmpty(toCommentInfo.getNickname())) {
                toCommentInfo.setRemark(Func.formatNickName(_activity, toCommentInfo.getUserId(), toCommentInfo.getNickname()));
            }
            comment.setItemViewType(VoteActiveDetailActivity.TPL_COMMENT);
        }
        models.addAll(commentList);
    }

    public ActiveInfo getActiveInfo() {
        return activeInfo;
    }

}
