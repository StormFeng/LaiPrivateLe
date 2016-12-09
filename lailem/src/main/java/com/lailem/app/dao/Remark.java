package com.lailem.app.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "REMARK".
 */
public class Remark {

    private Long id;
    private Long createTime;
    private String userId;
    private String toUserId;
    private String remark;
    private Long updateTime;

    public Remark() {
    }

    public Remark(Long id) {
        this.id = id;
    }

    public Remark(Long id, Long createTime, String userId, String toUserId, String remark, Long updateTime) {
        this.id = id;
        this.createTime = createTime;
        this.userId = userId;
        this.toUserId = toUserId;
        this.remark = remark;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

}