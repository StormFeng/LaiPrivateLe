package com.lailem.app.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.lailem.app.R;
import com.lailem.app.widget.pulltorefresh.GridViewWithHeaderAndFooter;
import com.lailem.app.widget.pulltorefresh.HeaderFooterGridView;
import com.lailem.app.widget.pulltorefresh.PullToRefreshGridView;
import com.lailem.app.widget.pulltorefresh.helper.DeFaultLoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.GridViewHelper;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.OnStateChangeListener;

import java.util.ArrayList;

public abstract class BaseGridListActivity<Model> extends BaseActivity implements OnItemClickListener, OnStateChangeListener<ArrayList<Model>>, OnItemLongClickListener {

    protected GridViewHelper<Model> listViewHelper;

    protected PullToRefreshGridView refreshGridView;

    protected GridView gridView;

    protected IDataSource<Model> dataSource;

    protected ArrayList<Model> resultList;

    protected BaseListAdapter<Model> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() == -1) {
            setContentView(R.layout.c_grid_list_activity);
        } else {
            setContentView(getLayoutId());
        }
        refreshGridView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshListView);
        listViewHelper = new GridViewHelper<Model>(refreshGridView);
        listViewHelper.init(getLoadViewFactory());
        // 设置数据源
        dataSource = getDataSource();
        listViewHelper.setDataSource(this.dataSource);
        gridView = refreshGridView.getRefreshableView();
        gridView.setScrollBarStyle(GridView.SCROLLBARS_OUTSIDE_OVERLAY);
        gridView.setOnItemClickListener(this);
        resultList = dataSource.getResultList();

        adapter = (BaseListAdapter<Model>) getAdapter();
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        // 设置适配器
        listViewHelper.setAdapter(adapter);
        listViewHelper.setOnStateChangeListener(this);
    }

    protected IDataAdapter<ArrayList<Model>> getAdapter() {
        return new BaseListAdapter(gridView, this, dataSource, getTemplateClass(), listViewHelper);
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
    protected abstract Class getTemplateClass();

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
