package com.lailem.app.ui.active;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveNearListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.tpl.ActiveConditionsTpl;
import com.lailem.app.tpl.ActiveNearTpl;
import com.lailem.app.tpl.ActiveSortSectionTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActiveNearListActivity extends BaseSectionListActivity<Object> {
    public static final int TPL_CONDITION = 1;
    public static final int TPL_ACTIVE_NEAR = 2;
    @Bind(R.id.topbar)
    TopBarView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        listViewHelper.refresh();
    }

    @Override
    protected int getLayoutId() {
        return super.getLayoutId();
    }

    private void initView() {
        topbar.setTitle("附近活动").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new ActiveNearListDataSource(this);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, ActiveSortSectionTpl.class);
        tpls.add(TPL_CONDITION, ActiveConditionsTpl.class);
        tpls.add(TPL_ACTIVE_NEAR, ActiveNearTpl.class);
        return tpls;
    }

    @Override
    public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
        super.onEndRefresh(adapter, result);
        if (resultList.size() == 0) {
            return;
        }
        listView.setSelection(0);

    }
}
