package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.base.BaseMultiTypeListAdapter;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.dynamic.PicMaterialTypeBean;
import com.lailem.app.jsonbean.dynamic.PicMaterialTypeBean.PicMaterialType;
import com.lailem.app.ui.create_old.MaterialListActivity;

import java.util.ArrayList;

public class MaterialListDataSource extends BaseListDataSource<Object> {

	public MaterialListDataSource(Context context) {
		super(context);
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		ArrayList<Object> models = new ArrayList<Object>();
		PicMaterialTypeBean list = (PicMaterialTypeBean) ApiClient.getApi().picMaterialType();
		
		if (list.isNotOK()) {
			ac.handleErrorCode(_activity, list.errorCode, list.errorInfo);
			return models;
		}

		ObjectWrapper cateSec = new ObjectWrapper("分类", BaseMultiTypeListAdapter.TPL_SECTION);
		models.add(cateSec);

		ArrayList<PicMaterialType> types = list.getPicMaterialTypeList();
		for (PicMaterialType picMaterialType : types) {
			picMaterialType.setItemViewType(MaterialListActivity.TPL_MATERIAL);
		}
		models.addAll(types);

		ObjectWrapper specialSec = new ObjectWrapper("专题", BaseMultiTypeListAdapter.TPL_SECTION);
		models.add(specialSec);

		ArrayList<PicMaterialType> specialTypes = list.getPicMaterialSpecialList();
		for (PicMaterialType picMaterialSpecial : specialTypes) {
			picMaterialSpecial.setItemViewType(MaterialListActivity.TPL_MATERIAL);
		}
		models.addAll(specialTypes);

		hasMore = false;
		return models;
	}

}
