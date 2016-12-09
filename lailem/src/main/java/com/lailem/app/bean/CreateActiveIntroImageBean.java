package com.lailem.app.bean;

import com.lailem.app.ui.create_old.CreateActiveActivity;

/**
 * Created by XuYang on 15/12/9.
 */
public class CreateActiveIntroImageBean extends Base {
    private String locPath;
    private String remotePath;
    private int width;
    private int height;

    public CreateActiveIntroImageBean() {
        this.itemViewType = CreateActiveActivity.TPL_IMAGE;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getLocPath() {
        return locPath;
    }

    public void setLocPath(String locPath) {
        this.locPath = locPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
}