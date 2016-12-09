package com.lailem.app.jsonbean.dynamic;

import com.lailem.app.bean.Result;

public class Comment extends Result {
    private String comment;// 评论内容 string
    private String createTime;// string
    private String headSPicName;// 小头像文件名 string
    private String id;// id string
    private String nickname;// 昵称 string
    private ToCommentInfo toCommentInfo;// 被评论的评论信息 object
    private String userId;// 评论者id string
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public ToCommentInfo getToCommentInfo() {
        return toCommentInfo;
    }

    public void setToCommentInfo(ToCommentInfo toCommentInfo) {
        this.toCommentInfo = toCommentInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class ToCommentInfo extends Result {
        private String comment;// 评论内容 string
        private String headSPicName;// 小头像文件名 string
        private String id;// string
        private String nickname;// 昵称 string
        private String userId;// 评论者id string
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }

}