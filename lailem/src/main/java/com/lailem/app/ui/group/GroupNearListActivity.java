package com.lailem.app.ui.group;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.GroupNearListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.tpl.GroupNearTpl;
import com.lailem.app.tpl.GroupSortSectionTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupNearListActivity extends BaseSectionListActivity<Object> {

	public static final int TPL_GROUP_NEAR = 1;

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
		topbar.setTitle("附近群组").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
		listView.setDividerHeight(0);
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		return new GroupNearListDataSource(this, _Bundle.getString(GroupTagsActivity.BUNDLE_KEY_TAG_ID));
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, GroupSortSectionTpl.class);
		tpls.add(TPL_GROUP_NEAR, GroupNearTpl.class);
		return tpls;
	}

	@Override
	public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
		super.onEndRefresh(adapter, result);
        if(resultList.size()==0){
            return;
        }
		listView.setSelection(0);

	}
}
