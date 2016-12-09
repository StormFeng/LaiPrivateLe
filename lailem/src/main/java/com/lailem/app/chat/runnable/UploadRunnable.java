package com.lailem.app.chat.runnable;

import android.content.Context;
import android.text.TextUtils;

import com.lailem.app.AppContext;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.Result;
import com.lailem.app.chat.model.msg.MsgPic;
import com.lailem.app.chat.model.msg.MsgVideo;
import com.lailem.app.chat.model.msg.MsgVoice;
import com.lailem.app.chat.util.ChatListenerManager;
import com.lailem.app.chat.util.ChatUtil;
import com.lailem.app.chat.util.Constant;
import com.lailem.app.chat.util.PoolFactory;
import com.lailem.app.dao.DaoOperate;
import com.lailem.app.dao.Message;
import com.lailem.app.jsonbean.chat.UploadChatFileInfo;
import com.lailem.app.utils.Const;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;

public class UploadRunnable implements Runnable {

    Message message;
    Context context;

    public UploadRunnable(Message message, Context context) {
        this.message = message;
        this.context = context;
    }

    @Override
    public void run() {
        String sType = message.getSType();
        String fileType = null;
        ArrayList<File> files = new ArrayList<File>();
        String duration = null;
        if (Constant.sType_pic.equals(sType)) {
            fileType = "1";
            MsgPic msgPic = message.getMsgObj();
            File file = new File(msgPic.getLocalPath());
            files.add(file);
        } else if (Constant.sType_voice.equals(sType)) {
            fileType = "2";
            MsgVoice msgVoice = message.getMsgObj();
            File file = new File(msgVoice.getLocalPath());
            duration = msgVoice.getD();
            files.add(file);
        } else if (Constant.sType_video.equals(sType)) {
            fileType = "3";
            MsgVideo msgVideo = message.getMsgObj();
            File file = new File(msgVideo.getLocalPath());
            duration = msgVideo.getD();
            files.add(file);
            File file1 = new File(msgVideo.getLocalPicPath());
            files.add(file1);
        }

        try {
            Result res = ApiClient.getApi().uploadeChatFile(files, fileType, duration, AppContext.getInstance().getLoginUid());
            resultDeal(res, message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resultDeal(Result res, Message message) {

        if (Const.value_success.equals(res.ret)) {
            KLog.i("上传成功");
            String sType = message.getSType();
            UploadChatFileInfo uploadFileInfo = (UploadChatFileInfo) res;
            KLog.i("uploadFileInfo:::" + uploadFileInfo);
            if (uploadFileInfo.getFileInfo() == null) {
                KLog.i("上传失败");
                message.setStatus(Constant.status_uploadFail);
                DaoOperate.getInstance(context).update(message);
                ChatListenerManager.getInstance().noticeSendListenerForSendFail(message);
                return;
            }
            String msg = null;
            if (Constant.sType_pic.equals(sType)) {
                MsgPic msgPic = message.getMsgObj();
                msgPic.setPic(uploadFileInfo.getFileInfo().getFileName());
                if (!TextUtils.isEmpty(uploadFileInfo.getFileInfo().getgThumbnail())) {
                    msgPic.settPic(uploadFileInfo.getFileInfo().getgThumbnail());
                } else {
                    msgPic.settPic(uploadFileInfo.getFileInfo().getThumbnail());
                }
                msg = ChatUtil.gson.toJson(msgPic);
            } else if (Constant.sType_voice.equals(sType)) {
                MsgVoice msgVoice = message.getMsgObj();
                msgVoice.setVoice(uploadFileInfo.getFileInfo().getFileName());
                msg = ChatUtil.gson.toJson(msgVoice);
            } else if (Constant.sType_video.equals(sType)) {
                MsgVideo msgVideo = message.getMsgObj();
                msgVideo.setVideo(uploadFileInfo.getFileInfo().getFileName());
                msgVideo.setPic(uploadFileInfo.getFileInfo().getThumbnail());
                msg = ChatUtil.gson.toJson(msgVideo);
            }
            message.setMsg(msg);
            message.initMsgObj(msg, message.getFId(), sType);
            message.setStatus(Constant.status_sending);
            DaoOperate.getInstance(context).update(message);
            PoolFactory.getFactory().getPoolForSend().submit(new SendRunnable(message, context));
        } else {
            KLog.i("上传失败");
            message.setStatus(Constant.status_uploadFail);
            DaoOperate.getInstance(context).update(message);
            ChatListenerManager.getInstance().noticeSendListenerForSendFail(message);
        }

    }
}
