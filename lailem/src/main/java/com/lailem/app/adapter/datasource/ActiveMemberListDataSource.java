package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean.GroupMember;
import com.lailem.app.ui.group.GroupMemberListActivity;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class ActiveMemberListDataSource extends BaseListDataSource<Object> {
    // 1（创建者）、2（管理员）、3（普通成员）
    public static final String CREATOR = "1";
    public static final String MANAGER = "2";
    public static final String NORMAL = "3";

    private String groupId;

    public ActiveMemberListDataSource(Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    protected ArrayList<Object> load(int page) throws Exception {
        ArrayList<Object> models = new ArrayList<Object>();
        GroupMemberBean list = (GroupMemberBean) ApiClient.getApi().membeSync(groupId, AppContext.PAGE_SIZE + "", page + "", ac.getLoginUid());

        if (list.isNotOK()) {
            ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
            return models;
        }

        for (int i = 0; i < list.getMemberList().size(); i++) {
            GroupMember curMember = list.getMemberList().get(i);
            if (!TextUtils.isEmpty(curMember.getGroupNickname())) {
                curMember.setRemark(Func.formatNickName(_activity, curMember.getMemberId(), curMember.getGroupNickname()));
            } else {
                curMember.setRemark(Func.formatNickName(_activity, curMember.getMemberId(), curMember.getNickname()));
            }

            curMember.setItemViewType(GroupMemberListActivity.TPL_MEMBER);
            models.add(curMember);

        }

        hasMore = list.getMemberList() != null && list.getMemberList().size() == AppContext.PAGE_SIZE;
        this.page = page;

        return models;
    }
}
