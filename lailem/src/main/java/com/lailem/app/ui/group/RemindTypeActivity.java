package com.lailem.app.ui.group;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemindTypeActivity extends BaseActivity {
	@Bind(R.id.topbar)
	TopBarView topbar;
	@Bind(R.id.remindRg)
	RadioGroup remind_rg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remind_type);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		topbar.setTitle("通知提醒").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
	}

	@OnClick({ R.id.remindAll, R.id.remindImportant, R.id.remindNormal, R.id.remindNever })
	public void clickRemind(View v) {
		switch (v.getId()) {
		case R.id.remindAll:
			break;
		case R.id.remindImportant:
			break;
		case R.id.remindNormal:
			break;
		case R.id.remindNever:
			break;
		}
	}

}
