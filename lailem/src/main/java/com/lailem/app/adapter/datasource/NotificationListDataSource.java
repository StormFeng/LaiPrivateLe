package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.socks.library.KLog;

import java.util.ArrayList;

public class NotificationListDataSource extends BaseListDataSource<Message> {

    String conversationId;
    int countPerPage = 10;

    public NotificationListDataSource(Context context, String conversationId) {
        super(context);
        this.conversationId = conversationId;
    }

    @Override
    protected ArrayList<Message> load(int page) throws Exception {
        long id = 0;
        if (page == FIRST_PAGE_NO) {
            id = Integer.MAX_VALUE;
        } else {
            id = data.get(data.size() - 1).getId();
        }
        ArrayList<Message> models = (ArrayList<Message>) DaoOperate.getInstance(context).queryMessagesForNotification(conversationId, id, countPerPage);
        KLog.i("messages:::" + models);
        if (models != null && models.size() > 0) {
            this.page++;
            if (models.size() < 10) {
                hasMore = false;
            } else {
                hasMore = true;
            }
        } else {
            hasMore = false;
        }
        return models;
    }

}
