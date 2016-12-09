package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.activegroup.GroupScheduleBean;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class GroupScheduleListDataSource extends BaseListDataSource<Object> {

    private String groupId;

    public GroupScheduleListDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        GroupScheduleBean list = (GroupScheduleBean) ApiClient.getApi().groupSchedule(ac.getLoginUid(), groupId, AppContext.PAGE_SIZE + "", page + "");

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getScheduleList() != null && list.getScheduleList().size() > 0) {
            models.addAll(list.getScheduleList());
            for (int i = 0; i < list.getScheduleList().size(); i++) {
                GroupScheduleBean.GroupSchedule groupSchedule = list.getScheduleList().get(i);
                groupSchedule.getCreatorInfo().setRemark(Func.formatNickName(_activity, groupSchedule.getCreatorInfo().getId(), groupSchedule.getCreatorInfo().getNickname()));
            }
        }

        hasMore = list.getScheduleList() != null && list.getScheduleList().size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }
}
