package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.GInfo;
import com.lailem.app.chat.model.inmsg.HInfo;

/**
 * 踢出群组，被踢者收到的信息
 */
public class MsgRemoveG extends Msg {
    private GInfo gInfo;    //群信息
    private HInfo hInfo;    //踢人操作者信息

    public GInfo getgInfo() {
        return gInfo;
    }

    public void setgInfo(GInfo gInfo) {
        this.gInfo = gInfo;
    }

    public HInfo gethInfo() {
        return hInfo;
    }

    public void sethInfo(HInfo hInfo) {
        this.hInfo = hInfo;
    }
}
