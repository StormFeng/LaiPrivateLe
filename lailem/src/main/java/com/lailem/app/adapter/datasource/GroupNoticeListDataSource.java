package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.activegroup.GroupNoticeListBean;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class GroupNoticeListDataSource extends BaseListDataSource<Object> {

    private String groupId;

    public GroupNoticeListDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        GroupNoticeListBean list = (GroupNoticeListBean) ApiClient.getApi().getGroupNoticeList(groupId, AppContext.PAGE_SIZE + "", page + "", ac.getLoginUid());

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getNoticeList() != null && list.getNoticeList().size() > 0) {
            for (int i = 0; i < list.getNoticeList().size(); i++) {
                GroupNoticeListBean.GroupNotice groupNotice = list.getNoticeList().get(i);
                groupNotice.getPublisherInfo().setRemark(Func.formatNickName(_activity, groupNotice.getPublisherInfo().getId(), groupNotice.getPublisherInfo().getNickname()));
            }
            models.addAll(list.getNoticeList());
        }
        hasMore = list.getNoticeList() != null && list.getNoticeList().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }
}
