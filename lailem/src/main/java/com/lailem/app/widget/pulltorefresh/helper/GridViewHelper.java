package com.lailem.app.widget.pulltorefresh.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;

import com.lailem.app.utils.NetworkUtils;
import com.lailem.app.widget.pulltorefresh.PullToRefreshBase;
import com.lailem.app.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory.ILoadMoreView;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory.ILoadView;

import java.util.ArrayList;

public class GridViewHelper<Model> implements OnRefreshListener<GridView>, IViewHelper {
    private IDataAdapter<ArrayList<Model>> dataAdapter;
    private PullToRefreshBase<GridView> plv;
    private IDataSource<Model> dataSource;
    private GridView mGridView;
    private Context context;
    private OnStateChangeListener<ArrayList<Model>> onStateChangeListener;
    private AsyncTask<Void, Void, ArrayList<Model>> asyncTask;
    private long loadDataTime = -1;
    /**
     * 是否还有更多数据。如果服务器返回的数据为空的话，就说明没有更多数据了，也就没必要自动加载更多数据
     */
    private boolean hasMoreData = true;
    private ILoadView mLoadView;
    private ILoadMoreView mLoadMoreView;
    public static ILoadViewFactory loadViewFactory = new DeFaultLoadViewFactory();

    @SuppressLint("NewApi")
    public GridViewHelper(PullToRefreshBase pullToRefreshAdapterViewBase) {
        super();
        this.context = pullToRefreshAdapterViewBase.getContext().getApplicationContext();
        this.autoLoadMore = true;
        this.plv = pullToRefreshAdapterViewBase;
        mGridView = plv.getRefreshableView();
        mGridView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mGridView.setCacheColorHint(Color.TRANSPARENT);
        plv.setScrollLoadEnabled(true);
        plv.setOnRefreshListener(this);
        mGridView.setOnScrollListener(onScrollListener);
        mGridView.setOnItemSelectedListener(onItemSelectedListener);
    }

    public void init(ILoadViewFactory loadViewFactory) {
        this.mLoadView = loadViewFactory.madeLoadView();
        this.mLoadMoreView = loadViewFactory.madeLoadMoreView();
        mLoadView.init(mGridView, onClickRefresListener);
        mLoadMoreView.init(mGridView, onClickLoadMoreListener);
    }

