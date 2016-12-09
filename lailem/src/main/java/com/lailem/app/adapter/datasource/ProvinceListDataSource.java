package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.Result;
import com.lailem.app.dao.Region;
import com.lailem.app.ui.me.ChooseCityOneActivity;
import com.lailem.app.utils.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class ProvinceListDataSource extends BaseListDataSource<Object> {

	public ProvinceListDataSource(Context context) {
		super(context);
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		
		ArrayList<Object> models = new ArrayList<Object>();
		models.add(new Result(ChooseCityOneActivity.TPL_HEAD));
		ConfigManager configManager = ConfigManager.getConfigManager();
		// 直辖市
		List<Region> directCity = configManager.queryDirectCity();
		for (Region region : directCity) {
			region.setItemViewType(ChooseCityOneActivity.TPL_PROVINCE);
		}
		// 省
		List<Region> province = configManager.queryProvince();
		for (Region region : province) {
			region.setItemViewType(ChooseCityOneActivity.TPL_PROVINCE);
		}
		models.addAll(directCity);
		models.addAll(province);
		hasMore = false;
		return models;
	}
}