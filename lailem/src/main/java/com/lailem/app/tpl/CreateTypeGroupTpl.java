package com.lailem.app.tpl;

import android.content.Context;
import android.widget.CheckedTextView;

import com.lailem.app.R;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.GroupType;

import butterknife.Bind;

public class CreateTypeGroupTpl extends BaseTpl<GroupType> {
	@Bind(R.id.name)
	CheckedTextView name_ctv;

	private int position;

	public CreateTypeGroupTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_create_type_group;
	}

	@Override
	public void setBean(GroupType bean, int position) {
		this.position = position;

		name_ctv.setChecked(adapter.getCheckedPosition() == position);
		name_ctv.setText(bean.getName());
	}

	protected void onItemClick() {
		super.onItemClick();
		adapter.setCheckedPosition(this.position);
		adapter.notifyDataSetChanged();
	}

}
