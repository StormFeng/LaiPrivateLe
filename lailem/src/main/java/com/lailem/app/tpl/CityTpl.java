package com.lailem.app.tpl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.dao.Region;
import com.lailem.app.ui.me.ChooseCityTwoActivity;
import com.lailem.app.utils.Const;

import butterknife.Bind;
import butterknife.OnClick;

public class CityTpl extends BaseTpl<Region> {
	@Bind(R.id.name)
	RadioButton name_cb;

	private Region bean;
	private int position;

	public CityTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_choose_city_two;
	}

	@Override
	public void setBean(Region bean, int position) {
		this.bean = bean;
		this.position = position;

		name_cb.setText(bean.getName());
		name_cb.setChecked(adapter.getCheckedPosition() == position);
	}

	@OnClick(R.id.name)
	public void clickName() {

		adapter.setCheckedPosition(position);
		adapter.notifyDataSetChanged();

		Bundle _Bundle = _activity.getIntent().getExtras();
		ApiClient.getApi().changePersonInfo(this, ac.getLoginUid(), null, bean.getRId(), null, null, null, null, _Bundle.getString(ChooseCityTwoActivity.BUNDLE_KEY_PROVINCEID), null);

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
			Bundle _Bundle = _activity.getIntent().getExtras();
			ac.setProperty(Const.USER_CITY, bean.getName());
			ac.setProperty(Const.USER_PROVINCE, _Bundle.getString(ChooseCityTwoActivity.BUNDLE_KEY_PROVINCENAME));
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
