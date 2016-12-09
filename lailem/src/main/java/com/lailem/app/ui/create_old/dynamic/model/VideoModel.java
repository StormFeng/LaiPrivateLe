package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;

public class VideoModel implements Serializable {

    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_VIDEO;
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
        private String title = "";// 小博美
        private String description = "";// 好可爱哦
        private String filename = "";// video_xiaobomei.mp4
        private String duration = "";// 55
        private String previewPic = "";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

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

        public void setPreviewPic(String previewPic) {
            this.previewPic = previewPic;
        }

        public String getPreviewPic() {
            return previewPic;
        }
    }
}
