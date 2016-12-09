package com.lailem.app.photo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.adapter.AlbumGridViewAdapter;
import com.lailem.app.photo.bean.ImageBucket;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.photo.util.AlbumHelper;
import com.lailem.app.widget.TopBarView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 这个是进入相册显示所有图片的界面
 *
 * @author king
 * @version 2014年10月18日 下午11:47:15
 * @QQ:595163260
 */
public class AlbumActivity extends Activity {
    public static List<ImageBucket> contentList;
    @Bind(R.id.topbar)
    TopBarView topbar;
    // 显示手机里的所有图片的列表控件
    private GridView gridView;
    // 当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    // gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            gridImageAdapter.notifyDataSetChanged();
        }
    };
    // 完成按钮
    private Button okButton;
    private Intent intent;
    // 预览按钮
    private Button preview;
    private ArrayList<PhotoBean> dataList;
    private AlbumHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_photo_camera_album);
        ButterKnife.bind(this);
        // 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        init();
        initListener();
        // 这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();

        topbar.setTitle("").setLeftText("相册", new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                intent.setClass(AlbumActivity.this, ImageFileActivity.class);
                startActivity(intent);
                finish();

            }
        }).setRightText("取消", new OnClickListener() {

            @Override
            public void onClick(View view) {
                PhotoManager.currPhotos.clear();
                finish();
            }
        });

        PhotoManager.getInstance().add(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        PhotoManager.getInstance().remove(this);
        unregisterReceiver(broadcastReceiver);
    }

    // 初始化，给一些对象赋值
    private void init() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<PhotoBean>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }

        preview = (Button) findViewById(R.id.preview);
        preview.setOnClickListener(new PreviewListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, PhotoManager.currPhotos);
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(R.id.myText);
        gridView.setEmptyView(tv);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.currLimit + ")");
    }

    private void initListener() {

        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, ImageView chooseIV) {
                KLog.i(dataList.get(position).toString());
                if (PhotoManager.currPhotos.size() >= PhotoManager.defaultLimit) {
                    toggleButton.setChecked(false);
                    chooseIV.setVisibility(View.GONE);
                    if (!removeOneData(dataList.get(position))) {
                        AppContext.showToast("超出可选图片张数");
                    }
                    return;
                }
                if (isChecked) {
                    chooseIV.setVisibility(View.VISIBLE);
                    PhotoManager.currPhotos.add(dataList.get(position));
                    okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.currLimit + ")");
                } else {
                    PhotoManager.currPhotos.remove(dataList.get(position));
                    chooseIV.setVisibility(View.GONE);
                    okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.currLimit + ")");
                }
                isShowOkBt();

                if (PhotoManager.getInstance().chechOnlySelectOne(dataList.get(position))) {
                    finish();
                }

            }
        });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(PhotoBean imageItem) {
        if (PhotoManager.currPhotos.contains(imageItem)) {
            PhotoManager.currPhotos.remove(imageItem);
            okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.currLimit + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (PhotoManager.currPhotos.size() > 0) {
            okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.currLimit + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.currLimit + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent.setClass(AlbumActivity.this, ImageFileActivity.class);
            startActivity(intent);
        }
        return false;

    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }

    // 预览按钮的监听
    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
            if (PhotoManager.currPhotos.size() > 0) {
                intent.setClass(AlbumActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        }

    }

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
            finish();
            PhotoManager.getInstance().onPhoto();
        }

    }
}
