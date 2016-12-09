package com.lailem.app.tpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import com.lailem.app.R;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean.FeedbackType;
import com.lailem.app.ui.me.FeedbackActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedBackAdviceTpl extends BaseTpl<FeedbackType> {

	@Bind(R.id.radio)
	RadioButton radio;
	private FeedbackType bean;

	public FeedBackAdviceTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_feed_back1;
	}

	@Override
	public void setBean(FeedbackType bean, int position) {
		this.bean = bean;
		radio.setText(bean.getItem());
	}

	@OnClick(R.id.radio)
	public void clickRadio() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(FeedbackActivity.BUNDLE_KEY_TEXT, bean.getItem());
		bundle.putString(FeedbackActivity.BUNDLE_KEY_VALUE, bean.getId());
		intent.putExtras(bundle);
		_activity.setResult(Activity.RESULT_OK, intent);
		_activity.finish();
	}

}
