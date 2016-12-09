package com.lailem.app.widget.pulltorefresh.helper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.widget.pulltorefresh.HeaderFooterGridView;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory.ILoadMoreView;

public class DefaultLoadMoreHelper implements ILoadMoreView {

    protected View footView;
    protected TextView text;
    protected ProgressBar progressBar;

    protected OnClickListener onClickRefreshListener;

    @Override
    public void init(AbsListView listView, OnClickListener onClickRefreshListener) {
        footView = LayoutInflater.from(listView.getContext()).inflate(R.layout.layout_listview_foot, listView, false);
        text = (TextView) footView.findViewById(R.id.text);
        progressBar = (ProgressBar) footView.findViewById(R.id.progressBar);
        if (listView instanceof ListView) {
            ((ListView) listView).addFooterView(footView);
        } else if (listView instanceof HeaderFooterGridView) {
            ((HeaderFooterGridView) listView).addFooterView(footView);
        }
        this.onClickRefreshListener = onClickRefreshListener;
        showNormal();
    }

    @Override
    public void showNormal() {
        text.setText("点击加载更多");
        progressBar.setVisibility(View.GONE);
        footView.setOnClickListener(onClickRefreshListener);
    }

    @Override
    public void showLoading() {
        text.setText("正在加载中..");
        progressBar.setVisibility(View.VISIBLE);
        footView.setOnClickListener(null);
    }

    @Override
    public void showFail() {
        progressBar.setVisibility(View.GONE);
        text.setText("加载失败，点击重新加载");
        footView.setOnClickListener(onClickRefreshListener);
    }

    @Override
    public void showNomore() {
        footView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
        text.setText("已经加载完毕");
        footView.setOnClickListener(null);
    }

}