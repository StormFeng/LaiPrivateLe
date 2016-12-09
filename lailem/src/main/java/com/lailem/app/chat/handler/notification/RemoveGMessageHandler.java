package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgRemoveG;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupUtils;

/**
 * 被人踢出群组
 */
public class RemoveGMessageHandler extends MessageHandler {

    public RemoveGMessageHandler(Message message, Context context) {
        super(message, context);
        MsgRemoveG msgObj = message.getMsgObj();
        if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {// 解散群
            GroupUtils.exitGroup(context, msgObj.getgInfo().getId());
        } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {// 解散活动
            GroupUtils.exitActivity(context, msgObj.getgInfo().getId());
        }
        dealConversationMessage(message, false, false);
        remaind();
    }
}
