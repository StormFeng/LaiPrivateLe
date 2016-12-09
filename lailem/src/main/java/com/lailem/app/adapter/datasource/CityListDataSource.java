package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.dao.Region;
import com.lailem.app.utils.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class CityListDataSource extends BaseListDataSource<Object> {

	private String provinceId;

	public CityListDataSource(Context context, String provinceId) {
		super(context);
		this.provinceId = provinceId;
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		
		ArrayList<Object> models = new ArrayList<Object>();
		ConfigManager configManager = ConfigManager.getConfigManager();
		List<Region> citys = configManager.queryCity(provinceId);
		models.addAll(citys);
		hasMore = false;
		return models;
	}
}