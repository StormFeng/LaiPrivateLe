package com.lailem.app.ui.me;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MeMessageListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.dao.Message;
import com.lailem.app.loadfactory.MeMessageListLoadViewFactory;
import com.lailem.app.tpl.MeMessageListTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeMessageListActivity extends BaseListActivity<Message> {
    @Bind(R.id.topbar)
    TopBarView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
        // 消息列表有新消息时由于是插入列表的顶部，在用户正在查看消息列表时，有新的消息进来暂无需即时更新到该页面，故不需要注册相关的监听器
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        topbar.setTitle("互动消息").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
    }

    @Override
    protected IDataSource<Message> getDataSource() {
        return new MeMessageListDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(MeMessageListTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new MeMessageListLoadViewFactory();
    }
}
