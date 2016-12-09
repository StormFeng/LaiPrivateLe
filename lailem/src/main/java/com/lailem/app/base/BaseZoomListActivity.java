package com.lailem.app.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.lailem.app.R;
import com.lailem.app.widget.PullToZoomListView;
import com.lailem.app.widget.PullToZoomListView.CallBack;
import com.lailem.app.widget.pulltorefresh.PullZoomRefresListView;
import com.lailem.app.widget.pulltorefresh.helper.DeFaultLoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.ListViewHelper;
import com.lailem.app.widget.pulltorefresh.helper.OnStateChangeListener;

import java.util.ArrayList;

/**
 * 列表activity基类 Created by XuYang on 15/4/15.
 */
public abstract class BaseZoomListActivity<Model> extends BaseActivity implements OnItemClickListener, OnStateChangeListener<ArrayList<Model>>, OnItemLongClickListener, CallBack {

    protected ListViewHelper<Model> listViewHelper;

    protected PullZoomRefresListView refreshListView;

    protected PullToZoomListView listView;

    protected IDataSource<Model> dataSource;

    protected ArrayList<Model> resultList;

    protected BaseListAdapter<Model> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() == -1) {
            setContentView(R.layout.c_zoom_list_activity);
        } else {
            setContentView(getLayoutId());
        }
        refreshListView = (PullZoomRefresListView) findViewById(R.id.pullToRefreshListView);
        listViewHelper = new ListViewHelper<Model>(refreshListView);
        listViewHelper.init(getLoadViewFactory());
        // 设置数据源
        dataSource = getDataSource();
        listViewHelper.setDataSource(this.dataSource);
        listView = (PullToZoomListView) refreshListView.getRefreshableView();
        listView.setCallBack(this);
        listView.setOnItemClickListener(this);
        listView.setDivider(getResources().getDrawable(R.drawable.c_divider_line));
        resultList = dataSource.getResultList();
        adapter = (BaseListAdapter<Model>) getAdapter();
        // 设置适配器
        listViewHelper.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        listViewHelper.setOnStateChangeListener(this);
    }

    protected IDataAdapter<ArrayList<Model>> getAdapter() {
        return new BaseMultiTypeListAdapter(listView, this, dataSource, getTemplateClasses(), listViewHelper);
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
    public void onDestroy() {
        super.onDestroy();
        // 释放资源
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

    @Override
    public void handZoom(float mScale) {

    }

}
