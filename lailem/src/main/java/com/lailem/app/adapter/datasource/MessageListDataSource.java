package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.socks.library.KLog;

import java.util.ArrayList;

public class MessageListDataSource extends BaseListDataSource<Message> {
    String conversationId;
    int countPerPage = 10;

    public MessageListDataSource(Context context, String conversationId) {
        super(context);
        this.conversationId = conversationId;
    }

    @Override
    protected ArrayList<Message> load(int page) throws Exception {
        long id = Integer.MAX_VALUE;
        if (data.size() > 0) {
            id = data.get(0).getId();
        }
        ArrayList<Message> models = (ArrayList<Message>) DaoOperate.getInstance(context).queryMessagesForChat(conversationId, id, countPerPage);

        if (models == null || models.size() == 0) {
            for (int i = 0; i < models.size(); i++) {

            }
        }

        KLog.i("messages:::" + models);
        if (models != null) {
            this.page++;
            hasMore = false;
        } else {
            hasMore = false;
        }
        return models;
    }

    @Override
    public boolean isPullDownLoadMore() {
        return true;
    }

}
