package com.lailem.app.jsonbean.activegroup;

import com.lailem.app.bean.Result;
import com.lailem.app.jsonbean.personal.Contact;

import java.util.ArrayList;

public class DynamicBean extends Result {
    private String activityId;// 活动id string
    // 活动id（独立的活动id与群内活动id）,即group表的表id
    private String collectCount;// 收藏数量 string
    private String comCount;// 评论数量 string
    private CreateActivityInfo createActivityInfo;// 创建活动信息 object
    private String createTime;// string
    private String dynaType;// 动态类型 string
    // 动态类型：1（发表）、2（通知）、3（活动）、4（群）、5（创建活动）、6（应用内转载）
    private String dynamicId;
    private String groupId;// 群id string
    private String id;// id string
    private String isLiked;// 是否已点赞 string 1（已赞）、2（未赞）
    private String isCollected;// 是否已收藏 string 1（已收藏）、2（未收藏）

    private String likeCount;// 赞数量 string
    private NoticeInfo noticeInfo;// 通知信息 object
    private ArrayList<PublishInfo> publishList;// 发表信息 object
    private PublisherInfo publisherInfo;// 发布者信息 object
    private String uniteGroupId;// 群联id string
    private String remark;
    private String dynaForm;

    public String getDynaForm() {
        return dynaForm;
    }

    public void setDynaForm(String dynaForm) {
        this.dynaForm = dynaForm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(String isCollected) {
        this.isCollected = isCollected;
    }

    public ArrayList<PublishInfo> getPublishList() {
        return publishList;
    }

    public void setPublishList(ArrayList<PublishInfo> publishList) {
        this.publishList = publishList;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    public String getComCount() {
        return comCount;
    }

    public void setComCount(String comCount) {
        this.comCount = comCount;
    }

    public CreateActivityInfo getCreateActivityInfo() {
        return createActivityInfo;
    }

    public void setCreateActivityInfo(CreateActivityInfo createActivityInfo) {
        this.createActivityInfo = createActivityInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDynaType() {
        return dynaType;
    }

    public void setDynaType(String dynaType) {
        this.dynaType = dynaType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public NoticeInfo getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(NoticeInfo noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public PublisherInfo getPublisherInfo() {
        return publisherInfo;
    }

    public void setPublisherInfo(PublisherInfo publisherInfo) {
        this.publisherInfo = publisherInfo;
    }

    public String getUniteGroupId() {
        return uniteGroupId;
    }

    public void setUniteGroupId(String uniteGroupId) {
        this.uniteGroupId = uniteGroupId;
    }

    public static class CreateActivityInfo extends Result {
        private CreatorInfo2 creatorInfo;// 创建者信息 object
        private String id;// string
        private String name;// 活动名称 string
        private String squareSPicName;// 正方形小图文件名 string

        public CreatorInfo2 getCreatorInfo() {
            return creatorInfo;
        }

        public void setCreatorInfo(CreatorInfo2 creatorInfo) {
            this.creatorInfo = creatorInfo;
        }

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

    public static class PublishInfo extends VoteInfo implements Cloneable {
        private String publishType;
        private String address;
        private String content;
        private String lat;
        private String lon;
        private ArrayList<Pic> pics;
        private String duration;
        private String filename;
        private String text;

        private String description;
        private String previewPic;
        private String title;

        public PublishInfo(String publishType) {
            this.publishType = publishType;
        }

        private ArrayList<Contact> contact;

        public String getPublishType() {
            return publishType;
        }

        public void setPublishType(String publishType) {
            this.publishType = publishType;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public ArrayList<Pic> getPics() {
            return pics;
        }

        public void setPics(ArrayList<Pic> pics) {
            this.pics = pics;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPreviewPic() {
            return previewPic;
        }

        public void setPreviewPic(String previewPic) {
            this.previewPic = previewPic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContact(ArrayList<Contact> contact) {
            this.contact = contact;
        }

        public ArrayList<Contact> getContact() {
            return contact;
        }


    }

    public static class PublisherInfo extends Result {
        private String age;// 年龄 string
        private String headSPicName;// 小头像文件名 string
        private String id;// id string
        private String nickname;// 昵称 string
        private String sex;// 性别 string 1（男）、0（女）
        private String createTime;
        private String dynaType;
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setDynaType(String dynaType) {
            this.dynaType = dynaType;
        }

        public String getDynaType() {
            return dynaType;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTime() {
            return createTime;
        }

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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

    }

    public static class CreatorInfo2 extends Result {
        private String headSPicName;// 小头像文件名 string
        private String id;// 创建者id string
        private String nickname;//

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

    }

    public static class NoticeInfo extends Result {
        private String detail;// 详情 string
        private String id;// id string
        private String topic;// 主题 string

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

    }

}