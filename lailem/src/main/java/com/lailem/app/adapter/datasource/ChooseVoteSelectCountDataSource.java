package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.Result;

import java.util.ArrayList;

public class ChooseVoteSelectCountDataSource extends BaseListDataSource<Object> {
	private int count;

	public ChooseVoteSelectCountDataSource(Context context, int count) {
		super(context);
		this.count = count;
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		ArrayList<Object> models = new ArrayList<Object>();

		for (int i = 1; i <= count; i++) {
			if (i == 1) {
				models.add(new SelectCountBean("单选", i + ""));
				continue;
			}
			if (i == count) {
				models.add(new SelectCountBean("不限", i + ""));
				continue;
			}
			models.add(new SelectCountBean(i + "个", i + ""));
		}

		hasMore = false;
		return models;
	}

	public static class SelectCountBean extends Result {
		private String name;
		private String value;

		public SelectCountBean(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}