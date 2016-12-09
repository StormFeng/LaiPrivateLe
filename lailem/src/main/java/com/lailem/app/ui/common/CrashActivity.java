package com.lailem.app.ui.common;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CrashActivity extends BaseActivity {

	@Bind(R.id.topbar)
	TopBarView topbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crash);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		topbar.setTitle("活动聚会神器-来了");
	}

	@OnClick(R.id.restart)
	public void restart() {
		UIHelper.showAppStart(this, true);
		finish();
	}
}
