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
import com.lailem.app.jsonbean.personal.CollectBean;
import com.lailem.app.jsonbean.personal.CollectBean.ActiveInfo;
import com.lailem.app.jsonbean.personal.CollectBean.Collect;
import com.lailem.app.ui.me.MeFavoriteListActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class MeFavoriteListDataSource extends BaseListDataSource<Object> {

    public static final String COLLECT_TYPE_DYNAMIC = "1";
    public static final String COLLECT_TYPE_ACTIVE = "2";

    public MeFavoriteListDataSource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        CollectBean list = (CollectBean) ApiClient.getApi().collect_List(ac.getLoginUid(), AppContext.PAGE_SIZE + "", page + "");

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        ArrayList<Collect> collectList = list.getCollectList();

        if (collectList != null & collectList.size() > 0) {
            // 动态和活动
            for (int i = 0; i < collectList.size(); i++) {
                Collect collect = collectList.get(i);

                // 判断添加时间分割条目
                Collect beforeCollect = null;
                if (i > 0) {
                    beforeCollect = collectList.get(i - 1);
                }
                String dateStr = collect.getCreateTime().substring(0, 10);
                if (beforeCollect == null) {
                    models.add(new ObjectWrapper(dateStr, BaseMultiTypeListAdapter.TPL_SECTION));
                } else {
                    String beforeDateStr = beforeCollect.getCreateTime().substring(0, 10);
                    if (!dateStr.equals(beforeDateStr)) {
                        models.add(new ObjectWrapper(dateStr, BaseMultiTypeListAdapter.TPL_SECTION));
                    }
                }

                if (COLLECT_TYPE_ACTIVE.equals(collect.getCollectType())) {
                    ActiveInfo activeInfo = collect.getActivityInfo();
                    // 活动收藏
                    if (activeInfo != null) {
                        // 添加活动收藏
                        activeInfo.setItemViewType(MeFavoriteListActivity.TPL_ACTIVEINFO);
                        models.add(activeInfo);
                    }
                } else if (COLLECT_TYPE_DYNAMIC.equals(collect.getCollectType())) {
                    // 动态收藏
                    DynamicBean dynamic = collect.getDynamic();
                    String dynaType = dynamic.getDynaType();
                    if (Const.DYNA_TYPE_NOTICE.equals(dynaType)) {
                        // 通知
                        PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                        if (publisherInfo != null) {
                            publisherInfo.setCreateTime(dynamic.getCreateTime());
                            publisherInfo.setDynaType(dynamic.getDynaType());
                            publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                            publisherInfo.setItemViewType(MeFavoriteListActivity.TPL_AVATAR);
                            models.add(publisherInfo);
                        }
                        NoticeInfo noticeInfo = dynamic.getNoticeInfo();
                        noticeInfo.setItemViewType(MeFavoriteListActivity.TPL_NOTICE);
                        models.add(noticeInfo);

                    } else if (Const.DYNA_TYPE_CREATE_ACTIVE.equals(dynaType)) {
                        // 创建活动
                        PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                        if (publisherInfo != null) {
                            publisherInfo.setCreateTime(dynamic.getCreateTime());
                            publisherInfo.setDynaType(dynamic.getDynaType());
                            publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                            publisherInfo.setItemViewType(MeFavoriteListActivity.TPL_AVATAR);
                            models.add(publisherInfo);
                        }

                        models.add(new ObjectWrapper(dynamic, MeFavoriteListActivity.TPL_CREATE));
                    } else if (Const.DYNA_TYPE_PUBLISH.equals(dynaType)
                            || Const.DYNA_TYPE_SCHEDULE.equals(dynaType)
                            || Const.DYNA_TYPE_VOTE.equals(dynaType)) {
                        // 发表的动态
                        PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                        if (publisherInfo != null) {
                            publisherInfo.setCreateTime(dynamic.getCreateTime());
                            publisherInfo.setDynaType(dynamic.getDynaType());
                            publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                            publisherInfo.setItemViewType(MeFavoriteListActivity.TPL_AVATAR);
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
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_TEXT);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_NEW_VOICE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
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
                                            models.add(new ObjectWrapper(objArr, MeFavoriteListActivity.TPL_VIDEOIMAGE));
                                        } else {
                                            //单个图片
                                            publishInfo.setItemViewType(MeFavoriteListActivity.TPL_SINGLE_IMAGE);
                                            models.add(publishInfo);
                                        }
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                        if (hasVideo == true) {
                                            hasVideo = false;
                                            videoPublishInfo.setItemViewType(MeFavoriteListActivity.TPL_SINGLE_VIDEO);
                                            models.add(videoPublishInfo);
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
                                        }
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_NEW_ADDRESS);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_SCHEDULE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
                                        }
                                    } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_VOTE);
                                        models.add(publishInfo);
                                        if (j < publishList.size() - 1) {
                                            models.add(new ObjectWrapper("分割", MeFavoriteListActivity.TPL_GAP));
                                        }
                                    }
                                    if (j == publishList.size() - 1 && hasVideo == true) {
                                        hasVideo = false;
                                        videoPublishInfo.setItemViewType(MeFavoriteListActivity.TPL_SINGLE_VIDEO);
                                        models.add(videoPublishInfo);
                                    }
                                }
                            } else {
                                for (int j = 0; j < publishList.size(); j++) {
                                    PublishInfo publishInfo = publishList.get(j);
                                    String publishType = publishInfo.getPublishType();
                                    if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_TEXT);
                                    } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_IMAGE);
                                    } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_VOICE);
                                    } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_VIDEO);
                                    } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_ADDRESS);
                                    } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_SCHEDULE);
                                    } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                        publishInfo.setItemViewType(MeFavoriteListActivity.TPL_VOTE);
                                    }
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割线", MeFavoriteListActivity.TPL_LINE));
                                    }
                                }
                            }
                        }
                    }
                }
                // 添加收藏菜单条目
                collect.setItemViewType(MeFavoriteListActivity.TPL_ACTIONBAR);
                models.add(collect);

            }
        }
        hasMore = collectList != null && collectList.size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }
}
