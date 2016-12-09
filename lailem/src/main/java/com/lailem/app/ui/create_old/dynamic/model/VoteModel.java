package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class VoteModel implements Serializable {
    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_VOTE;
    private Content content = new Content();

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content implements Serializable {
        private String topic = "";// 袁震胖不胖？
        private String description = "";// 袁震肚子好大！
        private String startTime = "";// 2015-08-0614: 06: 41
        private String endTime = "";// 2015-07-0614: 06: 45
        private String selectCount = "";// 1
        private ArrayList<VoteItem> voteItems;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSelectCount() {
            return selectCount;
        }

        public void setSelectCount(String selectCount) {
            this.selectCount = selectCount;
        }

        public ArrayList<VoteItem> getVoteItems() {
            return voteItems;
        }

        public void setVoteItems(ArrayList<VoteItem> voteItems) {
            this.voteItems = voteItems;
        }

    }

    public static class VoteItem implements Serializable {
        private String name = "";
        private String description = "";

        public VoteItem() {

        }

        public VoteItem(String name, String description) {
            super();
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

}
