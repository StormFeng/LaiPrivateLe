package com.lailem.app.ui.create_old.dynamic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.ui.create_old.dynamic.model.TextModel;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateDynamicTextActivity extends BaseActivity implements TextWatcher {
	@Bind(R.id.topbar)
	TopBarView topbar;
	@Bind(R.id.content)
	EditText content_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_dynamic_text);
		ButterKnife.bind(this);
		initView();
	}

	private void initView() {
		topbar.setTitle("发表").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("完成", new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});
		content_et.addTextChangedListener(this);
		TextModel textModel = (TextModel) _Bundle.getSerializable(CreateDynamicActivity.BUNDLE_KEY_TEXT);
		if (textModel == null) {
			content_et.setText("");
		} else {
			content_et.setText(textModel.getContent());
		}
		content_et.setSelection(content_et.length());
	}

	protected void submit() {
		String content = content_et.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			return;
		}
		TextModel model = new TextModel(content);
		Bundle bundle = new Bundle();
		bundle.putSerializable(CreateDynamicActivity.BUNDLE_KEY_TEXT, model);
		setResult(RESULT_OK, bundle);
		finish();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		topbar.getRight_tv().setEnabled(content_et.length() > 0);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
