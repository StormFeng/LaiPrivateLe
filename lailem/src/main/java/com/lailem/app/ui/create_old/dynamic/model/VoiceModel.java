package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;

public class VoiceModel implements Serializable {
    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_VOICE;
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
        private String filename = "";// voice_3333333.mp3
        private String duration = "";// 44
        private String text = "";// 好玩

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }

}
