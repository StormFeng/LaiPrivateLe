package com.lailem.app.chat.util;

import com.google.gson.Gson;
import com.lailem.app.AppContext;
import com.lailem.app.cache.GroupCache;
import com.lailem.app.chat.model.msg.MsgACom;
import com.lailem.app.chat.model.msg.MsgAGApply;
import com.lailem.app.chat.model.msg.MsgALike;
import com.lailem.app.chat.model.msg.MsgDCom;
import com.lailem.app.chat.model.msg.MsgDLike;
import com.lailem.app.chat.model.msg.MsgExitG;
import com.lailem.app.chat.model.msg.MsgGApply;
import com.lailem.app.chat.model.msg.MsgGMiss;
import com.lailem.app.chat.model.msg.MsgGNotice;
import com.lailem.app.chat.model.msg.MsgJoinG;
import com.lailem.app.chat.model.msg.MsgPosition;
import com.lailem.app.chat.model.msg.MsgRemoveG;
import com.lailem.app.chat.model.msg.MsgSysP;
import com.lailem.app.chat.model.msg.MsgText;
import com.lailem.app.chat.model.msg.MsgTip;
import com.lailem.app.dao.Group;
import com.lailem.app.dao.Message;
import com.socks.library.KLog;

public class ChatUtil {
    public static Gson gson = new Gson();

    /**
     * 【注意】message必须设置conversationId后调用才能通知到聊天窗口，
     * 因为message有conversationId表示该消息为聊天消息， 否则为其它消息
     *
     * @param message 不能为空
     */

