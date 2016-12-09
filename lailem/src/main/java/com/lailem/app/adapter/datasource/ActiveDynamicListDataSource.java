package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.dynamic.GroupDynamicsBean;
import com.lailem.app.ui.active.ActiveDynamicListActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

/**
 * Created by XuYang on 15/11/10.
 */
public class ActiveDynamicListDataSource extends BaseListDataSource<Object> {

    private final String groupId;

    public ActiveDynamicListDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        GroupDynamicsBean list = (GroupDynamicsBean) ApiClient.getApi().groupDynamic(groupId, null, AppContext.PAGE_SIZE + "", page + "", ac.getLoginUid());

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        ArrayList<DynamicBean> dynamicList = list.getDynamicList();
        if (dynamicList != null && dynamicList.size() > 0) {
            addDynamicList(dynamicList, models);
        }
        hasMore = dynamicList != null && dynamicList.size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

    private void addDynamicList(ArrayList<DynamicBean> dynamicList, ArrayList<Object> models) {
        // 动态消息
        if (dynamicList != null) {
            for (int i = 0; i < dynamicList.size(); i++) {
                DynamicBean dynamic = dynamicList.get(i);
                String dynaType = dynamic.getDynaType();
                if (Const.DYNA_TYPE_NOTICE.equals(dynaType)) {
                    // 通知
                    DynamicBean.PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamic.getCreateTime());
                        publisherInfo.setDynaType(dynamic.getDynaType());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(ActiveDynamicListActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    DynamicBean.NoticeInfo noticeInfo = dynamic.getNoticeInfo();
                    noticeInfo.setItemViewType(ActiveDynamicListActivity.TPL_NOTICE);
                    models.add(noticeInfo);

                    dynamic.setItemViewType(ActiveDynamicListActivity.TPL_ACTIONBAR);
                    models.add(dynamic);

                } else if (Const.DYNA_TYPE_PUBLISH.equals(dynaType)
                        || Const.DYNA_TYPE_SCHEDULE.equals(dynaType)
                        || Const.DYNA_TYPE_VOTE.equals(dynaType)) {
                    // 发表的动态
                    DynamicBean.PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamic.getCreateTime());
                        publisherInfo.setDynaType(dynamic.getDynaType());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(ActiveDynamicListActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    ArrayList<DynamicBean.PublishInfo> publishList = dynamic.getPublishList();
                    if (publishList != null) {
                        if (publishList != null) {
                            if (Const.DYNA_FROM_DISABLE_SORT.equals(dynamic.getDynaForm())) {
                                //固定排序
                                DynamicBean.PublishInfo videoPublishInfo = null;
                                boolean hasVideo = false;
                                for (int j = 0; j < publishList.size(); j++) {
                                    DynamicBean.PublishInfo publishInfo = publishList.get(j);
                                    String publishType = publishInfo.getPublishType();
                                    if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_TEXT);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_NEW_VOICE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
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
                                            models.add(new ObjectWrapper(objArr, ActiveDynamicListActivity.TPL_VIDEOIMAGE));
                                        } else {
                                            //单个图片
                                            publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_SINGLE_IMAGE);
                                            models.add(publishInfo);
                                        }
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                        if (hasVideo == true) {
                                            hasVideo = false;
                                            videoPublishInfo.setItemViewType(ActiveDynamicListActivity.TPL_SINGLE_VIDEO);
                                            models.add(videoPublishInfo);
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
                                        }
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_NEW_ADDRESS);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_SCHEDULE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_VOTE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", ActiveDynamicListActivity.TPL_GAP));
                                        }
                                    }
                                    if (j == publishList.size() - 1 && hasVideo == true) {
                                        hasVideo = false;
                                        videoPublishInfo.setItemViewType(ActiveDynamicListActivity.TPL_SINGLE_VIDEO);
                                        models.add(videoPublishInfo);
                                    }
                                }
                            } else {
                                for (int j = 0; j < publishList.size(); j++) {
                                    DynamicBean.PublishInfo publishInfo = publishList.get(j);
                                    String publishType = publishInfo.getPublishType();
                                    if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_TEXT);
                                    } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_IMAGE);
                                    } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_VOICE);
                                    } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_VIDEO);
                                    } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_ADDRESS);
                                    } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_SCHEDULE);
                                    } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                        publishInfo.setItemViewType(ActiveDynamicListActivity.TPL_VOTE);
                                    }
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割线", ActiveDynamicListActivity.TPL_LINE));
                                    }
                                }
                            }
                        }
                        dynamic.setItemViewType(ActiveDynamicListActivity.TPL_ACTIONBAR);
                        models.add(dynamic);
                    }

                }

            }
        }
    }
}
