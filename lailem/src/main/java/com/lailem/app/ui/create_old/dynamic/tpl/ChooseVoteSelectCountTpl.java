package com.lailem.app.ui.create_old.dynamic.tpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.ChooseVoteSelectCountDataSource.SelectCountBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.ChooseVoteSelectCountActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class ChooseVoteSelectCountTpl extends BaseTpl<SelectCountBean> {
	@Bind(R.id.radio)
	RadioButton radio;

	private SelectCountBean bean;

	public ChooseVoteSelectCountTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_choose_vote_select_count;
	}

	@Override
	public void setBean(SelectCountBean bean, int position) {
		this.bean = bean;
		radio.setText(bean.getName());
		radio.setChecked(position == adapter.getCheckedPosition());
	}

	@OnClick(R.id.radio)
	public void clickRadio() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(ChooseVoteSelectCountActivity.BUNDLE_KEY_NAME, bean.getName());
		bundle.putString(ChooseVoteSelectCountActivity.BUNDLE_KEY_VALUE, bean.getValue());
		intent.putExtras(bundle);
		_activity.setResult(Activity.RESULT_OK, intent);
		_activity.finish();
	}

}
