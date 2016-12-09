package com.lailem.app.ui.group;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.GroupNoticeListDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.loadfactory.GroupNotificationLoadViewFactory;
import com.lailem.app.tpl.GroupNoticeTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupNotificationActivity extends BaseListActivity<Object> {
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_SHOW_ADD = "show_add";

    @Bind(R.id.topbar)
    TopBarView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    private void initView() {
        topbar.setTitle(_Bundle.getString(BUNDLE_KEY_TITLE)).setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        if (_Bundle.getBoolean(BUNDLE_KEY_SHOW_ADD)) {
            topbar.setRightImageButton(R.drawable.ic_topbar_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showCreateNotice(_activity, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                }
            });
        }
        listView.setDivider(getResources().getDrawable(android.R.color.transparent));
        listView.setDividerHeight((int) TDevice.dpToPixel(10));
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new GroupNoticeListDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(GroupNoticeTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new GroupNotificationLoadViewFactory();
    }
}
