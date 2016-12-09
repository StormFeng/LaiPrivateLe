package com.lailem.app.tpl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.bean.Result;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;
import com.lailem.app.widget.pulltorefresh.helper.IViewHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 列表条目基础模板
 */
public abstract class BaseTpl<Model> extends LinearLayout implements ApiCallback {
    protected AppContext ac;
    protected BaseActivity _activity;
    protected Intent _Intent;
    protected Bundle _Bundle;

    protected BaseListAdapter<Model> adapter;
    protected IDataSource<Model> dataSource;
    protected ArrayList<Model> data;
    protected AbsListView absListView;
    protected IViewHelper listViewHelper;
    protected int itemViewType = -1;
    protected View root;
    protected Context context;

    public BaseTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTpl(Context context) {
        super(context);
    }

    public BaseTpl(Context context, int itemViewType) {
        super(context);
    }

    public void config(BaseListAdapter adapter, ArrayList<Model> data, IDataSource<Model> dataSource, AbsListView absListView, IViewHelper listViewHelper) {
        this.adapter = adapter;
        this.dataSource = dataSource;
        this.data = data;
        this.absListView = absListView;
        this.listViewHelper = listViewHelper;
    }

    public void init(Context context, int itemViewType) {
        this.itemViewType = itemViewType;
        this.context = context;
        this._activity = (BaseActivity) context;
        this._Intent = _activity.getIntent();
        if (this._Intent != null) {
            this._Bundle = _Intent.getExtras();
        }
        ac = (AppContext) context.getApplicationContext();

        initView();
    }

    protected void initView() {
        root = View.inflate(context, getLayoutId(), null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(root, params);
        ButterKnife.bind(this, root);
    }

    protected void onItemClick() {

    }

    protected void onItemLongClick() {
    }

    protected abstract int getLayoutId();

    public abstract void setBean(Model bean, int position);

    @Override
    public void onApiStart(String tag) {
    }

    @Override
    public void onApiLoading(long count, long current, String tag) {
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
    }

    @Override
    public void onApiFailure(Throwable t, int errorNo, String strMsg, String tag) {
        onApiError(tag);
    }

    @Override
    public void onParseError(String tag) {
        onApiError(tag);
    }

    protected void onApiError(String tag) {
        AppContext.showToast("网络出错啦，请重试");
    }

}
