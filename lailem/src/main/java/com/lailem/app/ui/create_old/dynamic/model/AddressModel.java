package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;

public class AddressModel implements Serializable {

    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_ADDRESS;

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
        private String address = "";// 广州天河小新塘
        private String lon = "";// 22.52145
        private String lat = "";// 42.25444

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

    }
}
