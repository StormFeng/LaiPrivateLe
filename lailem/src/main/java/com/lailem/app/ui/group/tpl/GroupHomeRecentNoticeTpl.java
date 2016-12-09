package com.lailem.app.ui.group.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.jsonbean.activegroup.RecentNotice;
import com.lailem.app.tpl.BaseTpl;

import butterknife.Bind;

public class GroupHomeRecentNoticeTpl extends BaseTpl<RecentNotice> {
	@Bind(R.id.date)
	TextView date_tv;
	@Bind(R.id.content)
	TextView content_tv;

	public GroupHomeRecentNoticeTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_group_home_recent_notice;
	}

	@Override
	public void setBean(RecentNotice bean, int position) {
		date_tv.setText(bean.getCreateTime());
		content_tv.setText("主题：" + bean.getTopic() + "\n\n" + bean.getDetail());
	}
}
