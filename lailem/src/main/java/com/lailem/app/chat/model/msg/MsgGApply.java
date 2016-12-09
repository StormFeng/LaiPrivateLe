package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.GInfo;
import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgGApply extends Msg {
    private String con;
    private String way;
    private String id;
    private String dur;
    private GInfo gInfo;
    private UInfo uInfo;

    public String getDur() {
        return dur;
    }

    public void setDur(String dur) {
        this.dur = dur;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GInfo getgInfo() {
        return gInfo;
    }

    public void setgInfo(GInfo gInfo) {
        this.gInfo = gInfo;
    }

    public UInfo getuInfo() {
        return uInfo;
    }

    public void setuInfo(UInfo uInfo) {
        this.uInfo = uInfo;
    }

}
