package com.lailem.app.ui.active;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.R;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.loadfactory.GroupActiveLoadViewFactory;
import com.lailem.app.tpl.GroupActiveTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupActiveListActivity extends BaseListActivity<Object> {
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
        topbar.setTitle("群组活动").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        if (_Bundle.getBoolean(BUNDLE_KEY_SHOW_ADD)) {
            topbar.setRightImageButton(R.drawable.ic_topbar_add, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showCreateActiveType(_activity, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
                }
            });
        }
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new GroupActiveListDataSource(this, _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID));
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(GroupActiveTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new GroupActiveLoadViewFactory();
    }
}
