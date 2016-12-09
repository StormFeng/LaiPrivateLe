package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.dynamic.CommentListBean;
import com.lailem.app.jsonbean.dynamic.LikeListBean;
import com.lailem.app.ui.active.ActiveInfoDetailActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class ActiveInfoDetailDataSource extends BaseListDataSource<Object> {

    private String groupId;
    private ActiveInfo activeInfo;

    public ActiveInfoDetailDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        if (page == FIRST_PAGE_NO) {
            // 详情条目
            this.activeInfo = (ActiveInfo) ac.getShareObject(Const.SHAREOBJ_KEY_ACTIVE_INFO);
            activeInfo.setRemark(Func.formatNickName(_activity, activeInfo.getCreatorInfo().getId(), activeInfo.getCreatorInfo().getNickname()));
            activeInfo.setItemViewType(ActiveInfoDetailActivity.TPL_DETAIL);
            models.add(activeInfo);
            // 点赞条目
            LikeListBean likeList = (LikeListBean) ApiClient.getApi().likeList(activeInfo.getId(), "1", "10", "1");

            if (likeList.isNotOK()) {
                ac.handleErrorCode(_activity, likeList.errorCode, likeList.errorInfo);
                return models;
            }

            if (likeList.getLikeList() != null && likeList.getLikeList().size() > 0) {
                models.add(new ObjectWrapper(likeList.getLikeList(), ActiveInfoDetailActivity.TPL_ZANLIST));
            }
            //评论head
            models.add(new ObjectWrapper(activeInfo, ActiveInfoDetailActivity.TPL_COMMENT_HEADER));
        }
        CommentListBean commentList = (CommentListBean) ApiClient.getApi().commentList(activeInfo.getId(), "1", AppContext.PAGE_SIZE + "", page + "");

        if (commentList.isNotOK()) {
            ac.handleErrorCode(_activity, commentList.errorCode, commentList.errorInfo);
            return models;
        }

        if (commentList.getCommentList() != null && commentList.getCommentList().size() > 0) {
            for (int i = 0; i < commentList.getCommentList().size(); i++) {
                Comment comment = commentList.getCommentList().get(i);
                comment.setRemark(Func.formatNickName(_activity, comment.getUserId(), comment.getNickname()));
                Comment.ToCommentInfo toCommentInfo = comment.getToCommentInfo();
                if (toCommentInfo != null && !TextUtils.isEmpty(toCommentInfo.getNickname())) {
                    toCommentInfo.setRemark(Func.formatNickName(_activity, toCommentInfo.getUserId(), toCommentInfo.getNickname()));
                }
                comment.setItemViewType(ActiveInfoDetailActivity.TPL_COMMENT);
            }
            models.addAll(commentList.getCommentList());
        } else {
            if (page == FIRST_PAGE_NO) {
                models.add(new ObjectWrapper("没有评论", ActiveInfoDetailActivity.TPL_COMMENT_EMPTY));
            }
        }
        hasMore = commentList.getCommentList() != null && commentList.getCommentList().size() == AppContext.PAGE_SIZE;

        this.page = page;
        return models;
    }

    public ActiveInfo getActiveInfo() {
        return activeInfo;
    }

}
