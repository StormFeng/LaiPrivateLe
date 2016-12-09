package com.lailem.app.ui.chat;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.NotificationListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.dao.Message;
import com.lailem.app.tpl.notification.NotificationActivityStartTipTpl;
import com.lailem.app.tpl.notification.NotificationAgreeGroupApplyTpl;
import com.lailem.app.tpl.notification.NotificationApplyJoinGroupTextTpl;
import com.lailem.app.tpl.notification.NotificationApplyJoinGroupVoiceTpl;
import com.lailem.app.tpl.notification.NotificationExitGroupTpl;
import com.lailem.app.tpl.notification.NotificationGroupDismissTpl;
import com.lailem.app.tpl.notification.NotificationGroupNoticeTpl;
import com.lailem.app.tpl.notification.NotificationJoinGroupTpl;
import com.lailem.app.tpl.notification.NotificationRemoveGTpl;
import com.lailem.app.tpl.notification.NotificationSystemNoticeTpl;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationListActivity extends BaseListActivity<Message> {
    public static final int TPL_ACTIVITY_START_TIP = 0;
    public static final int TPL_GROUP_NOTICE = 1;
    public static final int TPL_APPLY_JOIN_GROUP_TEXT = 2;
    public static final int TPL_APPLY_JOIN_GROUP_VOICE = 3;
    public static final int TPL_SYSTEM_NOTICE = 4;
    public static final int TPL_AGREE_GROUP_APPLY = 5;
    public static final int TPL_GROUP_DISMESS = 6;
    public static final int TPL_EXIT_GROUP = 7;
    public static final int TPL_JOIN_GROUP = 8;
    public static final int TPL_REMOVE_G = 9;

    @Bind(R.id.topbar)
    TopBarView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
        //通知类消息有新消息时由于是插入列表的顶部，在用户正在查看通知消息时，有新的消息进来暂无需即时更新到该页面，故不需要注册相关的监听器
        String tId = getIntent().getStringExtra("tId");
        String conversationId = getIntent().getStringExtra("conversationId");
        String cType = getIntent().getStringExtra("cType");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        topbar.setTitle("通知").setLeftText("关闭", UIHelper.finish(this));
        listView.setDivider(getResources().getDrawable(android.R.color.transparent));
        listView.setDividerHeight((int) TDevice.dpToPixel(7.7f));
    }

    @Override
    protected IDataSource<Message> getDataSource() {
        return new NotificationListDataSource(this, getIntent().getStringExtra("conversationId"));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(TPL_ACTIVITY_START_TIP, NotificationActivityStartTipTpl.class);
        tpls.add(TPL_GROUP_NOTICE, NotificationGroupNoticeTpl.class);
        tpls.add(TPL_APPLY_JOIN_GROUP_TEXT, NotificationApplyJoinGroupTextTpl.class);
        tpls.add(TPL_APPLY_JOIN_GROUP_VOICE, NotificationApplyJoinGroupVoiceTpl.class);
        tpls.add(TPL_SYSTEM_NOTICE, NotificationSystemNoticeTpl.class);
        tpls.add(TPL_AGREE_GROUP_APPLY, NotificationAgreeGroupApplyTpl.class);
        tpls.add(TPL_GROUP_DISMESS, NotificationGroupDismissTpl.class);
        tpls.add(TPL_EXIT_GROUP, NotificationExitGroupTpl.class);
        tpls.add(TPL_JOIN_GROUP, NotificationJoinGroupTpl.class);
        tpls.add(TPL_REMOVE_G,NotificationRemoveGTpl.class);
        return tpls;
    }

}
