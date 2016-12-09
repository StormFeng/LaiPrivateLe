package com.lailem.app.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.widget.pulltorefresh.helper.VaryViewHelper;

public class MyLoadViewHelper {
	private VaryViewHelper helper;
	private OnClickListener onClickRefreshListener;
	private Context context;

	public void init(View content, OnClickListener onClickRefreshListener) {
		helper = new VaryViewHelper(content);
		this.context = content.getContext().getApplicationContext();
		this.onClickRefreshListener = onClickRefreshListener;
	}

	public void restore() {
		helper.restoreView();
	}

	public void showLoading() {
		View layout = helper.inflate(R.layout.load_ing);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText("加载中...");
		helper.showLayout(layout);
	}

	public void tipFail() {
		AppContext.showToast("网络出错，加载失败");
	}

	public void showFail() {
		View layout = helper.inflate(R.layout.load_error);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText("网络出错，加载失败");
		Button button = (Button) layout.findViewById(R.id.button1);
		button.setText("重试");
		button.setOnClickListener(onClickRefreshListener);
		helper.showLayout(layout);
	}

	public void showEmpty() {
		View layout = helper.inflate(R.layout.load_empty);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText("暂无数据");
		Button button = (Button) layout.findViewById(R.id.button1);
		button.setText("重试");
		button.setOnClickListener(onClickRefreshListener);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
		helper.showLayout(layout);
	}

}