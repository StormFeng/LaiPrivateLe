package com.lailem.app.base;

import android.app.Activity;
import android.content.Context;

import com.lailem.app.AppContext;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

public abstract class BaseListDataSource<Object> implements IDataSource<Object> {
    public static int FIRST_PAGE_NO = 1;
    protected AppContext ac;
    protected Activity _activity;
    protected Context context;

    protected int page = 0;
    protected boolean hasMore = false;

    protected ArrayList<Object> data = new ArrayList<Object>();

    public BaseListDataSource(Context context) {
        this.context = context;
        this._activity = (Activity) context;
        this.ac = (AppContext) context.getApplicationContext();
    }

    @Override
    public ArrayList<Object> refresh() throws Exception {
        return load(FIRST_PAGE_NO);
    }

    @Override
    public ArrayList<Object> loadMore() throws Exception {
        return load(page + 1);
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public boolean isPullDownLoadMore() {
        return false;
    }

    @Override
    public int getRefreshInsertIndex() {
        return 0;
    }

    @Override
    public ArrayList<Object> getResultList() {
        return data;
    }

    protected abstract ArrayList<Object> load(int page) throws Exception;

}
