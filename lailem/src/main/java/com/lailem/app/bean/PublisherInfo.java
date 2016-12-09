package com.lailem.app.bean;

public class PublisherInfo extends Result {
    private String id;
    private String headSPicName;
    private String nickname;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadSPicName() {
        return headSPicName;
    }

    public void setHeadSPicName(String headSPicName) {
        this.headSPicName = headSPicName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
