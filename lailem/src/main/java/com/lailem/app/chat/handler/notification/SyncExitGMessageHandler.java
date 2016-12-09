package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgSyncExitG;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupUtils;

/**
 * 网页上退出group后同步至app端
 */
public class SyncExitGMessageHandler extends MessageHandler {

    public SyncExitGMessageHandler(Message message, Context context) {
        super(message, context);
        MsgSyncExitG msgObj = message.getMsgObj();
        String gType = msgObj.getgType();
        if (Constant.gType_group.equals(gType)) {//退出群
            GroupUtils.exitGroup(context, msgObj.getgId());
        } else if (Constant.gType_activity.equals(gType)) {// 退出活动
            GroupUtils.exitActivity(context, msgObj.getgId());
        }
    }
}
