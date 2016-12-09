package com.lailem.app.jsonbean.activegroup;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.dynamic.Comment;
import com.lailem.app.jsonbean.personal.Contact;

import java.util.ArrayList;

/**
 * 投票活动详情
 *
 * @author XuYang
 */
public class VoteActiveDetailBean extends Result {

    private ActiveInfo activityInfo;// 活动信息 object
    private ArrayList<Comment> commentList;// 评论列表 array<object>

    public ActiveInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActiveInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public static VoteActiveDetailBean parse(String json) throws AppException {
        VoteActiveDetailBean res = new VoteActiveDetailBean();
        try {
            res = gson.fromJson(json, VoteActiveDetailBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw AppException.json(e);
        }
        return res;
    }

    public static class ActiveInfo extends Result {

        private VoteInfo voteInfo;

        public VoteInfo getVoteInfo() {
            return voteInfo;
        }

        public void setVoteInfo(VoteInfo voteInfo) {
            this.voteInfo = voteInfo;
        }

        private String address;// 地址 string
        private String bPicName;// 大图文件名 string
        private String comCount;// 评论数量 string
        private String collectCount;// 收藏数量 string
        private ArrayList<Contact> contact;// 联系人 string
        // 格式：[{"name":"李小姐","phone":"18664891306,020-22378976"}]
        private String createTime;// string
        private CreatorInfo creatorInfo;// 创建者信息 object
        private String currCount;// 当前人数 string
        private String id;// id string
        private String intro;// 简介 string
        private String isLiked;// 是否已点赞 string 1（已赞）、2（未赞）
        private String lat;// 纬度 string
        private String likeCount;// 赞数量 string
        private String isCollected;// 是否已收藏 string 1（已收藏）、2（未收藏）
        private String lon;// 经度 string
        private String manCount;// 男人数量 string
        private ArrayList<Member> memberList;// 成员列表 array<object>
        private String name;// 名称 string
        private RecentNotice recentNotice;// 最近通知 object 已参加的用户才返回；
        private String roleType;// 角色类型 角色类型：1（创建者）、2（管理员）、3（普通成员）、4（未加入）
        private String startTime;// 开始时间 string 时间格式见【公共信息】模块里的【时间格式】
        private String typeName;// 所属类型名称 string
        private String womenCount;// 女人数量 string
        private String groupNum;// 活动号
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setGroupNum(String groupNum) {
            this.groupNum = groupNum;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public void setCollectCount(String collectCount) {
            this.collectCount = collectCount;
        }

        public String getCollectCount() {
            return collectCount;
        }

        public void setIsCollected(String isCollected) {
            this.isCollected = isCollected;
        }

        public String getIsCollected() {
            return isCollected;
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

        public String getComCount() {
            return comCount;
        }

        public void setComCount(String comCount) {
            this.comCount = comCount;
        }

        public void setContact(ArrayList<Contact> contact) {
            this.contact = contact;
        }

        public ArrayList<Contact> getContact() {
            return contact;
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

        public String getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(String isLiked) {
            this.isLiked = isLiked;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
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

        public ArrayList<Member> getMemberList() {
            return memberList;
        }

        public void setMemberList(ArrayList<Member> memberList) {
            this.memberList = memberList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public RecentNotice getRecentNotice() {
            return recentNotice;
        }

        public void setRecentNotice(RecentNotice recentNotice) {
            this.recentNotice = recentNotice;
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
