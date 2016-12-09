package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ActiveNearListDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ListViewHelper;

import butterknife.Bind;
import butterknife.OnClick;

public class ActiveSortSectionTpl extends BaseTpl<Object> {
	@Bind(R.id.sortRg)
	RadioGroup sortRg;

	public ActiveSortSectionTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_active_sort_section;
	}

	@Override
	public void setBean(Object bean, int position) {
		ActiveNearListDataSource ds = (ActiveNearListDataSource) dataSource;
		String sort = ds.getSort();
		if (ActiveNearListDataSource.SORT_TIME.equals(ds.getSort())) {
			sortRg.check(R.id.time);
		} else if (ActiveNearListDataSource.SORT_HOT.equals(ds.getSort())) {
			sortRg.check(R.id.hot);
		} else if (ActiveNearListDataSource.SORT_DISTANCE.equals(ds.getSort())) {
			sortRg.check(R.id.distance);
		}
	}

	@OnClick({ R.id.time, R.id.hot, R.id.distance })
	public void clickSort(View v) {
		ActiveNearListDataSource ds = (ActiveNearListDataSource) dataSource;
		switch (v.getId()) {
		case R.id.time:
			ds.setSort(ActiveNearListDataSource.SORT_TIME);
			break;
		case R.id.hot:
			ds.setSort(ActiveNearListDataSource.SORT_HOT);
			break;
		case R.id.distance:
			ds.setSort(ActiveNearListDataSource.SORT_DISTANCE);
			break;
		}
		ListViewHelper lvh = (ListViewHelper) this.listViewHelper;
		lvh.doPullRefreshing(true, 0);
	}

}
