package com.lailem.app.loadfactory;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.widget.pulltorefresh.helper.DefaultLoadMoreHelper;
import com.lailem.app.widget.pulltorefresh.helper.ILoadViewFactory;
import com.lailem.app.widget.pulltorefresh.helper.VaryViewHelper;

public class ActiveDynamicLoadViewFactory implements ILoadViewFactory {

	@Override
	public ILoadMoreView madeLoadMoreView() {
		return new DefaultLoadMoreHelper();
	}

	@Override
	public ILoadView madeLoadView() {
		return new ActiveDynamicLoadViewHelper();
	}

	public static class ActiveDynamicLoadViewHelper implements ILoadView {

		private VaryViewHelper helper;
		private OnClickListener onClickRefreshListener;
		private Context context;

		@Override
		public void init(AbsListView mListView, OnClickListener onClickRefreshListener) {
			helper = new VaryViewHelper(mListView);
			this.context = mListView.getContext().getApplicationContext();
			this.onClickRefreshListener = onClickRefreshListener;
		}

		@Override
		public void restore() {
			helper.restoreView();
		}

		@Override
		public void showLoading() {
			View layout = helper.inflate(R.layout.load_ing);
			TextView textView = (TextView) layout.findViewById(R.id.textView1);
			textView.setText("加载中...");
			helper.showLayout(layout);
		}

		@Override
		public void tipFail() {
			AppContext.showToast("网络出错，加载失败");
		}

		@Override
		public void showFail() {
			View layout = helper.inflate(R.layout.load_error);
			TextView textView = (TextView) layout.findViewById(R.id.textView1);
			textView.setText("网络出错，加载失败");
			Button button = (Button) layout.findViewById(R.id.button1);
			button.setText("重试");
			button.setOnClickListener(onClickRefreshListener);
			helper.showLayout(layout);
		}

		@Override
		public void showEmpty() {
			View layout = helper.inflate(R.layout.load_empty_active_dynamic);
			layout.findViewById(R.id.text).setOnClickListener(onClickRefreshListener);
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
			helper.showLayout(layout);
		}

	}

}
