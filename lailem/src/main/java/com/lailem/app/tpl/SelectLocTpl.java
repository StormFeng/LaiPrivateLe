package com.lailem.app.tpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.lailem.app.R;
import com.lailem.app.bean.AddressInfo;
import com.lailem.app.bean.SelectLoc;
import com.lailem.app.ui.group.SelectLocActivity;

import butterknife.Bind;

public class SelectLocTpl extends BaseTpl<SelectLoc> {
	@Bind(R.id.name)
	TextView name_tv;

	private SelectLoc bean;

	public SelectLocTpl(Context context) {
		super(context);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.item_content_sel_loc;
	}

	@Override
	public void setBean(SelectLoc bean, int position) {
		this.bean = bean;
		PoiInfo p = bean.getPoiInfo();
		name_tv.setText(p.address + p.name);
	}

	@Override
	protected void onItemClick() {
		super.onItemClick();
		Intent data = new Intent();
		Bundle bundle = new Bundle();
        AddressInfo addressInfo = new AddressInfo(
                bean.getPoiInfo().location.latitude + "",
                bean.getPoiInfo().location.longitude + "",
                bean.getPoiInfo().address+bean.getPoiInfo().name);
        bundle.putSerializable(SelectLocActivity.BUNDLE_KEY_ADDRESSINFO,addressInfo);
		data.putExtras(bundle);
		_activity.setResult(Activity.RESULT_OK, data);
		_activity.finish();
	}

}
