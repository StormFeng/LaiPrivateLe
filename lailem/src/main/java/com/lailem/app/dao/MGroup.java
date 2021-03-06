package com.lailem.app.dao;

import com.lailem.app.bean.Base;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MGROUPS".
 */
public class MGroup extends Base{

    private Long id;
    private Long createTime;
    private String groupId;
    private String groupType;
    private String userId;
    private Integer newPublishCount;
    private Integer newNoticeCount;
    private Integer newApplyCount;
    private Integer totalCount;
    private String isTop;
    private Long topTime;
    private Long updateTime;

    public MGroup() {
    }

    public MGroup(Long id) {
        this.id = id;
    }

    public MGroup(Long id, Long createTime, String groupId, String groupType, String userId, Integer newPublishCount, Integer newNoticeCount, Integer newApplyCount, Integer totalCount, String isTop, Long topTime, Long updateTime) {
        this.id = id;
        this.createTime = createTime;
        this.groupId = groupId;
        this.groupType = groupType;
        this.userId = userId;
        this.newPublishCount = newPublishCount;
        this.newNoticeCount = newNoticeCount;
        this.newApplyCount = newApplyCount;
        this.totalCount = totalCount;
        this.isTop = isTop;
        this.topTime = topTime;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getNewPublishCount() {
        return newPublishCount;
    }

    public void setNewPublishCount(Integer newPublishCount) {
        this.newPublishCount = newPublishCount;
    }

    public Integer getNewNoticeCount() {
        return newNoticeCount;
    }

    public void setNewNoticeCount(Integer newNoticeCount) {
        this.newNoticeCount = newNoticeCount;
    }

    public Integer getNewApplyCount() {
        return newApplyCount;
    }

    public void setNewApplyCount(Integer newApplyCount) {
        this.newApplyCount = newApplyCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public Long getTopTime() {
        return topTime;
    }

    public void setTopTime(Long topTime) {
        this.topTime = topTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

}
