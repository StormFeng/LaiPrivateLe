package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupOrActiveNoExistReceiver;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean;
import com.lailem.app.jsonbean.activegroup.ActiveInfoBean.ActiveInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.DynamicBean.NoticeInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublisherInfo;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.dynamic.CommentListBean;
import com.lailem.app.jsonbean.dynamic.GroupDynamicsBean;
import com.lailem.app.jsonbean.dynamic.LikeListBean;
import com.lailem.app.ui.active.ActiveDetailActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.widget.TipDialog;

import java.util.ArrayList;

public class ActiveDetailDataSource extends BaseListDataSource<Object> {

    private String activeId;
    private ActiveInfo activeInfo;

    public ActiveDetailDataSource(Context context, String activeId) {
        super(context);
        this.activeId = activeId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        if (page == FIRST_PAGE_NO) {
            ActiveInfoBean info = (ActiveInfoBean) ApiClient.getApi().activity(activeId, ac.getLoginUid());

            if (info.isNotOK()) {
                if ("privateActivityNotMember".equals(info.errorCode)) {
                    //私有成员访问
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final TipDialog dialog = new TipDialog(_activity);
                            dialog.config("温馨提示", "您访问的是私有活动！", "知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    _activity.finish();
                                }
                            });
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    _activity.finish();
                                }
                            });
                            dialog.show();
                        }
                    });
                } else if ("actiNotExist".equals(info.errorCode)) {
                    //活动已经删除
                    //删除本地记录
                    GroupUtils.exitActivity(_activity, activeId);
                    BroadcastManager.sendGroupOrActiveNoExistBroadcast(_activity, GroupOrActiveNoExistReceiver.ACTION_ACTIVE_NO_EXIST, activeId);
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final TipDialog dialog = new TipDialog(_activity);
                            dialog.config("温馨提示", "您访问的活动已经删除！", "知道了", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    _activity.finish();
                                }
                            });
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    _activity.finish();
                                }
                            });
                            dialog.show();
                        }
                    });
                } else {
                    ac.handleErrorCode(_activity, info.errorCode, info.errorInfo);
                }
                return models;
            }

            this.activeInfo = info.getActivityInfo();
            // 添加详情
            if (activeInfo != null) {
                activeInfo.setRemark(Func.formatNickName(_activity, activeInfo.getCreatorInfo().getId(), activeInfo.getCreatorInfo().getNickname()));
                if (activeInfo.getRoleType().equals(Const.ROLE_TYPE_STRANGER)) {
                    models.add(new ObjectWrapper(activeInfo, ActiveDetailActivity.TPL_DETAIL_FOR_PUBLIC));
                } else if (activeInfo.getRoleType().equals(Const.ROLE_TYPE_CREATOR)
                        || activeInfo.getRoleType().equals(Const.ROLE_TYPE_MANAGER)) {
                    models.add(new ObjectWrapper(activeInfo, ActiveDetailActivity.TPL_DETAIL_FOR_CREATOR));
                } else {
                    models.add(new ObjectWrapper(activeInfo, ActiveDetailActivity.TPL_DETAIL_FOR_MEMBER));
                }
            }

            //添加赞列表
            if (activeInfo.getRoleType().equals(Const.ROLE_TYPE_STRANGER)) {
                LikeListBean likeList = (LikeListBean) ApiClient.getApi().likeList(activeInfo.getId(), "1", "10", "1");
                if (likeList.isNotOK()) {
                    ac.handleErrorCode(_activity, likeList.errorCode, likeList.errorInfo);
                    models.clear();
                    return models;
                }

                if (likeList.getLikeList() != null && likeList.getLikeList().size() > 0) {
                    models.add(new ObjectWrapper(likeList.getLikeList(), ActiveDetailActivity.TPL_ZAN_LIST));
                }
            }

            if (activeInfo.getRoleType().equals(Const.ROLE_TYPE_STRANGER)) {
                //添加评论列表头
                models.add(new ObjectWrapper(activeInfo, ActiveDetailActivity.TPL_COMMENT_HEADER));
                // 添加活动评论
                ArrayList<Comment> commentList = info.getCommentList();
                if (commentList != null && commentList.size() > 0) {
                    addCommentList(commentList, models);
                    hasMore = true;
                } else {
                    hasMore = false;
                }
            } else {
                //添加动态列表头
                models.add(new ObjectWrapper(Integer.parseInt(activeInfo.getDynamicCount()), ActiveDetailActivity.TPL_DYNAMIS_HEADER));
                // 添加发表的动态
                ArrayList<DynamicBean> dynamicList = info.getDynamicList();
                if (dynamicList != null && dynamicList.size() > 0) {
                    addDynamicList(dynamicList, models);
                    hasMore = true;
                } else {
                    hasMore = false;
                }
            }
        } else {
            if (activeInfo.getRoleType().equals(Const.ROLE_TYPE_STRANGER)) {
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
                hasMore = commentList.size() == AppContext.PAGE_SIZE;
            } else {
                // 添加发表的动态
                GroupDynamicsBean list = (GroupDynamicsBean) ApiClient.getApi().groupDynamic(activeId, null, AppContext.PAGE_SIZE + "", page + "", ac.getLoginUid());

                if (list.isNotOK()) {
                    ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
                    return models;
                }

                ArrayList<DynamicBean> dynamicList = list.getDynamicList();
                if (dynamicList != null && dynamicList.size() > 0) {
                    addDynamicList(dynamicList, models);
                }
                hasMore = dynamicList != null && dynamicList.size() == AppContext.PAGE_SIZE;
            }
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
            comment.setItemViewType(ActiveDetailActivity.TPL_COMMENT);
        }
        models.addAll(commentList);
    }

    private void addDynamicList(ArrayList<DynamicBean> dynamicList, ArrayList<Object> models) {
        // 动态消息
        if (dynamicList != null) {
            for (int i = 0; i < dynamicList.size(); i++) {
                DynamicBean dynamic = dynamicList.get(i);
                String dynaType = dynamic.getDynaType();
                if (Const.DYNA_TYPE_NOTICE.equals(dynaType)) {
                    // 通知
                    PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamic.getCreateTime());
                        publisherInfo.setDynaType(dynamic.getDynaType());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(ActiveDetailActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    NoticeInfo noticeInfo = dynamic.getNoticeInfo();
                    noticeInfo.setItemViewType(ActiveDetailActivity.TPL_NOTICE);
                    models.add(noticeInfo);
                } else if (Const.DYNA_TYPE_PUBLISH.equals(dynaType)
                        || Const.DYNA_TYPE_SCHEDULE.equals(dynaType)
                        || Const.DYNA_TYPE_VOTE.equals(dynaType)) {
                    // 发表的动态
                    PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamic.getCreateTime());
                        publisherInfo.setDynaType(dynamic.getDynaType());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(ActiveDetailActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    ArrayList<PublishInfo> publishList = dynamic.getPublishList();
                    if (publishList != null) {
                        if (Const.DYNA_FROM_DISABLE_SORT.equals(dynamic.getDynaForm())) {
                            //固定排序
                            PublishInfo videoPublishInfo = null;
                            boolean hasVideo = false;
                            for (int j = 0; j < publishList.size(); j++) {
                                PublishInfo publishInfo = publishList.get(j);
                                String publishType = publishInfo.getPublishType();
                                if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_TEXT);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_NEW_VOICE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
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
                                        models.add(new ObjectWrapper(objArr, ActiveDetailActivity.TPL_VIDEOIMAGE));
                                    } else {
                                        //单个图片
                                        publishInfo.setItemViewType(ActiveDetailActivity.TPL_SINGLE_IMAGE);
                                        models.add(publishInfo);
                                    }
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                    if (hasVideo == true) {
                                        hasVideo = false;
                                        videoPublishInfo.setItemViewType(ActiveDetailActivity.TPL_SINGLE_VIDEO);
                                        models.add(videoPublishInfo);
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
                                    }
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_NEW_ADDRESS);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_SCHEDULE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_VOTE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", ActiveDetailActivity.TPL_GAP));
                                    }
                                }
                                if (j == publishList.size() - 1 && hasVideo == true) {
                                    hasVideo = false;
                                    videoPublishInfo.setItemViewType(ActiveDetailActivity.TPL_SINGLE_VIDEO);
                                    models.add(videoPublishInfo);
                                }
                            }
                        } else {
                            for (int j = 0; j < publishList.size(); j++) {
                                PublishInfo publishInfo = publishList.get(j);
                                String publishType = publishInfo.getPublishType();
                                if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_TEXT);
                                } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_IMAGE);
                                } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_VOICE);
                                } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_VIDEO);
                                } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_ADDRESS);
                                } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_SCHEDULE);
                                } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                    publishInfo.setItemViewType(ActiveDetailActivity.TPL_VOTE);
                                }
                                models.add(publishInfo);
                                if (j < publishList.size() - 1) {
                                    models.add(new ObjectWrapper("分割线", ActiveDetailActivity.TPL_LINE));
                                }
                            }
                        }
                    }
                }
                dynamic.setItemViewType(ActiveDetailActivity.TPL_ACTIONBAR);
                models.add(dynamic);

            }
        }
    }

    public ActiveInfo getActiveInfo() {
        return activeInfo;
    }

}