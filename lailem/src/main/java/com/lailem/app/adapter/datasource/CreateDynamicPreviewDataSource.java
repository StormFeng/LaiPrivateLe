package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.bean.Base;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicPreviewActivity;

import java.util.ArrayList;

public class CreateDynamicPreviewDataSource extends BaseListDataSource<Object> {
	private ArrayList<Object> previewModels;

	public CreateDynamicPreviewDataSource(Context context, ArrayList<Object> previewModels) {
		super(context);
		this.previewModels = previewModels;
	}

	@Override
	protected ArrayList<Object> load(int page) throws Exception {
		ArrayList<Object> models = new ArrayList<Object>();
		models.add(new ObjectWrapper("头像", CreateDynamicPreviewActivity.TPL_AVATAR));
		for (int i = 0; i < previewModels.size(); i++) {
			Base base = (Base) previewModels.get(i);
			switch (base.getItemViewType()) {
			case CreateDynamicActivity.TPL_TEXT:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_TEXT);
				break;
			case CreateDynamicActivity.TPL_IMAGE:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_IMAGE);
				break;
			case CreateDynamicActivity.TPL_VOICE:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_VOICE);
				break;
			case CreateDynamicActivity.TPL_VIDEO:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_VIDEO);
				break;
			case CreateDynamicActivity.TPL_VOTE:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_VOTE);
				break;
			case CreateDynamicActivity.TPL_ADDRESS:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_ADDRESS);
				break;
			case CreateDynamicActivity.TPL_SCHEDULE:
				base.setItemViewType(CreateDynamicPreviewActivity.TPL_SCHEDULE);
				break;
			}
			models.add(base);
			if (i < previewModels.size() - 1) {
				models.add(new ObjectWrapper("分割线", CreateDynamicPreviewActivity.TPL_LINE));
			}
		}
		return models;
	}

}
