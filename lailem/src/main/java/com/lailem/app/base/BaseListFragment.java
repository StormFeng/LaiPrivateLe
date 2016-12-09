package com.lailem.app.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.lailem.app.R;
import com.lailem.app.widget.pulltorefresh.PullToRefreshListView;
import com.lailem.app.widget.pulltorefresh.helper.DeFaultLoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.ListViewHelper;
import com.lailem.app.widget.pulltorefresh.helper.OnStateChangeListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 列表fragment基类 Created by XuYang on 15/4/15.
 */
public abstract class BaseListFragment<Model> extends BaseFragment implements OnItemClickListener, OnStateChangeListener<ArrayList<Model>>, OnItemLongClickListener {

	protected ListViewHelper<Model> listViewHelper;

	protected PullToRefreshListView refreshListView;

	protected ListView listView;

	protected IDataSource<Model> dataSource;

	protected ArrayList<Model> resultList;

	protected BaseListAdapter<Model> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = null;
		if (getLayoutId() == -1) {
			refreshListView = new PullToRefreshListView(_activity);
			root = refreshListView;
		} else {
			root = inflater.inflate(getLayoutId(), null);
			refreshListView = (PullToRefreshListView) root.findViewById(R.id.pullToRefreshListView);
		}
		if (listViewHelper == null) {
			listViewHelper = new ListViewHelper<Model>(refreshListView);
			listViewHelper.init(getLoadViewFactory());
		}
		// 设置数据源
		if (dataSource == null) {
			dataSource = getDataSource();
		}
		listViewHelper.setDataSource(this.dataSource);
		listView = refreshListView.getRefreshableView();
		listView.setDivider(getResources().getDrawable(R.drawable.c_divider_line));
		listView.setOnItemClickListener(this);
		if (resultList == null) {
			resultList = dataSource.getResultList();
		}

		listViewHelper.setOnStateChangeListener(this);

		// 设置适配器
		if (adapter == null) {
			adapter = new BaseMultiTypeListAdapter(listView, _activity, dataSource, getTemplateClasses(), listViewHelper);
			listViewHelper.setAdapter(adapter);
		}
		adapter.setOnItemClickListener(this);
		adapter.setOnItemLongClickListener(this);
		init();
		ButterKnife.bind(this, root);
		return root;
	}

    protected void init(){
        if (resultList.size() == 0) {
            listViewHelper.refresh();
        }
    }

	/**
	 * 默认
	 * 
	 * @return
	 */
	protected final int getDefaultLayoutId() {
		return -1;
	}

	protected int getLayoutId() {
		return getDefaultLayoutId();
	}

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	protected abstract IDataSource<Model> getDataSource();

	/**
	 * 获取条目模板
	 * 
	 * @return
	 */
	protected abstract ArrayList<Class> getTemplateClasses();

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 释放资源
		listViewHelper.destory();
	}

	/**
	 * 获取加载视图工厂
	 * 
	 * @return
	 */
	protected ILoadViewFactory getLoadViewFactory() {
		return new DeFaultLoadViewFactory();
	}

	public void refresh() {
		listViewHelper.doPullRefreshing(true, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return false;
	}

	@Override
	public void onStartRefresh(IDataAdapter<ArrayList<Model>> adapter) {

	}

	@Override
	public void onEndRefresh(IDataAdapter<ArrayList<Model>> adapter, ArrayList<Model> result) {

	}

	@Override
	public void onStartLoadMore(IDataAdapter<ArrayList<Model>> adapter) {

	}

	@Override
	public void onEndLoadMore(IDataAdapter<ArrayList<Model>> adapter, ArrayList<Model> result) {

	}

}
