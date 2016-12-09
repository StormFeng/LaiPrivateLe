package com.lailem.app.chat.model.msg;

/**
 * 网页端创建Group后通知App端缓存
 * Created by XuYang on 15/12/8.
 */
public class MsgSyncCreateG extends Msg {
    private String gId;//	群或活动id	string
    private String gIntro;//	group简介	string
    private String gName;//	group名称	string
    private String gSSPic;//	group方形小图	string
    private String gType;//	group类型	string	1（活动）、2（普通群）
    private String perm;//	权限	string	1（私有群）、2（公开群）

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getgIntro() {
        return gIntro;
    }

    public void setgIntro(String gIntro) {
        this.gIntro = gIntro;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgSSPic() {
        return gSSPic;
    }

    public void setgSSPic(String gSSPic) {
        this.gSSPic = gSSPic;
    }

    public String getgType() {
        return gType;
    }

    public void setgType(String gType) {
        this.gType = gType;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }
}
