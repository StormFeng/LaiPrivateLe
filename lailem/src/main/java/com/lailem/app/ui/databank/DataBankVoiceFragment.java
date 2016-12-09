package com.lailem.app.ui.databank;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.adapter.datasource.DataBankVoiceDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListFragment;
import com.lailem.app.loadfactory.DataBankVoiceLoadViewFactory;
import com.lailem.app.tpl.DataBankSectionTpl;
import com.lailem.app.tpl.DataBankVoiceTpl;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DataBankVoiceFragment extends BaseSectionListFragment<Object> {

    public static final int TPL_VOICE = 1;
    private final String groupId;

    public DataBankVoiceFragment(String groupId) {
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
        return new DataBankVoiceDataSource(_activity, groupId);
    }

    @Override
    protected ArrayList<Class> getTemplateClasses() {
        ArrayList<Class> tpls = new ArrayList<Class>();
        tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, DataBankSectionTpl.class);
        tpls.add(TPL_VOICE, DataBankVoiceTpl.class);
        return tpls;
    }

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new DataBankVoiceLoadViewFactory();
    }
}
