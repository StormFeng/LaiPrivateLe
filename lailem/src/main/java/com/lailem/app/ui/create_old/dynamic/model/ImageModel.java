package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageModel implements Serializable {

    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_PICS;
    private Content content = new Content();
    private String tag = "";

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


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public static class Content implements Serializable {
        private ArrayList<Pic> pics;
        private String text = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public ArrayList<Pic> getPics() {
            return pics;
        }

        public void setPics(ArrayList<Pic> pics) {
            this.pics = pics;
        }

    }
}
