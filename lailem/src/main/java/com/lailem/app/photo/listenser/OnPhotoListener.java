package com.lailem.app.photo.listenser;

import com.lailem.app.photo.bean.PhotoBean;

import java.util.ArrayList;

public interface OnPhotoListener {
    public void onPhoto(String tag, ArrayList<PhotoBean> photos);
}
