package com.lailem.app.tpl.chat;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.Func;

import butterknife.Bind;

public class ProvinceHeadTpl extends BaseTpl<Object> {
    @Bind(R.id.loc)
    TextView loc_tv;

    public ProvinceHeadTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_choose_city_one_header;
    }

    @Override
    public void setBean(Object bean, int position) {
        if (!TextUtils.isEmpty(ac.provinceName) && !TextUtils.isEmpty(ac.cityName)) {
            loc_tv.setText(ac.provinceName + " " + ac.cityName);
        } else {
            ac.requestLoc();
        }
    }

    @Override
    protected void onItemClick() {
        super.onItemClick();
        if (!TextUtils.isEmpty(ac.provinceName) && !TextUtils.isEmpty(ac.cityName)) {
            String[] ids = Func.getIdByName(ac.provinceName, ac.cityName);
            ac.provinceId = ids[0];
            ac.cityId = ids[1];
            if (!TextUtils.isEmpty(ac.cityId)) {
                ApiClient.getApi().changePersonInfo(this, ac.getLoginUid(), null, ac.cityId, null, null, null, null, ac.provinceId, null);
            }
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        _activity.showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        _activity.hideWaitDialog();
        if (res.isOK()) {
            ac.setProperty(Const.USER_CITY, ac.cityName);
            ac.setProperty(Const.USER_PROVINCE, ac.provinceName);
            _activity.setResult(Activity.RESULT_OK);
            _activity.finish();
        } else {
            ac.handleErrorCode(_activity, res.errorCode, res.errorInfo);
        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        _activity.hideWaitDialog();
    }
}
