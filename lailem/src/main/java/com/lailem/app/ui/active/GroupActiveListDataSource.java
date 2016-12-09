package com.lailem.app.ui.active;

import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.activegroup.GroupActiveListBean;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupActiveListDataSource extends BaseListDataSource<Object> {

    private HashMap<String, String> typeColorMap;

    private String groupId;

    public GroupActiveListDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;

        //初始化颜色
        typeColorMap = new HashMap<String, String>();
        ArrayList<CommonConfigBean.ActivityType> list = (ArrayList<CommonConfigBean.ActivityType>) ac.readObject(Const.ACTIVITY_TYPE_LIST);
        for (CommonConfigBean.ActivityType activityType : list) {
            typeColorMap.put(activityType.getId(), activityType.getColor());
        }
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        GroupActiveListBean list = (GroupActiveListBean) ApiClient.getApi().getGroupActiveList(groupId, AppContext.PAGE_SIZE + "", page + "", ac.getLoginUid());

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        if (list.getActivityList() != null && list.getActivityList().size() > 0) {
            for (GroupActiveListBean.GroupActive groupActive : list.getActivityList()) {
                groupActive.setTypeColor(typeColorMap.get(groupActive.getTypeId()));
                groupActive.setRemark(Func.formatNickName(_activity, groupActive.getCreatorInfo().getId(), groupActive.getCreatorInfo().getNickname()));
            }
            models.addAll(list.getActivityList());
        }

        hasMore = models.size() == AppContext.PAGE_SIZE;
        this.page = page;
        return models;
    }

}