    public static String getTipMsg(Message message) {
        String sType = message.getSType();
        String tipMsg = null;
        if (Constant.sType_text.equals(sType)) {
            MsgText msgText = (MsgText) message.getMsgObj();
            KLog.i("msgText:::" + msgText);
            tipMsg = msgText.getText();

        } else if (Constant.sType_position.equals(sType)) {

            MsgPosition msgPosition = (MsgPosition) message.getMsgObj();
            tipMsg = "地址：" + msgPosition.getAddr();

        } else if (Constant.sType_sysP.equals(sType)) {
            MsgSysP msgSysP = (MsgSysP) message.getMsgObj();
            tipMsg = "系统消息：" + msgSysP.getTitle();

        } else if (Constant.sType_gNotice.equals(sType)) {

            MsgGNotice msgGNotice = (MsgGNotice) message.getMsgObj();
            String gType = msgGNotice.getgInfo().getgType();
            if (Constant.gType_activity.equals(gType)) {
                tipMsg = "活动通知：" + msgGNotice.getTopic();
            } else if (Constant.gType_group.equals(gType)) {
                tipMsg = "群通知：" + msgGNotice.getTopic();
            }

        } else if (Constant.sType_gApply.equals(sType)) {
            MsgGApply msgGApply = (MsgGApply) message.getMsgObj();
            String gType = msgGApply.getgInfo().getgType();
            String applyWay = msgGApply.getWay();
            if ("1".equals(gType)) {
                if ("1".equals(applyWay)) {// 文本
                    tipMsg = "活动申请：" + msgGApply.getCon();
                } else if ("2".equals(applyWay)) {// 语音
                    tipMsg = "活动申请：[语音]";
                }

            } else if ("2".equals(gType)) {
                if ("1".equals(applyWay)) {// 文本
                    tipMsg = "群申请：" + msgGApply.getCon();
                } else if ("2".equals(applyWay)) {// 语音
                    tipMsg = "群申请：[语音]";
                }

            }

        } else if (Constant.sType_aGApply.equals(sType)) {

            MsgAGApply msgAGApply = (MsgAGApply) message.getMsgObj();
            String gType = msgAGApply.getgInfo().getgType();
            if ("1".equals(gType)) {
                tipMsg = msgAGApply.getvInfo().getNick() + "同意了你的活动申请";
            } else if ("2".equals(gType)) {
                tipMsg = msgAGApply.getvInfo().getNick() + "同意了你的群申请";
            }

        } else if (Constant.sType_pic.equals(sType)) {
            tipMsg = "[图片]";
        } else if (Constant.sType_voice.equals(sType)) {
            tipMsg = "[声音]";
        } else if (Constant.sType_video.equals(sType)) {
            tipMsg = "[视频]";
        } else if (Constant.sType_dCom.equals(sType)) {// 动态评论
            MsgDCom msgObj = message.getMsgObj();
            tipMsg = "评论：" + msgObj.getCom();
        } else if (Constant.sType_dLike.equals(sType)) {// 动态点赞
            MsgDLike msgObj = message.getMsgObj();
            tipMsg = msgObj.getuInfo().getNick() + "给了一个赞！";
        } else if (Constant.sType_aCom.equals(sType)) {// 活动评论
            MsgACom msgObj = message.getMsgObj();
            tipMsg = "评论：" + msgObj.getCom();
        } else if (Constant.sType_aLike.equals(sType)) {// 活动点赞
            MsgALike msgObj = message.getMsgObj();
            tipMsg = msgObj.getuInfo().getNick() + "给了一个赞！";
        } else if (Constant.sType_tip.equals(sType)) {// 提示消息
            MsgTip msgObj = message.getMsgObj();
            tipMsg = msgObj.getTip();
        } else if (Constant.sType_joinG.equals(sType)) {// 新人加入了group提醒
            MsgJoinG msgObj = message.getMsgObj();
            Group group = GroupCache.getInstance(
                    AppContext.getInstance().getApplicationContext()).get(
                    msgObj.getgInfo().getId());
            String gName = "";
            if (group != null) {
                gName = group.getName();
            }
            tipMsg = msgObj.getuInfo().getNick() + "加入了" + gName;
        } else if (Constant.sType_gMiss.equals(sType)) {// 解散了group
            MsgGMiss msgObj = message.getMsgObj();
            String gType = msgObj.getgInfo().getgType();
            Group group = GroupCache.getInstance(
                    AppContext.getInstance().getApplicationContext()).get(
                    msgObj.getgInfo().getId());
            String gName = "";
            if (group != null) {
                gName = group.getName();
            }
            if (Constant.gType_activity.equals(gType)) {// 活动
                tipMsg = msgObj.getcInfo().getNick() + "删除了活动：" + gName;
            } else if (Constant.gType_activity.equals(gType)) {// 群
                tipMsg = msgObj.getcInfo().getNick() + "解散了群：" + gName;
            }
        } else if (Constant.sType_exitG.equals(sType)) {// 退出了Group
            MsgExitG msgObj = message.getMsgObj();
            String gType = msgObj.getgInfo().getgType();
            Group group = GroupCache.getInstance(
                    AppContext.getInstance().getApplicationContext()).get(
                    msgObj.getgInfo().getId());
            String gName = "";
            if (group != null) {
                gName = group.getName();
            }
            if (Constant.gType_activity.equals(gType)) {// 活动
                tipMsg = msgObj.getuInfo().getNick() + "退出了活动：" + gName;
            } else if (Constant.gType_activity.equals(gType)) {// 群
                tipMsg = msgObj.getuInfo().getNick() + "退出了群：" + gName;
            }
        } else if (Constant.sType_removeG.equals(sType)) {
            MsgRemoveG msgObj = message.getMsgObj();
            Group group = GroupCache.getInstance(
                    AppContext.getInstance().getApplicationContext()).get(
                    msgObj.getgInfo().getId());
            String gName = "";
            if (group != null) {
                gName = group.getName();
            }
            tipMsg = "您已被" + msgObj.gethInfo().getNick() + "移除了" + gName;
        } /**else if (Constant.sType_nGPub.equals(sType)) {// 群或活动新发表提醒

         } else if (Constant.sType_gUpdate.equals(sType)) {// 群资料更新

         }*/
        return tipMsg;
    }

}
