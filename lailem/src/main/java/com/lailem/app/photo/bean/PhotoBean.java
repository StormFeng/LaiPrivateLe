package com.lailem.app.photo.bean;

import com.lailem.app.utils.StringUtils;

import java.io.Serializable;

public class PhotoBean implements Serializable {

    private String imageId;
    private String imagePath;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (StringUtils.isEmpty(imagePath)) {
            return;
        }
        this.imagePath = imagePath;
    }

}
