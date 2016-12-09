package com.lailem.app.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;

import butterknife.Bind;

public class GroupMemberSectionTpl extends BaseTpl<ObjectWrapper> {
	@Bind(R.id.name)
	TextView name_tv;

	public GroupMemberSectionTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_group_member_section;
	}

	@Override
	public void setBean(ObjectWrapper bean, int position) {
		name_tv.setText(bean.getObject().toString());
	}

}
