package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.DynamicBean.NoticeInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublisherInfo;
import com.lailem.app.jsonbean.personal.MyDynamicListBean;
import com.lailem.app.ui.me.MeDynamicActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class MeDynamicsDataSource extends BaseListDataSource<Object> {

    public MeDynamicsDataSource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        MyDynamicListBean list = (MyDynamicListBean) ApiClient.getApi().getMyDynamicList(ac.getLoginUid(), AppContext.PAGE_SIZE + "", page + "");

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        ArrayList<DynamicBean> dynamicList = list.getDynamicList();
        if (dynamicList != null && dynamicList.size() > 0) {
            // 动态消息
            if (dynamicList != null) {
                for (int i = 0; i < dynamicList.size(); i++) {
                    DynamicBean dynamic = dynamicList.get(i);

                    // 判断添加时间分割条目
                    DynamicBean beforeDynamic = null;
                    if (i > 0) {
                        beforeDynamic = dynamicList.get(i - 1);
                    }
                    String dateStr = dynamic.getCreateTime().substring(0, 10);
                    if (beforeDynamic == null) {
                        models.add(new ObjectWrapper(dateStr, BaseMultiTypeListAdapter.TPL_SECTION));
                    } else {
                        String beforeDateStr = beforeDynamic.getCreateTime().substring(0, 10);
                        if (!dateStr.equals(beforeDateStr)) {
                            models.add(new ObjectWrapper(dateStr, BaseMultiTypeListAdapter.TPL_SECTION));
                        }
                    }

                    String dynaType = dynamic.getDynaType();
                    if (Const.DYNA_TYPE_NOTICE.equals(dynaType)) {
                        // 通知
                        PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                        if (publisherInfo != null) {
                            publisherInfo.setCreateTime(dynamic.getCreateTime());
                            publisherInfo.setDynaType(dynamic.getDynaType());
                            publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                            publisherInfo.setItemViewType(MeDynamicActivity.TPL_AVATAR);
                            models.add(publisherInfo);
                        }
                        NoticeInfo noticeInfo = dynamic.getNoticeInfo();
                        noticeInfo.setItemViewType(MeDynamicActivity.TPL_NOTICE);
                        models.add(noticeInfo);

                        dynamic.setItemViewType(MeDynamicActivity.TPL_ACTIONBAR);
                        models.add(dynamic);
                    } else if (Const.DYNA_TYPE_CREATE_ACTIVE.equals(dynaType)) {
                        // 创建活动
                        PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                        if (publisherInfo != null) {
                            publisherInfo.setCreateTime(dynamic.getCreateTime());
                            publisherInfo.setDynaType(dynamic.getDynaType());
                            publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                            publisherInfo.setItemViewType(MeDynamicActivity.TPL_AVATAR);
                            models.add(publisherInfo);
                        }

                        models.add(new ObjectWrapper(dynamic, MeDynamicActivity.TPL_CREATE));

                        dynamic.setRemark(Func.formatNickName(_activity, dynamic.getCreateActivityInfo().getCreatorInfo().getId(), dynamic.getCreateActivityInfo().getCreatorInfo().getNickname()));
                        dynamic.setItemViewType(MeDynamicActivity.TPL_ACTIONBAR);
                        models.add(dynamic);
                    } else if (Const.DYNA_TYPE_PUBLISH.equals(dynaType)
                            || Const.DYNA_TYPE_SCHEDULE.equals(dynaType)
                            || Const.DYNA_TYPE_VOTE.equals(dynaType)) {
                        // 发表的动态
                        PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                        if (publisherInfo != null) {
                            publisherInfo.setCreateTime(dynamic.getCreateTime());
                            publisherInfo.setDynaType(dynamic.getDynaType());
                            publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                            publisherInfo.setItemViewType(MeDynamicActivity.TPL_AVATAR);
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
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_TEXT);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_NEW_VOICE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
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
                                            models.add(new ObjectWrapper(objArr, MeDynamicActivity.TPL_VIDEOIMAGE));
                                        } else {
                                            //单个图片
                                            publishInfo.setItemViewType(MeDynamicActivity.TPL_SINGLE_IMAGE);
                                            models.add(publishInfo);
                                        }
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                        if (hasVideo == true) {
                                            hasVideo = false;
                                            videoPublishInfo.setItemViewType(MeDynamicActivity.TPL_SINGLE_VIDEO);
                                            models.add(videoPublishInfo);
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
                                        }
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_NEW_ADDRESS);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_SCHEDULE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_VOTE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeDynamicActivity.TPL_GAP));
                                        }
                                    }
                                    if (j == publishList.size() - 1 && hasVideo == true) {
                                        hasVideo = false;
                                        videoPublishInfo.setItemViewType(MeDynamicActivity.TPL_SINGLE_VIDEO);
                                        models.add(videoPublishInfo);
                                    }
                                }
                            } else {
                                for (int j = 0; j < publishList.size(); j++) {
                                    PublishInfo publishInfo = publishList.get(j);
                                    String publishType = publishInfo.getPublishType();
                                    if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_TEXT);
                                    } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_IMAGE);
                                    } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_VOICE);
                                    } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_VIDEO);
                                    } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_ADDRESS);
                                    } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_SCHEDULE);
                                    } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeDynamicActivity.TPL_VOTE);
                                    }
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割线", MeDynamicActivity.TPL_LINE));
                                    }
                                }
                            }
                        }
                        dynamic.setItemViewType(MeDynamicActivity.TPL_ACTIONBAR);
                        models.add(dynamic);

                    }

                }
            }
        }
        hasMore = dynamicList != null && dynamicList.size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

}
