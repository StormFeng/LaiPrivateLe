package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgSyncCreateG;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupBriefUtil;
import com.lailem.app.utils.GroupUtils;

/**
 * 网页端创建Group后通知App端缓存
 */
public class SyncCreateGMessageHandler extends MessageHandler {
    public SyncCreateGMessageHandler(Message message, Context context) {
        super(message, context);
        MsgSyncCreateG msgObj = message.getMsgObj();
        String gType = msgObj.getgType();
        if (Constant.gType_activity.equals(gType)) {//添加活动
            GroupBriefUtil.getGroupBriefFromNetBySync(msgObj.getgId(), Constant.gType_activity, context);
            Group group = GroupCache.getInstance(context).get(msgObj.getgId());
            GroupUtils.joinActivity(context, group.getGroupId(), group.getName(), group.getIntro(), group.getSquareSPic(), group.getPermission() + "", group.getTypeId());
        } else if (Constant.gType_group.equals(gType)) {// 添加群
            GroupBriefUtil.getGroupBriefFromNetBySync(msgObj.getgId(), Constant.gType_group, context);
            Group group = GroupCache.getInstance(context).get(msgObj.getgId());
            GroupUtils.joinGroup(context, group.getGroupId(), group.getName(), group.getIntro(), group.getSquareSPic(), String.valueOf(group.getPermission()));
        }
    }
}
