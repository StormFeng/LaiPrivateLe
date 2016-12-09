package com.lailem.app.tpl;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.ui.active.CreateVoteActiveActivity.CreateVoteActiveItem;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建投票活动投票选项条目
 * 
 * @author XuYang
 *
 */
public class CreateVoteActiveItemTpl extends BaseTpl<CreateVoteActiveItem> implements TextWatcher {

	@Bind(R.id.name)
	EditText name_et;
	@Bind(R.id.label)
	TextView label_tv;

	private CreateVoteActiveItem bean;

	private int position;

	public CreateVoteActiveItemTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_vote_item;
	}

	@Override
	protected void initView() {
		super.initView();
		name_et.addTextChangedListener(this);
	}

	@Override
	public void setBean(CreateVoteActiveItem bean, int position) {
		this.bean = bean;
		this.position = position;
		name_et.setText(bean.getName());
		label_tv.setText((position + 1) + ".");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		bean.setName(name_et.getText().toString().trim());

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@OnClick(R.id.name)
	public void clickName() {
		name_et.setSelection(name_et.length());
		name_et.requestFocus();
		name_et.setCursorVisible(true);
	}

}