    /**
     * 设置数据源，用于加载数据
     *
     * @param dataSource
     */
    public void setDataSource(IDataSource<Model> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 设置适配器，用于显示数据
     *
     * @param adapter
     */
    public void setAdapter(IDataAdapter<ArrayList<Model>> adapter) {
        mGridView.setAdapter(adapter);
        this.dataAdapter = adapter;
    }

    /**
     * 设置状态监听，监听开始刷新，刷新成功，开始加载更多，加载更多成功
     *
     * @param onStateChangeListener
     */
    public void setOnStateChangeListener(OnStateChangeListener<ArrayList<Model>> onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    /**
     * 刷新，开启异步线程，并且显示加载中的界面，当数据加载完成自动还原成加载完成的布局，并且刷新列表数据
     */
    public void refresh() {
        if (dataAdapter == null || dataSource == null) {
            if (plv != null) {
                plv.onPullUpRefreshComplete();
            }
            return;
        }
        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTask.cancel(true);
        }
        asyncTask = new AsyncTask<Void, Void, ArrayList<Model>>() {
            protected void onPreExecute() {
                mLoadMoreView.showNormal();
                if (dataAdapter.isEmpty()) {
                    mLoadView.showLoading();
                    plv.onPullUpRefreshComplete();
                } else {
                    mLoadView.restore();
                }
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStartRefresh(dataAdapter);
                }
            }

            ;

            @Override
            protected ArrayList<Model> doInBackground(Void... params) {
                try {
                    return dataSource.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(ArrayList<Model> result) {
                if (result == null) {
                    if (dataAdapter.isEmpty()) {
                        mLoadView.showFail();
                    } else {
                        mLoadView.tipFail();
                    }
                } else {
                    loadDataTime = System.currentTimeMillis();
                    dataAdapter.setData(result, true, dataSource.isPullDownLoadMore());
                    dataAdapter.notifyDataSetChanged();
                    if (dataAdapter.isEmpty()) {
                        mLoadView.showEmpty();
                    } else {
                        mLoadView.restore();
                        plv.getRefreshableView().setSelection(0);
                    }
                    hasMoreData = dataSource.hasMore();
                    if (hasMoreData) {
                        mLoadMoreView.showNormal();
                    } else {
                        mLoadMoreView.showNomore();
                    }
                }
                if (onStateChangeListener != null) {
                    onStateChangeListener.onEndRefresh(dataAdapter, result);
                }

                plv.onPullDownRefreshComplete();
                plv.onPullUpRefreshComplete();

            }

            ;

        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }
    }

    /**
     * 加载更多，开启异步线程，并且显示加载中的界面，当数据加载完成自动还原成加载完成的布局，并且刷新列表数据
     */
    public void loadMore() {
        if (isLoading()) {
            return;
        }
        if (dataAdapter.isEmpty()) {
            refresh();
            return;
        }

        if (dataAdapter == null || dataSource == null) {
            if (plv != null) {
                plv.onPullUpRefreshComplete();
            }
            return;
        }
        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTask.cancel(true);
        }
        asyncTask = new AsyncTask<Void, Void, ArrayList<Model>>() {
            protected void onPreExecute() {
                if (onStateChangeListener != null) {
                    onStateChangeListener.onStartLoadMore(dataAdapter);
                }
                mLoadMoreView.showLoading();
            }

            @Override
            protected ArrayList<Model> doInBackground(Void... params) {
                try {
                    return dataSource.loadMore();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(ArrayList<Model> result) {
                if (result == null) {
                    mLoadView.tipFail();
                    mLoadMoreView.showFail();
                } else {
                    dataAdapter.setData(result, false, dataSource.isPullDownLoadMore());
                    dataAdapter.notifyDataSetChanged();
                    if (dataAdapter.isEmpty()) {
                        mLoadView.showEmpty();
                    } else {
                        mLoadView.restore();
                    }
                    hasMoreData = dataSource.hasMore();
                    if (hasMoreData) {
                        mLoadMoreView.showNormal();
                    } else {
                        mLoadMoreView.showNomore();
                    }
                }
                if (onStateChangeListener != null) {
                    onStateChangeListener.onEndLoadMore(dataAdapter, result);
                }
            }

            ;
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }
    }

    /**
     * 做销毁操作，比如关闭正在加载数据的异步线程等
     */
    public void destory() {
        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    /**
     * 是否正在加载中
     *
     * @return
     */
    public boolean isLoading() {
        return asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED;
    }

    public GridView getGridView() {
        return plv.getRefreshableView();
    }

    /**
     * 获取上次刷新数据的时间（数据成功的加载），如果数据没有加载成功过，那么返回-1
     *
     * @return
     */
    public long getLoadDataTime() {
        return loadDataTime;
    }

    public OnStateChangeListener<ArrayList<Model>> getOnStateChangeListener() {
        return onStateChangeListener;
    }

    public IDataAdapter<ArrayList<Model>> getAdapter() {
        return dataAdapter;
    }

    public IDataSource<Model> getDataSource() {
        return dataSource;
    }

    public PullToRefreshBase<GridView> getPullToRefreshAdapterViewBase() {
        return plv;
    }

    @Override
    public ILoadView getLoadView() {
        return mLoadView;
    }

    @Override
    public ILoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
        if (!isLoading()) {
            mLoadMoreView.showNormal();
        }
    }

    private boolean autoLoadMore = true;

    public boolean isAutoLoadMore() {
        return autoLoadMore;
    }

    private OnClickListener onClickLoadMoreListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            loadMore();
        }
    };
    private OnClickListener onClickRefresListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            refresh();
        }
    };
    /**
     * 滚动到底部自动加载更多数据
     */
    private OnScrollListener onScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            if (autoLoadMore) {
                if (hasMoreData) {
                    if (!plv.isPullRefreshing()) {// 如果不是刷新状态

                        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行

                            // 如果网络可以用

                            if (NetworkUtils.hasNetwork(context)) {
                                loadMore();
                            } else {
                                if (!isLoading()) {
                                    mLoadMoreView.showFail();
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };
    /**
     * 针对于电视 选择到了底部项的时候自动加载更多数据
     */
    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
            if (autoLoadMore) {
                if (hasMoreData) {
                    if (listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                        // 如果网络可以用
                        if (NetworkUtils.hasNetwork(context)) {
                            loadMore();
                        }
                    }
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
        refresh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        loadMore();
    }

    public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
        plv.doPullRefreshing(smoothScroll, delayMillis);
    }

}