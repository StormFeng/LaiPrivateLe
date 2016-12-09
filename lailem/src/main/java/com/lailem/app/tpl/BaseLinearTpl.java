package com.lailem.app.tpl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiCallback;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 列表条目基础模板
 */
public abstract class BaseLinearTpl<Model> extends LinearLayout implements ApiCallback {
    protected AppContext ac;
    protected BaseActivity _activity;
    protected Intent _Intent;
    protected Bundle _Bundle;

    protected ArrayList<Model> data;
    protected View root;
    protected Context context;

    public BaseLinearTpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLinearTpl(Context context, ArrayList<Model> data) {
        super(context);
        this.context = context;
        this._activity = (BaseActivity) context;
        this._Intent = _activity.getIntent();
        if (this._Intent != null) {
            this._Bundle = _Intent.getExtras();
        }
        ac = (AppContext) context.getApplicationContext();

        this.data = data;

        root = View.inflate(context, getLayoutId(), null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(root, params);
        ButterKnife.bind(this, root);
    }

    protected abstract int getLayoutId();

    public abstract void setBean();

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
