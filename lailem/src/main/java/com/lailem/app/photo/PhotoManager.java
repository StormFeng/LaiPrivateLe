package com.lailem.app.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.lailem.app.base.BaseActivity;
import com.lailem.app.photo.activity.AlbumActivity;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.listenser.OnPhotoListener;
import com.lailem.app.utils.BitmapUtil;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.ImageUtils;
import com.lailem.app.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 管理拍照或从图库中取图片
 *
 * @author leeib
 */
public class PhotoManager {

    public static final int TAKE_PHOTO = 0x000001;
    public static final int CUT_PHOTO = 0x000002;
    public static String currTag;
    public static int currLimit;
    public static ArrayList<PhotoBean> currPhotos;
    /**
     * 限制选取的照片张数
     */
    public static int defaultLimit = 9;
    private static PhotoManager instance;
    public int aspectX, aspectY, outputX, outputY;
    BaseActivity activity;
    String defaultTag = "defaultTag";
    ArrayList<Activity> activities = new ArrayList<Activity>();
    HashMap<String, ArrayList<PhotoBean>> map = new HashMap<String, ArrayList<PhotoBean>>();
    HashMap<String, Integer> limitMap = new HashMap<String, Integer>();
    OnPhotoListener onPhotoListener;
    /**
     * 是否需要裁剪
     */
    private boolean isCut = false;
    private String tempCameraPath;

    private PhotoManager() {
    }

    public static PhotoManager getInstance() {
        if (instance == null) {
            instance = new PhotoManager();
        }
        return instance;
    }

    public ArrayList<PhotoBean> getPhotos(String tag) {
        ArrayList<PhotoBean> photos = null;
        if (StringUtils.isEmpty(tag)) {
            tag = defaultTag;
        }
        if (map.containsKey(tag)) {
            photos = map.get(tag);
        } else {
            photos = new ArrayList<PhotoBean>();
            map.put(tag, photos);
        }
        return photos;
    }

    private void setCurr(String tag) {
        if (StringUtils.isEmpty(tag)) {
            currTag = defaultTag;
        } else {
            currTag = tag;
        }
        currLimit = getLimit(currTag);
        currPhotos = getPhotos(currTag);
    }

    public void addPhoto(PhotoBean photoBean) {
        getPhotos(currTag).add(photoBean);
    }

    public void removePhoto(PhotoBean photoBean) {
        getPhotos(currTag).remove(photoBean);
    }

    public void setLimit(String tag, int limit) {
        if (StringUtils.isEmpty(tag)) {
            tag = defaultTag;
        }
        limitMap.put(tag, limit);
    }

    private int getLimit(String tag) {
        if (StringUtils.isEmpty(tag)) {
            tag = defaultTag;
        }
        Integer limit = limitMap.get(tag);
        if (limit == null)
            limit = defaultLimit;
        return limit;
    }

    /**
     * 最终通过该方法传出照片
     */
    public void onPhoto() {
        if (onPhotoListener != null) {
            activity.showWaitDialog();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    //压缩图片
                    for (PhotoBean photoBean : currPhotos) {
                        File f = new File(photoBean.getImagePath());
                        String imageType = ImageUtils.getImageType(f);
                        if ("image/gif".equals(imageType)) {
                            //gif不压缩
                        } else {
                            Bitmap bitmap = BitmapUtil.decodeFile(photoBean.getImagePath(), 960);
                            File file = BitmapUtil.saveBitmap(bitmap, activity);
                            photoBean.setImagePath(file.getAbsolutePath());
                        }
                    }
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            activity.hideWaitDialog();
                            onPhotoListener.onPhoto(currTag, currPhotos);
                        }
                    });

                }
            }).start();
        }
    }

    /**
     * 从相册选择照片
     */
    public void album(String tag) {
        setCurr(tag);
        Intent intent = new Intent(activity, AlbumActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 从相册选择照片,当tag对应的limit=1为才支持裁剪
     */
    public void album(String tag, int aspectX, int aspectY, int outputX, int outputY) {
        setCurr(tag);
        if (getLimit(tag) == 1) {
            isCut = true;
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
        }
        Intent intent = new Intent(activity, AlbumActivity.class);
        activity.startActivity(intent);

    }

    /**
     * 拍照
     */
    public void photo(String tag) {
        setCurr(tag);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtils.createFile(".jpg", activity);
        tempCameraPath = file.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 拍照,当tag对应的limit=1为才支持裁剪
     */
    public void photo(String tag, int aspectX, int aspectY, int outputX, int outputY) {
        setCurr(tag);
        if (getLimit(tag) == 1) {
            isCut = true;
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtils.createFile(".jpg", activity);
        tempCameraPath = file.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    private void cutPhoto(String path, int aspectX, int aspectY, int outputX, int outputY) {
        isCut = false;
        Intent intent = new Intent(activity, CropActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CropActivity.BUNDLE_KEY_PATH, path);
        bundle.putInt(CropActivity.BUNDLE_KEY_ASPECT_X, aspectX);
        bundle.putInt(CropActivity.BUNDLE_KEY_ASPECT_Y, aspectY);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, CUT_PHOTO);
    }

    /**
     * 从相册选择1张图片的判断
     *
     * @param photoBean
     * @return
     */
    public boolean chechOnlySelectOne(PhotoBean photoBean) {
        if (currLimit == 1 && isCut) {
            currPhotos.clear();
            cutPhoto(photoBean.getImagePath(), aspectX, aspectY, outputX, outputY);
            return true;
        } else if (currLimit == 1) {
            onPhoto();
            return true;
        }

        return false;
    }

    /**
     * 在Activity方法onActivityResult(...)里调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 拍照
            case TAKE_PHOTO:
                if (getPhotos(currTag).size() < currLimit && resultCode == Activity.RESULT_OK) {
                    if (isCut) {// 需要裁剪
                        cutPhoto(tempCameraPath, aspectX, aspectY, outputX, outputY);
                    } else {
                        PhotoBean takePhoto = new PhotoBean();
                        takePhoto.setImagePath(tempCameraPath);
                        addPhoto(takePhoto);
                        onPhoto();
                    }
                }
                break;
            case CUT_PHOTO:
                if (data != null && data.getExtras() != null) {
                    String path = data.getExtras().getString(CropActivity.BUNDLE_KEY_PATH);
                    PhotoBean takePhoto = new PhotoBean();
                    takePhoto.setImagePath(path);
                    addPhoto(takePhoto);
                    onPhoto();

                }
                break;
        }
    }

    /**
     * 在Activity生命周期方法onDestory()中调用
     */
    public void onDestory() {
        map.clear();
        limitMap.clear();
        onPhotoListener = null;
        activity = null;
        currLimit = 0;
        currPhotos = null;
        currTag = null;
        tempCameraPath = null;
    }


    private void deleteFile(boolean deletePicFile) {
        if (deletePicFile) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        ArrayList<PhotoBean> photoBeans = map.get(key);
                        for (PhotoBean photoBean : photoBeans) {
                            FileUtils.delete(photoBean.getImagePath());
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 在activity生命周期方法onCreate(..)中调用
     *
     * @param activity
     */
    public void onCreate(BaseActivity activity, OnPhotoListener onPhotoListener) {
        this.activity = activity;
        this.onPhotoListener = onPhotoListener;
    }

    public void add(Activity activity) {
        if (activities.contains(activity))
            return;
        activities.add(activity);
    }

    public void remove(Activity activity) {
        if (activities.contains(activity))
            activities.remove(activity);
    }

    public void finishActivities() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }


}
