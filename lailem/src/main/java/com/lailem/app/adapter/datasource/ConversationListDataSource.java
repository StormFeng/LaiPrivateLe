package com.lailem.app.adapter.datasource;

import android.content.Context;

import com.lailem.app.base.BaseListDataSource;
import com.lailem.app.dao.Conversation;
import com.lailem.app.dao.DaoOperate;
import com.socks.library.KLog;

import java.util.ArrayList;

public class ConversationListDataSource extends BaseListDataSource<Conversation> {


    public ConversationListDataSource(Context context) {
        super(context);
    }

    @Override
    protected ArrayList<Conversation> load(int page) throws Exception {
        ArrayList<Conversation> models = new ArrayList<Conversation>();
        if (ac.isLogin()) {
            models = (ArrayList<Conversation>) DaoOperate.getInstance(context).queryConversations();
        }
        KLog.i("conversations:::" + models);
//		if (models.size() == 0) {
//				Conversation conversation = new Conversation();
//				conversation.setCType(Constant.cType_sc);
//				conversation.setConversationId(UUIDUtils.uuid32());
//				conversation.setUserId(AppContext.getInstance().getLoginUid());
//				conversation.setTId("402881f34f24fc75014f25525c740016");
//				conversation.setNoReadCount(0);
//				conversation.setUpdateTime(System.currentTimeMillis());
//				conversation.setCreateTime(System.currentTimeMillis());
//				DaoFactory.getInstance(_activity).getConversationDao().insert(conversation);
//				models.add(conversation);
//				
//				
//				Conversation conversation1 = new Conversation();
//				conversation1.setCType(Constant.cType_p);
//				conversation1.setConversationId(UUIDUtils.uuid32());
//				conversation1.setUserId(AppContext.getInstance().getLoginUid());
//				conversation1.setTId(JniSharedLibraryWrapper.notificationPublisherId());
//				conversation1.setNoReadCount(0);
//				conversation1.setUpdateTime(System.currentTimeMillis());
//				conversation1.setCreateTime(System.currentTimeMillis());
//				DaoFactory.getInstance(_activity).getConversationDao().insert(conversation1);
//				User user = new User();
//				user.setUserId(JniSharedLibraryWrapper.notificationPublisherId());
//				user.setNickname("通知");
//				user.setHead("http://192.168.1.194:8080/tomcat.png");
//				UserCache.getInstance(_activity).put(user);
//				models.add(conversation1);
//		}
        hasMore = false;
        return models;
    }

}
