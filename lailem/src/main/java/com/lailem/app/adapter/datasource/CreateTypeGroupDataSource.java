package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.jsonbean.dynamic.CommonConfigBean.GroupType;
import com.lailem.app.utils.Const;

import java.util.ArrayList;

public class CreateTypeGroupDataSource extends BaseListDataSource<Object> {

	public CreateTypeGroupDataSource(Context context) {
		super(context);
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		ArrayList<Object> models = new ArrayList<Object>();
		ArrayList<GroupType> list = (ArrayList<GroupType>) ac.readObject(Const.GROUP_TYPE_LIST);
		models.addAll(list);
		hasMore = false;
		return models;
	}
}
