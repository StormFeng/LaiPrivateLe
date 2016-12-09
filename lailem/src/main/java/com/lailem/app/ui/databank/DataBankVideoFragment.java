package com.lailem.app.ui.databank;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.adapter.datasource.DataBankVideoDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListFragment;
import com.lailem.app.loadfactory.DataBankMapLoadViewFactory;
import com.lailem.app.loadfactory.DataBankVideoLoadViewFactory;
import com.lailem.app.tpl.DataBankSectionTpl;
import com.lailem.app.tpl.DataBankVideoTpl;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DataBankVideoFragment extends BaseSectionListFragment<Object> {
    public static final int TPL_VIDEO = 1;
    private final String groupId;

    public DataBankVideoFragment(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
        listViewHelper.refresh();
    }

    private void initView() {
        listView.setDividerHeight(0);
    }

    @Override
    protected IDataSource<Object> getDataSource() {
        return new DataBankVideoDataSource(_activity,groupId);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, DataBankSectionTpl.class);
        tpls.add(TPL_VIDEO, DataBankVideoTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new DataBankVideoLoadViewFactory();
    }
}
