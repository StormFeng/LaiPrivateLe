package com.lailem.app.chat.model.msg;

/**
 * 网页上解散group后同步至app端
 */
public class MsgSyncGMiss extends Msg {
    private String gId;//	群或活动id	string
    private String gType;//	group类型	string	1（活动）、2（普通群）

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }
}
