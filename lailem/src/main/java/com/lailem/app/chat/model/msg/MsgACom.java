package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.TUInfo;
import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgACom extends Msg {
    private String id;
    private String aId;
    private String com;
    private String tCom;
    private TUInfo tUInfo;
    private UInfo uInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String gettCom() {
        return tCom;
    }

    public void settCom(String tCom) {
        this.tCom = tCom;
    }

    public TUInfo gettUInfo() {
        return tUInfo;
    }

    public void settUInfo(TUInfo tUInfo) {
        this.tUInfo = tUInfo;
    }

    public UInfo getuInfo() {
        return uInfo;
    }

    public void setuInfo(UInfo uInfo) {
        this.uInfo = uInfo;
    }

}
