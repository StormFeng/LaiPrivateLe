package com.lailem.app.chat.handler.other;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgNGPub;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.MessageCountManager;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.MGroup;
import com.lailem.app.dao.Message;

public class NGPubMessageHandler extends MessageHandler {

    public NGPubMessageHandler(Message message, Context context) {
        super(message, context);
        // 群或活动新发表提醒消息，由于需要刷新才看得到消息，所以在此无需判断是否需要增加未读消息数量
        MsgNGPub msgNGPub = (MsgNGPub) message.getMsgObj();
        MGroup mGroup = DaoOperate.getInstance(context).queryMGroup(msgNGPub.getgId());
        if (mGroup != null) {
            mGroup.setUpdateTime(message.getSTime());
            mGroup.setNewPublishCount(mGroup.getNewPublishCount() + 1);
            DaoOperate.getInstance(context).update(mGroup);
            ChatListenerManager.getInstance().noticeGroupListenerForNGPubMessageIn(mGroup);
            MessageCountManager.getInstance().addOne(MessageCountManager.KEY_NO_READ_COUNT_FOR_NGPUB);
            remaind();
        }
    }

}
