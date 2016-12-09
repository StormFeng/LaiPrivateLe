package com.lailem.app.ui.create_old.dynamic.model;

import com.lailem.app.ui.create_old.dynamic.CreateDynamicActivity;

import java.io.Serializable;

/**
 * 发表动态-文本
 */
public class TextModel implements Serializable {

    private String content = "";
    private String publishType = CreateDynamicActivity.PUBLISH_TYPE_TEXT;

    public TextModel(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public String getPublishType() {
        return publishType;
    }
}
