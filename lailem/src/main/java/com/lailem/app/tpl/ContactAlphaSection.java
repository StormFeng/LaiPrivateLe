package com.lailem.app.tpl;

import android.content.Context;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;

import butterknife.Bind;

public class ContactAlphaSection extends BaseTpl<ObjectWrapper> {
	@Bind(R.id.alpha)
	TextView alpha_tv;

	public ContactAlphaSection(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_contact_alpha_section;
	}

	@Override
	public void setBean(ObjectWrapper bean, int position) {
		alpha_tv.setText(bean.getObject().toString());
	}

}
