package com.lailem.app.ui.create_old;

import android.content.Intent;
import android.os.Bundle;

import com.lailem.app.R;
import com.lailem.app.adapter.datasource.MaterialListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.base.BaseSectionListActivity;
import com.lailem.app.tpl.MaterialSectionTpl;
import com.lailem.app.tpl.MaterialTpl;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.lailem.app.widget.pulltorefresh.helper.IDataSource;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MaterialListActivity extends BaseSectionListActivity<Object> {

	public static final int REQUEST_CODE_MATERIAL_TYPE = 1000;

	public static final int TPL_MATERIAL = 1;

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
		topbar.setTitle("素材中心").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
	}

	@Override
	protected IDataSource<Object> getDataSource() {
		return new MaterialListDataSource(this);
	}

	@Override
	protected ArrayList<Class> getTemplateClasses() {
		ArrayList<Class> tpls = new ArrayList<Class>();
		tpls.add(BaseMultiTypeListAdapter.TPL_SECTION, MaterialSectionTpl.class);
		tpls.add(TPL_MATERIAL, MaterialTpl.class);
		return tpls;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			finish();
		}
	}

}
