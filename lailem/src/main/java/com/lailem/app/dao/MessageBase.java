package com.lailem.app.dao;


import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.bean.Base;
import com.lailem.app.chat.model.msg.Msg;
import com.lailem.app.chat.model.msg.MsgACom;
import com.lailem.app.chat.model.msg.MsgAGApply;
import com.lailem.app.chat.model.msg.MsgALike;
import com.lailem.app.chat.model.msg.MsgDCom;
import com.lailem.app.chat.model.msg.MsgDLike;
import com.lailem.app.chat.model.msg.MsgExitG;
import com.lailem.app.chat.model.msg.MsgGApply;
import com.lailem.app.chat.model.msg.MsgGMiss;
import com.lailem.app.chat.model.msg.MsgGNotice;
import com.lailem.app.chat.model.msg.MsgGUpdate;
import com.lailem.app.chat.model.msg.MsgJoinG;
import com.lailem.app.chat.model.msg.MsgNGPub;
import com.lailem.app.chat.model.msg.MsgPic;
import com.lailem.app.chat.model.msg.MsgPosition;
import com.lailem.app.chat.model.msg.MsgRemoveG;
import com.lailem.app.chat.model.msg.MsgSyncCreateG;
import com.lailem.app.chat.model.msg.MsgSyncExitG;
import com.lailem.app.chat.model.msg.MsgSyncGMiss;
import com.lailem.app.chat.model.msg.MsgSysP;
import com.lailem.app.chat.model.msg.MsgText;
import com.lailem.app.chat.model.msg.MsgTip;
import com.lailem.app.chat.model.msg.MsgVideo;
import com.lailem.app.chat.model.msg.MsgVoice;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.MessageFactory;
import com.lailem.app.ui.chat.ChatActivity;
import com.lailem.app.ui.chat.NotificationListActivity;
import com.socks.library.KLog;

public class MessageBase extends Base {
    public static Gson gson = new Gson();
    private Msg msgObj;

    public void send() {
        MessageFactory.getFactory().send((Message) this);
    }

    public void reSend() {
        MessageFactory.getFactory().reSend((Message) this);
    }

    public <T extends Msg> T getMsgObj() {
        return (T) msgObj;
    }

    public void setMsgObj(Msg msgObj) {
        this.msgObj = msgObj;
    }


    public void initMsgObj(String msg, String fId, String sType) {
        KLog.i("msg:::" + msg + ":::fId:::" + fId + ":::sType:::" + sType);
        boolean isRight = false;
        if (AppContext.getInstance().getLoginUid().equals(fId)) {
            isRight = true;
        }
        //聊天
        if (Constant.sType_text.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgText.class);
            if (isRight) {
                itemViewType = ChatActivity.TPL_RIGHT_TEXT;
            } else {
                itemViewType = ChatActivity.TPL_LEFT_TEXT;
            }
        } else if (Constant.sType_pic.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgPic.class);
            if (isRight) {
                itemViewType = ChatActivity.TPL_RIGHT_PIC;
            } else {
                itemViewType = ChatActivity.TPL_LEFT_PIC;
            }
        } else if (Constant.sType_voice.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgVoice.class);
            if (isRight) {
                itemViewType = ChatActivity.TPL_RIGHT_VOICE;
            } else {
                itemViewType = ChatActivity.TPL_LEFT_VOICE;
            }
        } else if (Constant.sType_video.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgVideo.class);
            if (isRight) {
                itemViewType = ChatActivity.TPL_RIGHT_VIDEO;
            } else {
                itemViewType = ChatActivity.TPL_LEFT_VIDEO;
            }
        } else if (Constant.sType_position.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgPosition.class);
            if (isRight) {
                itemViewType = ChatActivity.TPL_RIGHT_POSITION;
            } else {
                itemViewType = ChatActivity.TPL_LEFT_POSITION;
            }
        }
        //通知
        else if (Constant.sType_sysP.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgSysP.class);
            itemViewType = NotificationListActivity.TPL_SYSTEM_NOTICE;
        } else if (Constant.sType_gNotice.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgGNotice.class);
            itemViewType = NotificationListActivity.TPL_GROUP_NOTICE;
        } else if (Constant.sType_gApply.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgGApply.class);
            MsgGApply msgGApply = (MsgGApply) msgObj;
            if (Constant.gApplyWay_voice.equals(msgGApply.getWay())) {//1是文本 2是语音 ，默认文本
                itemViewType = NotificationListActivity.TPL_APPLY_JOIN_GROUP_VOICE;
            } else {
                itemViewType = NotificationListActivity.TPL_APPLY_JOIN_GROUP_TEXT;
            }
        } else if (Constant.sType_aGApply.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgAGApply.class);
            itemViewType = NotificationListActivity.TPL_AGREE_GROUP_APPLY;
        } else if (Constant.sType_exitG.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgExitG.class);
            itemViewType = NotificationListActivity.TPL_EXIT_GROUP;
        } else if (Constant.sType_gMiss.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgGMiss.class);
            itemViewType = NotificationListActivity.TPL_GROUP_DISMESS;
        } else if (Constant.sType_joinG.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgJoinG.class);
            itemViewType = NotificationListActivity.TPL_JOIN_GROUP;
        } else if (Constant.sType_sync_createG.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgSyncCreateG.class);
        } else if (Constant.sType_sync_exitG.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgSyncExitG.class);
        } else if (Constant.sType_sync_GMiss.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgSyncGMiss.class);
        } else if (Constant.sType_removeG.equals(sType)) {
            itemViewType = NotificationListActivity.TPL_REMOVE_G;
            msgObj = gson.fromJson(msg, MsgRemoveG.class);
        }
        // 评论与点赞
        else if (Constant.sType_dCom.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgDCom.class);
        } else if (Constant.sType_dLike.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgDLike.class);
        } else if (Constant.sType_aCom.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgACom.class);
        } else if (Constant.sType_aLike.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgALike.class);
        }
        //group新发表提醒
        else if (Constant.sType_nGPub.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgNGPub.class);
        }
        //时间
        else if (Constant.sType_time.equals(sType)) {
            msgObj = new Msg();
            itemViewType = ChatActivity.TPL_TIP;
        } else if (Constant.sType_tip.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgTip.class);
            itemViewType = ChatActivity.TPL_TIP;
        } else if (Constant.sType_gUpdate.equals(sType)) {
            msgObj = gson.fromJson(msg, MsgGUpdate.class);
        }

    }

}
