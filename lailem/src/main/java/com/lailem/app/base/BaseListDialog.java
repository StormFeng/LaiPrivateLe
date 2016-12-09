package com.lailem.app.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lailem.app.R;
import com.lailem.app.utils.TDevice;
import com.lailem.app.widget.pulltorefresh.PullToRefreshListView;
import com.lailem.app.widget.pulltorefresh.helper.DeFaultLoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.ListViewHelper;
import com.lailem.app.widget.pulltorefresh.helper.OnStateChangeListener;

import java.util.ArrayList;

public abstract class BaseListDialog<Model> extends Dialog implements OnItemClickListener, OnItemLongClickListener, OnStateChangeListener<ArrayList<Model>> {

	private static final int DEFAULT_THEME = R.style.confirm_dialog;

	protected ListViewHelper<Model> listViewHelper;

	protected PullToRefreshListView refreshListView;

	protected ListView listView;

	protected IDataSource<Model> dataSource;

	protected ArrayList<Model> resultList;

	protected BaseListAdapter<Model> adapter;

	protected Activity _activity;

	public BaseListDialog(Context context) {
		super(context, DEFAULT_THEME);
		init(context);
	}

	public BaseListDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	protected void init(Context context) {
		this._activity = (Activity) context;
		Window w = this.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		w.setAttributes(lp);
		this.setCanceledOnTouchOutside(true);
	}

	protected IDataAdapter<ArrayList<Model>> getAdapter() {
		return new BaseMultiTypeListAdapter(listView, _activity, dataSource, getTemplateClasses(), listViewHelper);
	}

	@Override
	public void show() {
		super.show();
		if (getLayoutId() == -1) {
			setContentView(View.inflate(_activity, R.layout.c_list_dialog, null));
		} else {
			setContentView(View.inflate(_activity, getLayoutId(), null));
		}

		refreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (TDevice.getScreenWidth() - TDevice.dpToPixel(20)), (int) (TDevice.getScreenHeight() / 5 * 3));
		refreshListView.setLayoutParams(params);

		listViewHelper = new ListViewHelper<Model>(refreshListView);
		listViewHelper.init(getLoadViewFactory());
		// 设置数据源
		dataSource = getDataSource();
		listViewHelper.setDataSource(this.dataSource);
		listView = refreshListView.getRefreshableView();
		listView.setOnItemClickListener(this);
		listView.setDivider(_activity.getResources().getDrawable(R.drawable.c_divider_line));
		resultList = dataSource.getResultList();
		adapter = (BaseListAdapter<Model>) getAdapter();
		// 设置适配器
		listViewHelper.setAdapter(adapter);
		adapter.setOnItemClickListener(this);
		adapter.setOnItemLongClickListener(this);
		listViewHelper.setOnStateChangeListener(this);
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

	/**
	 * 获取加载视图工厂
	 * 
	 * @return
	 */
	protected ILoadViewFactory getLoadViewFactory() {
		return new DeFaultLoadViewFactory();
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		listViewHelper.destory();
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
