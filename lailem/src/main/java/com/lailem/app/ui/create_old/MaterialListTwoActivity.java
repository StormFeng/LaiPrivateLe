package com.lailem.app.ui.create_old;

import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MaterialListTwoDataSource;
import com.lailem.app.base.BaseGridListActivity;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.jsonbean.dynamic.PicMaterialBean;
import com.lailem.app.tpl.MaterialTwoTpl;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataAdapter;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MaterialListTwoActivity extends BaseGridListActivity<Object> {
	public static final int REQUEST_CODE_MATERIAL_PIC = 1000;

	public static final String BUNDLE_KEY_TYPE_ID = "type_id";
	public static final String BUNDLE_KEY_CHECKED_PIC_ID = "checked_pic_id";
	public static final String BUNDLE_KEY_CHECKED_PIC_SNAME = "checked_pic_small_name";
	public static final String BUNDLE_KEY_CHECKED_PIC_BNAME = "checked_pic_big_name";
	public static final String BUNDLE_KEY_CHECKED_PIC_SQUARE_SNAME = "checked_pic_square_small_name";

	@Bind(R.id.topbar)
	TopBarView topbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
		initView();
		listViewHelper.refresh();
	}

	private void initView() {
		topbar.setTitle("简约").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
		gridView.setPadding((int) TDevice.dpToPixel(15.4f), 0, (int) TDevice.dpToPixel(15.4f), 0);
		gridView.setNumColumns(2);
		gridView.setHorizontalSpacing((int) TDevice.dpToPixel(15.4f));
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		return new MaterialListTwoDataSource(this, _Bundle.getString(BUNDLE_KEY_TYPE_ID));
	}

	@Override
	protected Class getTemplateClass() {
		return MaterialTwoTpl.class;
	}

	@Override
	public void onEndRefresh(IDataAdapter<ArrayList<Object>> adapter, ArrayList<Object> result) {
		super.onEndRefresh(adapter, result);
		for (int i = 0; i < resultList.size(); i++) {
			PicMaterialBean pic = (PicMaterialBean) resultList.get(i);
			if (pic.getId().equals(_Bundle.getString(BUNDLE_KEY_CHECKED_PIC_ID))) {
				BaseListAdapter baseListAdapter = (BaseListAdapter) adapter;
				baseListAdapter.setCheckedPosition(i);
				break;
			}
		}
	}
}
