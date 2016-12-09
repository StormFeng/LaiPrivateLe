package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.socks.library.KLog;

import java.util.ArrayList;

public class MeMessageListDataSource extends BaseListDataSource<Message> {

	public MeMessageListDataSource(Context context) {
		super(context);
	}

	@Override
	protected ArrayList<Message> load(int page) throws Exception {
		long id = 0;
		if (page == FIRST_PAGE_NO) {
			id = Integer.MAX_VALUE;
		} else {
			id = data.get(data.size() - 1).getId();
		}
		ArrayList<Message> models = (ArrayList<Message>) DaoOperate.getInstance(_activity).queryMessagesForMeMessageList(id, 20);
        KLog.i("models:::" + models);
		if (models != null && models.size() == 20) {
			hasMore = true;
			this.page++;
		} else {
			hasMore = false;
		}
		return models;
	}

}
