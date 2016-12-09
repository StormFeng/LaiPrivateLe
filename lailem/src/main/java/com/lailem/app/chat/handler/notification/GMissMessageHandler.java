package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgGMiss;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupUtils;

public class GMissMessageHandler extends MessageHandler {

    public GMissMessageHandler(Message message, Context context) {
        super(message, context);
        MsgGMiss msgObj = message.getMsgObj();
        dealConversationMessage(message, false, false);
        Group group = GroupCache.getInstance(context).get(msgObj.getgInfo().getId());
        if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {// 解散群
            GroupUtils.exitGroup(context, msgObj.getgInfo().getId());
        } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {// 解散活动
            GroupUtils.exitActivity(context, msgObj.getgInfo().getId());
        }
        remaind();
    }


}
