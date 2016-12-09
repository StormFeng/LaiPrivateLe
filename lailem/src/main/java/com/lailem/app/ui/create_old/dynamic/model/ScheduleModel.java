package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.jsonbean.personal.Contact;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleModel implements Serializable {
    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_SCHEDULE;
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
        private String startTime = "";// 2015-08-0614: 06: 45
        private String endTime = "";// 2015-07-0614: 06: 41
        private String content = "";// 漂流漂流漂流漂流漂流
        private String topic = "";// 漂流
        private String lon = "";// 24.564654
        private String lat = "";// 85.545456344
        private String address = "";// 广东清远
        private ArrayList<Contact> contact = new ArrayList<>();

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setContact(ArrayList<Contact> contact) {
            this.contact = contact;
        }

        public ArrayList<Contact> getContact() {
            return contact;
        }
    }

}
