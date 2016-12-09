package com.lailem.app.ui.group;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.cache.RemarkCache;
import com.lailem.app.dao.Remark;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyMemberRemarkActivity extends BaseActivity implements TextWatcher {
	public static final String BUNDLE_KEY_NAME = "name";
	public static final String BUNDLE_KEY_ID = "id";
	public static final int REQUEST_MODIFY_NAME = 4000;
	@Bind(R.id.topbar)
	TopBarView topbar;
	@Bind(R.id.right_tv)
	TextView submit_tv;
	@Bind(R.id.input)
	EditText input_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_group_name);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		topbar.setTitle("设置备注名").setLeftText("取消", UIHelper.finish(this)).setRightText("保存");
		if (_Bundle != null) {
			input_et.setText(_Bundle.getString(BUNDLE_KEY_NAME));
			input_et.setTag(_Bundle.getString(BUNDLE_KEY_NAME));
			input_et.setSelection(input_et.length());
		}
		submit_tv.setEnabled(false);
		input_et.addTextChangedListener(this);
	}

	@OnClick(R.id.right_tv)
	public void submit() {
		ApiClient.getApi().setRemark(this, ac.getLoginUid(), input_et.getText().toString().trim(), _Bundle.getString(BUNDLE_KEY_ID));
	}

	@Override
	public void onApiStart(String tag) {
		super.onApiStart(tag);
		showWaitDialog();
	}

	@Override
	public void onApiSuccess(Result res, String tag) {
		super.onApiSuccess(res, tag);
		hideWaitDialog();
		if (res.isOK()) {
			Remark remark = new Remark();
			remark.setCreateTime(System.currentTimeMillis());
			remark.setRemark(input_et.getText().toString().trim());
			remark.setToUserId(_Bundle.getString(BUNDLE_KEY_ID));
			remark.setUserId(ac.getLoginUid());
			RemarkCache.getInstance(this).put(remark);
			Bundle bundle = new Bundle();
			bundle.putString(BUNDLE_KEY_NAME, input_et.getText().toString().trim());
			setResult(RESULT_OK, bundle);
			finish();
		} else {
			ac.handleErrorCode(this, res.errorCode, res.errorInfo);
		}
	}

	@Override
	protected void onApiError(String tag) {
		super.onApiError(tag);
		hideWaitDialog();
	}

	@OnClick(R.id.clear)
	public void clear() {
		input_et.setText("");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		submit_tv.setEnabled(isCanSave());
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private boolean isCanSave() {
		return input_et.length() > 0 && !input_et.getText().toString().equals(input_et.getTag().toString());
	}
}
