package com.lailem.app.ui.databank;

import android.os.Bundle;
import android.view.View;

import com.lailem.app.adapter.datasource.DataBankAddressDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListFragment;
import com.lailem.app.loadfactory.DataBankMapLoadViewFactory;
import com.lailem.app.loadfactory.MeDynamicLoadViewFactory;
import com.lailem.app.tpl.DataBankAddressTpl;
import com.lailem.app.tpl.DataBankSectionTpl;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class DataBankMapFragment extends BaseSectionListFragment<Object> {
	public static final int TPL_ADDRESS = 1;
    private final String groupId;

    public DataBankMapFragment(String groupId) {
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
		return new DataBankAddressDataSource(_activity,groupId);
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, DataBankSectionTpl.class);
		tpls.add(TPL_ADDRESS, DataBankAddressTpl.class);
		return tpls;
	}

    @Override
    protected ILoadViewFactory getLoadViewFactory() {
        return new DataBankMapLoadViewFactory();
    }
}
