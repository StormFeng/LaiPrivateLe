package com.lailem.app.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.jsonbean.personal.FeedbackTypeBean.FeedbackType;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;

public class FeedBackBugTpl extends BaseTpl<FeedbackType> {

	@Bind(R.id.title)
	TextView title_tv;
	@Bind(R.id.content)
	TextView content_tv;

	private FeedbackType bean;

	public FeedBackBugTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_feed_back2;
	}

	@Override
	public void setBean(FeedbackType bean, int position) {
		this.bean = bean;
		title_tv.setText(bean.getItem());
		content_tv.setText(bean.getItemTip());
	}

	@Override
	protected void onItemClick() {
		super.onItemClick();
		UIHelper.showFeedbackTypeTwo(_activity, bean.getId());
	}
}
