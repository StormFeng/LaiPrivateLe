package com.lailem.app.api;

import com.lailem.app.bean.Result;

public interface ApiCallback {

	void onApiStart(String tag);

	void onApiLoading(long count, long current, String tag);

	void onApiSuccess(Result res, String tag);

	void onApiFailure(Throwable t, int errorNo, String strMsg, String tag);

	void onParseError(String tag);

}
