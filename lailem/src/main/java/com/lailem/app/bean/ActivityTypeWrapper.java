package com.lailem.app.bean;

import com.lailem.app.jsonbean.dynamic.CommonConfigBean;

/**
 * Created by XuYang on 15/11/29.
 */
public class ActivityTypeWrapper extends Base {
    private CommonConfigBean.ActivityType activityType;
    private boolean hasDraft;
    private CreateActiveCache cache;

    public ActivityTypeWrapper() {
    }

    public ActivityTypeWrapper(CommonConfigBean.ActivityType activityType, boolean hasDraft) {
        this.activityType = activityType;
        this.hasDraft = hasDraft;
    }

    public ActivityTypeWrapper(CommonConfigBean.ActivityType activityType, boolean hasDraft, CreateActiveCache cache) {
        this.activityType = activityType;
        this.hasDraft = hasDraft;
        this.cache = cache;
    }

    public CreateActiveCache getCache() {
        return cache;
    }

    public void setCache(CreateActiveCache cache) {
        this.cache = cache;
    }

    public CommonConfigBean.ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(CommonConfigBean.ActivityType activityType) {
        this.activityType = activityType;
    }

    public boolean isHasDraft() {
        return hasDraft;
    }

    public void setHasDraft(boolean hasDraft) {
        this.hasDraft = hasDraft;
    }

}
