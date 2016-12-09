package com.lailem.app.tpl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.dao.Region;
import com.lailem.app.ui.me.ChooseCityTwoActivity;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;

public class ProvinceTpl extends BaseTpl<Region> {
	@Bind(R.id.name)
	CheckedTextView name_tv;
	@Bind(R.id.arrow)
	ImageView arrow_iv;

	private Region bean;

	public ProvinceTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_choose_city_one;
	}

	@Override
	public void setBean(Region bean, int position) {
		this.bean = bean;
		name_tv.setText(bean.getName());
		if ("2".equals(bean.getRType())) {
			arrow_iv.setVisibility(GONE);
			name_tv.setChecked(adapter.getCheckedPosition() == position);
		} else if ("1".equals(bean.getRType())) {
			arrow_iv.setVisibility(VISIBLE);
			name_tv.setChecked(false);
		}
	}

	@Override
	protected void onItemClick() {
		super.onItemClick();
		if ("2".equals(bean.getRType())) {
			// 直辖市
			ApiClient.getApi().changePersonInfo(this, ac.getLoginUid(), null, bean.getRId(), null, null, null, null, null, null);
		} else if ("1".equals(bean.getRType())) {
			// 省
			Bundle _Bundle = _activity.getIntent().getExtras();
			String cityName = _Bundle.getString(ChooseCityTwoActivity.BUNDLE_KEY_CITYNAME);
			String cityId = _Bundle.getString(ChooseCityTwoActivity.BUNDLE_KEY_CITYID);
			String provinceName = bean.getName();
			String provinceId = bean.getRId();
			UIHelper.showChooseCityTwo(_activity, provinceName, provinceId, cityName);
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
			ac.setProperty(Const.USER_CITY, bean.getName());
			ac.setProperty(Const.USER_PROVINCE, "");
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
