package com.lailem.app.widget.pulltorefresh.helper;

public interface IViewHelper {
	 void refresh();
	 ILoadViewFactory.ILoadMoreView getLoadMoreView();
	 ILoadViewFactory.ILoadView getLoadView();
}