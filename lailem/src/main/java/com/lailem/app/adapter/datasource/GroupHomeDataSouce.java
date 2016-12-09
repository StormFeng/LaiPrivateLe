package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.broadcast.BroadcastManager;
import com.lailem.app.broadcast.GroupOrActiveNoExistReceiver;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.DynamicBean.NoticeInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublishInfo;
import com.lailem.app.jsonbean.activegroup.DynamicBean.PublisherInfo;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean;
import com.lailem.app.jsonbean.activegroup.GroupInfoBean.GroupInfo;
import com.lailem.app.jsonbean.dynamic.GroupDynamicsBean;
import com.lailem.app.ui.group.GroupHomeActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.GroupUtils;
import com.lailem.app.widget.TipDialog;

import java.util.ArrayList;

public class GroupHomeDataSouce extends BaseListDataSource<Object> {

    private String groupId;
    private GroupInfo groupInfo;

    public GroupHomeDataSouce(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        if (page == FIRST_PAGE_NO) {
            GroupInfoBean info = (GroupInfoBean) ApiClient.getApi().group(groupId, ac.getLoginUid());

            if (info.isNotOK()) {

                if ("privateGroupNotMember".equals(info.errorCode)) {
                    //私有成员访问
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final TipDialog dialog = new TipDialog(_activity);
                            dialog.config("温馨提示", "您访问的是私有群组！", "知道了", new View.OnClickListener() {
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
                } else if ("groupNotExist".equals(info.errorCode)) {
                    //群组已经解散
                    //删除本地记录
                    GroupUtils.exitGroup(_activity, groupId);
                    BroadcastManager.sendGroupOrActiveNoExistBroadcast(_activity, GroupOrActiveNoExistReceiver.ACTION_GROUP_NO_EXIST, groupId);
                    _activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final TipDialog dialog = new TipDialog(_activity);
                            dialog.config("温馨提示", "您访问的群组已经解散！", "知道了", new View.OnClickListener() {
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

            this.groupInfo = info.getGroupInfo();
            // 添加详情
            if (groupInfo != null) {
                groupInfo.setRemark(Func.formatNickName(_activity, groupInfo.getCreatorInfo().getId(), groupInfo.getCreatorInfo().getNickname()));
                if (groupInfo.getRoleType().equals(Const.ROLE_TYPE_STRANGER)) {
                    models.add(new ObjectWrapper(groupInfo, GroupHomeActivity.TPL_DETAIL_FOR_PUBLIC));
                } else if (groupInfo.getRoleType().equals(Const.ROLE_TYPE_CREATOR)
                        || groupInfo.getRoleType().equals(Const.ROLE_TYPE_MANAGER)) {
                    models.add(new ObjectWrapper(groupInfo, GroupHomeActivity.TPL_DETAIL_FOR_CREATOR));
                } else {
                    models.add(new ObjectWrapper(groupInfo, GroupHomeActivity.TPL_DETAIL_FOR_MEMBER));
                }
            }

            //添加动态列表头
            models.add(new ObjectWrapper(Integer.parseInt(groupInfo.getDynamicCount()), GroupHomeActivity.TPL_DYNAMIS_HEADER));

            // 添加发表的动态
            ArrayList<DynamicBean> dynamicList = groupInfo.getDynamicList();
            if (dynamicList != null && dynamicList.size() > 0) {
                addDynamicList(dynamicList, models);
                hasMore = true;
            } else {
                hasMore = false;
            }
        } else {
            // 添加发表的动态
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
        }
        this.page = page;
        return models;
    }

    private void addDynamicList(ArrayList<DynamicBean> dynamicList, ArrayList<Object> models) {
        // 动态消息
        if (dynamicList != null) {
            for (int i = 0; i < dynamicList.size(); i++) {
                DynamicBean dynamic = dynamicList.get(i);
                String dynaType = dynamic.getDynaType();

                //自由排序
                if (Const.DYNA_TYPE_NOTICE.equals(dynaType)) {
                    // 通知
                    PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamic.getCreateTime());
                        publisherInfo.setDynaType(dynamic.getDynaType());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(GroupHomeActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    NoticeInfo noticeInfo = dynamic.getNoticeInfo();
                    noticeInfo.setItemViewType(GroupHomeActivity.TPL_NOTICE);
                    models.add(noticeInfo);

                    dynamic.setItemViewType(GroupHomeActivity.TPL_ACTIONBAR);
                    models.add(dynamic);

                } else if (Const.DYNA_TYPE_CREATE_ACTIVE.equals(dynaType)) {
                    // 创建活动
                    PublisherInfo publisherInfo = dynamic.getPublisherInfo();
                    if (publisherInfo != null) {
                        publisherInfo.setCreateTime(dynamic.getCreateTime());
                        publisherInfo.setDynaType(dynamic.getDynaType());
                        publisherInfo.setRemark(Func.formatNickName(_activity, publisherInfo.getId(), publisherInfo.getNickname()));
                        publisherInfo.setItemViewType(GroupHomeActivity.TPL_AVATAR);
                        models.add(publisherInfo);
                    }
                    dynamic.setRemark(Func.formatNickName(_activity, dynamic.getCreateActivityInfo().getCreatorInfo().getId(), dynamic.getCreateActivityInfo().getCreatorInfo().getNickname()));
                    models.add(new ObjectWrapper(dynamic, GroupHomeActivity.TPL_CREATE));
                    dynamic.setItemViewType(GroupHomeActivity.TPL_ACTIONBAR);
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
                        publisherInfo.setItemViewType(GroupHomeActivity.TPL_AVATAR);
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
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_TEXT);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_NEW_VOICE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
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
                                        models.add(new ObjectWrapper(objArr, GroupHomeActivity.TPL_VIDEOIMAGE));
                                    } else {
                                        //单个图片
                                        publishInfo.setItemViewType(GroupHomeActivity.TPL_SINGLE_IMAGE);
                                        models.add(publishInfo);
                                    }
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                    if (hasVideo == true) {
                                        hasVideo = false;
                                        videoPublishInfo.setItemViewType(GroupHomeActivity.TPL_SINGLE_VIDEO);
                                        models.add(videoPublishInfo);
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
                                    }
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_NEW_ADDRESS);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_SCHEDULE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
                                    }
                                } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_VOTE);
                                    models.add(publishInfo);
                                    if (j < publishList.size() - 1) {
                                        models.add(new ObjectWrapper("分割", GroupHomeActivity.TPL_GAP));
                                    }
                                }
                                if (j == publishList.size() - 1 && hasVideo == true) {
                                    hasVideo = false;
                                    videoPublishInfo.setItemViewType(GroupHomeActivity.TPL_SINGLE_VIDEO);
                                    models.add(videoPublishInfo);
                                }
                            }
                        } else {
                            for (int j = 0; j < publishList.size(); j++) {
                                PublishInfo publishInfo = publishList.get(j);
                                String publishType = publishInfo.getPublishType();
                                if (Const.PUBLISH_TYPE_TEXT.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_TEXT);
                                } else if (Const.PUBLISH_TYPE_IMAGE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_IMAGE);
                                } else if (Const.PUBLISH_TYPE_VOICE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_VOICE);
                                } else if (Const.PUBLISH_TYPE_VIDEO.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_VIDEO);
                                } else if (Const.PUBLISH_TYPE_ADDRESS.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_ADDRESS);
                                } else if (Const.PUBLISH_TYPE_SCHEDULE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_SCHEDULE);
                                } else if (Const.PUBLISH_TYPE_VOTE.equals(publishType)) {
                                    publishInfo.setItemViewType(GroupHomeActivity.TPL_VOTE);
                                }
                                models.add(publishInfo);
                                if (j < publishList.size() - 1) {
                                    models.add(new ObjectWrapper("分割线", GroupHomeActivity.TPL_LINE));
                                }
                            }
                        }
                    }
                    dynamic.setItemViewType(GroupHomeActivity.TPL_ACTIONBAR);
                    models.add(dynamic);
                }
            }
        }

    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

}
