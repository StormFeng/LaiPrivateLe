package com.lailem.app.photo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lailem.app.R;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.adapter.AlbumGridViewAdapter;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 这个是显示一个文件夹里面的所有图片时的界面
 *
 * @author king
 * @version 2014年10月18日 下午11:49:10
 * @QQ:595163260
 */
public class ShowAllPhotoActivity extends Activity {
    public static ArrayList<PhotoBean> dataList = new ArrayList<PhotoBean>();
    @Bind(R.id.topbar)
    TopBarView topbar;
    private GridView gridView;
    private ProgressBar progressBar;
    private AlbumGridViewAdapter gridImageAdapter;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            gridImageAdapter.notifyDataSetChanged();
        }
    };
    // 完成按钮
    private Button okButton;
    // 预览按钮
    private Button preview;
    private Intent intent;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_photo_camera_show_all_photo);
        ButterKnife.bind(this);
        mContext = this;
        preview = (Button) findViewById(R.id.showallphoto_preview);
        okButton = (Button) findViewById(R.id.showallphoto_ok_button);
        this.intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        if (folderName.length() > 8) {
            folderName = folderName.substring(0, 9) + "...";
        }
        topbar.setTitle(folderName).setLeftText("相册", UIHelper.finish(this)).setRightText("取消", new OnClickListener() {

            @Override
            public void onClick(View view) {
                PhotoManager.currPhotos.clear();
                PhotoManager.getInstance().finishActivities();
            }
        });
        preview.setOnClickListener(new PreviewListener());
        init();
        initListener();
        isShowOkBt();
        PhotoManager.getInstance().add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        PhotoManager.getInstance().remove(this);
    }

    private void init() {
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        progressBar = (ProgressBar) findViewById(R.id.showallphoto_progressbar);
        progressBar.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.showallphoto_myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, PhotoManager.currPhotos);
        gridView.setAdapter(gridImageAdapter);
        okButton = (Button) findViewById(R.id.showallphoto_ok_button);
    }

    private void initListener() {

        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, ImageView chooseIV) {
                if (PhotoManager.currPhotos.size() >= PhotoManager.defaultLimit && isChecked) {
                    chooseIV.setVisibility(View.GONE);
                    toggleButton.setChecked(false);
                    Toast.makeText(ShowAllPhotoActivity.this, "超出可选图片张数", 200).show();
                    return;
                }

                if (isChecked) {
                    chooseIV.setVisibility(View.VISIBLE);
                    PhotoManager.currPhotos.add(dataList.get(position));
                    okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
                } else {
                    chooseIV.setVisibility(View.GONE);
                    PhotoManager.currPhotos.remove(dataList.get(position));
                    okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
                }
                isShowOkBt();

                if (PhotoManager.getInstance().chechOnlySelectOne(dataList.get(position))) {
                    finish();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                okButton.setClickable(false);
                PhotoManager.getInstance().finishActivities();
                PhotoManager.getInstance().onPhoto();
            }
        });

    }

    public void isShowOkBt() {
        if (PhotoManager.currPhotos.size() > 0) {
            okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }

    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
            if (PhotoManager.currPhotos.size() > 0) {
                intent.setClass(ShowAllPhotoActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        }

    }

}
