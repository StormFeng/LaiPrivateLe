package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.MGroup;

import java.util.ArrayList;

public class DynamicGroupListDataSource extends BaseListDataSource<MGroup> {

	public DynamicGroupListDataSource(Context context) {
		super(context);
	}

	@Override
	protected ArrayList<MGroup> load(int page) throws Exception {
		
		hasMore = false;
		if(ac.isLogin()) {
			return (ArrayList<MGroup>) DaoOperate.getInstance(_activity).queryMGroups(Constant.gType_group);
		}else{
			return new ArrayList<MGroup>();
		}
	}

}
