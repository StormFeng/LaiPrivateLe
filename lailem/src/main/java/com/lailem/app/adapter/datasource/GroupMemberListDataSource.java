package com.lailem.app.adapter.datasource;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean;
import com.lailem.app.jsonbean.activegroup.GroupMemberBean.GroupMember;
import com.lailem.app.ui.group.GroupMemberListActivity;
import com.lailem.app.utils.Func;

import java.util.ArrayList;

public class GroupMemberListDataSource extends BaseListDataSource<Object> {
    // 1（创建者）、2（管理员）、3（普通成员）
    public static final String CREATOR = "1";
    public static final String MANAGER = "2";
    public static final String NORMAL = "3";

    private String groupId;

    public GroupMemberListDataSource(Context context, String groupId) {
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
            GroupMember beforeMember = null;
            if (i > 0) {
                beforeMember = list.getMemberList().get(i - 1);
            }
            if (beforeMember == null) {
                // 添加群主section
                if (page == FIRST_PAGE_NO) {
                    models.add(new ObjectWrapper("群主", BaseMultiTypeListAdapter.TPL_SECTION));
                }
            } else if (!beforeMember.getRoleType().equals(curMember.getRoleType())) {
                if (curMember.getRoleType().equals(MANAGER)) {
                    models.add(new ObjectWrapper("管理员", BaseMultiTypeListAdapter.TPL_SECTION));
                }
                if (curMember.getRoleType().equals(NORMAL)) {
                    models.add(new ObjectWrapper("成员", BaseMultiTypeListAdapter.TPL_SECTION));
                }
            }

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
