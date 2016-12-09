package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.GroupNearListDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ListViewHelper;

import butterknife.Bind;
import butterknife.OnClick;

public class GroupSortSectionTpl extends BaseTpl<Object> {
	@Bind(R.id.sortRg)
	RadioGroup sortRg;

	public GroupSortSectionTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_group_sort_section;
	}

	@Override
	public void setBean(Object bean, int position) {
		GroupNearListDataSource ds = (GroupNearListDataSource) dataSource;
		String sort = ds.getSort();
		if (GroupNearListDataSource.SORT_TIME.equals(ds.getSort())) {
			sortRg.check(R.id.time);
		} else if (GroupNearListDataSource.SORT_HOT.equals(ds.getSort())) {
			sortRg.check(R.id.hot);
		} else if (GroupNearListDataSource.SORT_DISTANCE.equals(ds.getSort())) {
			sortRg.check(R.id.distance);
		}
	}

	@OnClick({ R.id.time, R.id.hot, R.id.distance })
	public void clickSort(View v) {
		GroupNearListDataSource ds = (GroupNearListDataSource) dataSource;
		switch (v.getId()) {
		case R.id.time:
			ds.setSort(GroupNearListDataSource.SORT_TIME);
			break;
		case R.id.hot:
			ds.setSort(GroupNearListDataSource.SORT_HOT);
			break;
		case R.id.distance:
			ds.setSort(GroupNearListDataSource.SORT_DISTANCE);
			break;
		}
		ListViewHelper lvh = (ListViewHelper) this.listViewHelper;
		lvh.doPullRefreshing(true, 0);
	}

}
