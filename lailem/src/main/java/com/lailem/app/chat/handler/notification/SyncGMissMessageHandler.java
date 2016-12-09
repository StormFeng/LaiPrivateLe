package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgSyncGMiss;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupUtils;

/**
 * 网页上解散group后同步至app端
 */
public class SyncGMissMessageHandler extends MessageHandler {

    public SyncGMissMessageHandler(Message message, Context context) {
        super(message, context);
        MsgSyncGMiss msgObj = message.getMsgObj();
        if (Constant.gType_group.equals(msgObj.getgType())) {// 解散群
            GroupUtils.exitGroup(context, msgObj.getgId());
        } else if (Constant.gType_activity.equals(msgObj.getgType())) {// 解散活动
            GroupUtils.exitActivity(context, msgObj.getgId());
        }
    }
}
