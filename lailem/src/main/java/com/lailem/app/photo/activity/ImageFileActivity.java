package com.lailem.app.photo.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.lailem.app.R;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.adapter.FolderAdapter;
import com.lailem.app.widget.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author king
 * @version 2014年10月18日  下午11:48:06
 * @QQ:595163260
 */
public class ImageFileActivity extends Activity {
    @Bind(R.id.topbar)
    TopBarView topbar;
    private FolderAdapter folderAdapter;
    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_photo_camera_image_file);
        ButterKnife.bind(this);
        mContext = this;
        GridView gridView = (GridView) findViewById(R.id.fileGridView);
        folderAdapter = new FolderAdapter(this);
        gridView.setAdapter(folderAdapter);
        topbar.setTitle("选取相册").setRightText("取消", new OnClickListener() {

            @Override
            public void onClick(View view) {
                //清空选择的图片
                PhotoManager.currPhotos.clear();
                PhotoManager.getInstance().finishActivities();
            }
        });

        PhotoManager.getInstance().add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().remove(this);
    }
}
