package com.lailem.app.ui.create_old.dynamic;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ChooseVoteSelectCountDataSource;
import com.lailem.app.base.BaseListActivity;
import com.lailem.app.ui.create_old.dynamic.tpl.ChooseVoteSelectCountTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseVoteSelectCountActivity extends BaseListActivity<Object> {
	private static final int TPL_SELECT_COUNT = 0;

	public static final String BUNDLE_KEY_NAME = "name";
	public static final String BUNDLE_KEY_VALUE = "value";

	@Bind(R.id.topbar)
	TopBarView topbar;

	private int count;
	private int selectCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
		initView();
		listViewHelper.refresh();
	}

	private void initView() {
		topbar.setTitle("多选数").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
		adapter.setCheckedPosition(_Bundle.getInt(CreateDynamicVoteActivity.BUNDLE_KEY_SELECTCOUNT) - 1);
		refreshListView.setPullRefreshEnabled(false);
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		return new ChooseVoteSelectCountDataSource(this, _Bundle.getInt(CreateDynamicVoteActivity.BUNDLE_KEY_COUNT));
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(ChooseVoteSelectCountTpl.class);
		return tpls;
	}

}
