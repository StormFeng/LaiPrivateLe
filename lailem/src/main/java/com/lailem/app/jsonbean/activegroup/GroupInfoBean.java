package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.Contact;

import java.util.ArrayList;

/**
 * 活动详情-群组详情
 *
 * @author XuYang
 */
public class GroupInfoBean extends Result {

    private GroupInfo groupInfo;// 活动或群信息

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public static GroupInfoBean parse(String json) throws AppException {
        GroupInfoBean res = new GroupInfoBean();
        try {
            res = gson.fromJson(json, GroupInfoBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    public static class GroupInfo extends Result {
        private String activityCount;// 活动数量
        private String createActivityFlay;//普通成员创建群内活动权限开关 1（允许）2（不允许）
        private ArrayList<Activiti> activityList;
        private String address;// 地址
        private String bPicName;// 大图文件名
        private ArrayList<Contact> contact;// 联系人 string
        // 格式：[{"name":"李小姐","phone":"18664891306,020-22378976"}]
        private String createTime;
        private CreatorInfo creatorInfo;// 创建者信息 object
        private String currCount;// 当前人数
        private ArrayList<DynamicBean> dynamicList;// 动态列表 array<object>
        // 动态的类型有多种，接口只返回对应类型的字段，如该条动态是通知，则接口只返回noticeInfo以及公用字段
        private String groupNum;// 群号
        private String id;// id
        private String intro;// 简介
        private String lat; // 纬度
        private String lon; // 经度
        private String manCount;// 男人数量
        private ArrayList<Member> memberList;// 成员列表 array<object>
        private String name;// 名称
        private RecentNotice recentNotice;// 最近通知 object 已参加的用户才返回；
        private String roleType;// 角色类型 string 角色类型：1（创建者）、2（管理员）、3（普通成员）、4（未加入）
        private String startTime;// 开始时间 string 时间格式见【公共信息】模块里的【时间格式】
        private String tagNames;// 群标签 string
        private String typeName;// 所属类型名称 string
        private String womenCount;// 女人数量 string
        private String permission;// 权限 string
        // 权限：1（私有群，需邀请加入）、2（公开群：会出现在附近群组或活动列表里）
        private String verifyWay;// 验证方式 string 1（文本）、2（语音）
        private String remark;

        private String dynamicCount;

        public String getCreateActivityFlay() {
            return createActivityFlay;
        }

        public void setCreateActivityFlay(String createActivityFlay) {
            this.createActivityFlay = createActivityFlay;
        }

        public void setDynamicCount(String dynamicCount) {
            this.dynamicCount = dynamicCount;
        }

        public String getDynamicCount() {
            return dynamicCount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setContact(ArrayList<Contact> contact) {
            this.contact = contact;
        }

        public ArrayList<Contact> getContact() {
            return contact;
        }

        public RecentNotice getRecentNotice() {
            return recentNotice;
        }

        public void setRecentNotice(RecentNotice recentNotice) {
            this.recentNotice = recentNotice;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }

        public void setVerifyWay(String verifyWay) {
            this.verifyWay = verifyWay;
        }

        public String getVerifyWay() {
            return verifyWay;
        }

        public ArrayList<Member> getMemberList() {
            return memberList;
        }

        public void setMemberList(ArrayList<Member> memberList) {
            this.memberList = memberList;
        }

        public String getActivityCount() {
            return activityCount;
        }

        public void setActivityCount(String activityCount) {
            this.activityCount = activityCount;
        }

        public ArrayList<Activiti> getActivityList() {
            return activityList;
        }

        public void setActivityList(ArrayList<Activiti> activityList) {
            this.activityList = activityList;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getbPicName() {
            return bPicName;
        }

        public void setbPicName(String bPicName) {
            this.bPicName = bPicName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public CreatorInfo getCreatorInfo() {
            return creatorInfo;
        }

        public void setCreatorInfo(CreatorInfo creatorInfo) {
            this.creatorInfo = creatorInfo;
        }

        public String getCurrCount() {
            return currCount;
        }

        public void setCurrCount(String currCount) {
            this.currCount = currCount;
        }

        public ArrayList<DynamicBean> getDynamicList() {
            return dynamicList;
        }

        public void setDynamicList(ArrayList<DynamicBean> dynamicList) {
            this.dynamicList = dynamicList;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getManCount() {
            return manCount;
        }

        public void setManCount(String manCount) {
            this.manCount = manCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRoleType() {
            return roleType;
        }

        public void setRoleType(String roleType) {
            this.roleType = roleType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTagNames() {
            return tagNames;
        }

        public void setTagNames(String tagNames) {
            this.tagNames = tagNames;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getWomenCount() {
            return womenCount;
        }

        public void setWomenCount(String womenCount) {
            this.womenCount = womenCount;
        }

    }

    public static class Activiti extends Result {
        private String id;// string
        private String name;// 活动名称 string
        private String squareSPicName;// 正方形小图文件名 string

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSquareSPicName() {
            return squareSPicName;
        }

        public void setSquareSPicName(String squareSPicName) {
            this.squareSPicName = squareSPicName;
        }

    }

    public static class CreatorInfo extends Result {
        private String age;// 年龄 string
        private String headSPicName;// 小头像文件名 string
        private String id;// 创建者id string
        private String nickname;// 昵称 string
        private String personalizedSignature;// 个性签名 string
        private String sex;// 性别 string 1（男）、0（女）

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHeadSPicName() {
            return headSPicName;
        }

        public void setHeadSPicName(String headSPicName) {
            this.headSPicName = headSPicName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPersonalizedSignature() {
            return personalizedSignature;
        }

        public void setPersonalizedSignature(String personalizedSignature) {
            this.personalizedSignature = personalizedSignature;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

    }

}
