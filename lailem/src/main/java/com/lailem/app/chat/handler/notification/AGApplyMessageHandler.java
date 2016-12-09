package com.lailem.app.chat.handler.notification;

import android.content.Context;

import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.handler.MessageHandler;
import com.lailem.app.chat.model.msg.MsgAGApply;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.MGroup;
import com.lailem.app.dao.Message;
import com.lailem.app.utils.GroupUtils;

public class AGApplyMessageHandler extends MessageHandler {

    public AGApplyMessageHandler(Message message, Context context) {
        super(message, context);
        MsgAGApply msgObj = message.getMsgObj();

        Group group = DaoOperate.getInstance(context).queryGroup(msgObj.getgInfo().getId());
        if (group == null) {
            group = new Group();
            group.setCreateTime(System.currentTimeMillis());
            group.setUpdateTime(System.currentTimeMillis());
            group.setGroupId(msgObj.getgInfo().getId());
            group.setName(msgObj.getgInfo().getgName());
            group.setIntro(msgObj.getgInfo().getgIntro());
            group.setSquareSPic(msgObj.getgInfo().getgSSPic());
            group.setGroupType(msgObj.getgInfo().getgType());
            group.setPermission(Integer.parseInt(msgObj.getgInfo().getPerm()));
            DaoOperate.getInstance(context).insert(group);
        } else {
            group.setUpdateTime(System.currentTimeMillis());
            group.setName(msgObj.getgInfo().getgName());
            group.setIntro(msgObj.getgInfo().getgIntro());
            group.setSquareSPic(msgObj.getgInfo().getgSSPic());
            group.setPermission(Integer.parseInt(msgObj.getgInfo().getPerm()));
            DaoOperate.getInstance(context).update(group);
        }
        // 放入缓存
        GroupCache.getInstance(context).put(group);
        dealConversationMessage(message, false, false);

        MGroup mGroup = DaoOperate.getInstance(context).queryMGroup(msgObj.getgInfo().getId());
        if (mGroup == null) {
            if (Constant.gType_group.equals(msgObj.getgInfo().getgType())) {// 加入群
                GroupUtils.joinGroup(context, group.getGroupId(), group.getName(), group.getIntro(), group.getSquareSPic(), String.valueOf(group.getPermission()));
            } else if (Constant.gType_activity.equals(msgObj.getgInfo().getgType())) {// 加入活动
                GroupUtils.joinActivity(context, group.getGroupId(), group.getName(), group.getIntro(), group.getSquareSPic(), String.valueOf(group.getPermission()));
            }
        }
        remaind();

    }

}
