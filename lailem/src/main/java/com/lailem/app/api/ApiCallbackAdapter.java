package com.lailem.app.api;

import com.lailem.app.AppContext;
import com.lailem.app.bean.Result;

public class ApiCallbackAdapter implements ApiCallback {

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
		t.printStackTrace();
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
