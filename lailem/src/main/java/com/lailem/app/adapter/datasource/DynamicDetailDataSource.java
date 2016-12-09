package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.DynamicBean.NoticeInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublisherInfo;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.dynamic.CommentListBean;
import com.lailem.app.jsonbean.dynamic.DynamicDetailBean;
import com.lailem.app.jsonbean.dynamic.DynamicDetailBean.Like;
import com.lailem.app.ui.dynamic.DynamicDetailActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class DynamicDetailDataSource extends BaseListDataSource<Object> {

    private String dynamicId;

    private DynamicBean dynamicInfo;

    public DynamicDetailDataSource(Context context, String dynamicId) {
        super(context);
        this.dynamicId = dynamicId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        if (page == FIRST_PAGE_NO) {
            DynamicDetailBean list = (DynamicDetailBean) ApiClient.getApi().onDynamic(dynamicId, ac.getLoginUid());

            if (list.isNotOK()) {
                ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
                return models;
            }

            ArrayList<Comment> commentList = list.getCommentList();
            this.dynamicInfo = list.getDynamicInfo();
            ArrayList<Like> likeList = list.getLikeList();
            if (dynamicInfo != null) {
                String dynaType = dynamicInfo.getDynaType();
                if (Const.DYNA_TYPE_NOTICE.equals(dynaType)) {
                    // 通知
                    PublisherInfo publisherInfo = dynamicInfo.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamicInfo.getCreateTime());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(DynamicDetailActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    NoticeInfo noticeInfo = dynamicInfo.getNoticeInfo();
                    noticeInfo.setItemViewType(DynamicDetailActivity.TPL_NOTICE);
                    models.add(noticeInfo);

                    dynamicInfo.setItemViewType(DynamicDetailActivity.TPL_ACTIONBAR);
                    models.add(dynamicInfo);
                } else if (Const.DYNA_TYPE_PUBLISH.equals(dynaType)
                        || Const.DYNA_TYPE_SCHEDULE.equals(dynaType)
                        || Const.DYNA_TYPE_VOTE.equals(dynaType)) {
                    // 发表的动态
                    PublisherInfo publisherInfo = dynamicInfo.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamicInfo.getCreateTime());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(DynamicDetailActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    ArrayList<PublishInfo> publishList = dynamicInfo.getPublishList();
                    if (publishList != null) {
                        if (Const.DYNA_FROM_DISABLE_SORT.equals(dynamicInfo.getDynaForm())) {
                            //固定排序
                            PublishInfo videoPublishInfo = null;
                            boolean hasVideo = false;
                            for (int j = 0; j < publishList.size(); j++) {
                                PublishInfo publishInfo = publishList.get(j);
                                String publishType = publishInfo.getPublishType();
                                if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_TEXT);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_NEW_VOICE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                    hasVideo = true;
                                    videoPublishInfo = publishInfo;
                                } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                    if (hasVideo || (publishInfo.getPics() != null && publishInfo.getPics().size() > 1)) {
                                        if (hasVideo) {
                                            hasVideo = false;
                                        }
                                        //九宫格
                                        Object[] objArr = new Object[]{videoPublishInfo, publishInfo};
                                        models.add(new ObjectWrapper(objArr, DynamicDetailActivity.TPL_VIDEOIMAGE));
                                    } else {
                                        //单个图片
                                        publishInfo.setItemViewType(DynamicDetailActivity.TPL_SINGLE_IMAGE);
                                        models.add(publishInfo);
                                    }
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                    if (hasVideo == true) {
                                        hasVideo = false;
                                        videoPublishInfo.setItemViewType(DynamicDetailActivity.TPL_SINGLE_VIDEO);
                                        models.add(videoPublishInfo);
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_NEW_ADDRESS);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_SCHEDULE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_VOTE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", DynamicDetailActivity.TPL_GAP));
                                    }
                                }
                                if (j == publishList.size() - 1 && hasVideo == true) {
                                    hasVideo = false;
                                    videoPublishInfo.setItemViewType(DynamicDetailActivity.TPL_SINGLE_VIDEO);
                                    models.add(videoPublishInfo);
                                }
                            }
                        } else {
                            for (int j = 0; j < publishList.size(); j++) {
                                PublishInfo publishInfo = publishList.get(j);
                                String publishType = publishInfo.getPublishType();
                                if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_TEXT);
                                } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_IMAGE);
                                } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_VOICE);
                                } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_VIDEO);
                                } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_ADDRESS);
                                } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_SCHEDULE);
                                } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                    publishInfo.setItemViewType(DynamicDetailActivity.TPL_VOTE);
                                }
                                models.add(publishInfo);
                                if (j < publishList.size() - 1) {
                                    models.add(new ObjectWrapper("分割线", DynamicDetailActivity.TPL_LINE));
                                }
                            }
                        }
                    }
                    dynamicInfo.setItemViewType(DynamicDetailActivity.TPL_ACTIONBAR);
                    models.add(dynamicInfo);
                }
            }
            if (likeList != null && likeList.size() > 0) {
                models.add(new ObjectWrapper(likeList, DynamicDetailActivity.TPL_ZANLIST));
            }
            if (commentList != null && commentList.size() > 0) {
                addCommentList(commentList, models);
                hasMore = true;
            } else {
                models.add(new ObjectWrapper("没有评论", DynamicDetailActivity.TPL_COMMENT_EMPTY));
                hasMore = false;
            }
        } else

        {
            // 1(活动评论)、2（动态评论）
            CommentListBean list = (CommentListBean) ApiClient.getApi().commentList(dynamicId, "2", AppContext.PAGE_SIZE + "", page + "");

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
            comment.setItemViewType(DynamicDetailActivity.TPL_COMMENT);
        }
        models.addAll(commentList);
    }

    public DynamicBean getDynamicInfo() {
        return dynamicInfo;
    }

}
