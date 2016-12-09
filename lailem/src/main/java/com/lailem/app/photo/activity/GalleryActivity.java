package com.lailem.app.photo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.photo.PhotoManager;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.HackyViewPager;
import uk.co.senab.photoview.PhotoView;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author king
 * @version 2014年10月18日 下午11:47:53
 * @QQ:595163260
 */
public class GalleryActivity extends Activity {
    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    public List<String> del = new ArrayList<String>();
    RelativeLayout photo_relativeLayout;
    @Bind(R.id.topbar)
    TopBarView topbar;
    private Intent intent;
    // 发送按钮
    private Button send_bt;
    // 顶部显示预览图片位置的textview
    private TextView positionTextView;
    // 当前的位置
    private int location = 0;
    private ArrayList<View> listViews = null;
    private HackyViewPager pager;
    private MyPageAdapter adapter;
    private Context mContext;
    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_photo_camera_gallery);// 切屏到主界面
        ButterKnife.bind(this);
        mContext = this;
        send_bt = (Button) findViewById(R.id.send_button);
        send_bt.setOnClickListener(new GallerySendListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        isShowOkBt();
        // 为发送按钮设置文字
        pager = (HackyViewPager) findViewById(R.id.gallery01);
        pager.setOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < PhotoManager.currPhotos.size(); i++) {
            initListViews(PhotoManager.currPhotos.get(i));
        }

        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin(25);
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);

        topbar.setTitle("").setLeftText("返回", UIHelper.finish(this)).setRightText("删除", new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (listViews.size() == 1) {
                    PhotoManager.currPhotos.clear();
                    send_bt.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
                    Intent intent = new Intent("data.broadcast.action");
                    sendBroadcast(intent);
                    finish();
                } else {
                    if (PhotoManager.currPhotos.size() > 0) {
                        PhotoManager.currPhotos.remove(location);
                        pager.removeAllViews();
                        listViews.remove(location);
                        adapter.setListViews(listViews);
                        send_bt.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        PhotoManager.getInstance().add(this);
    }

    @Override
    protected void onDestroy() {
        PhotoManager.getInstance().remove(this);
        super.onDestroy();
    }

    private void initListViews(PhotoBean photoBean) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        if (Func.checkImageTag(photoBean.getImagePath(), img)) {
            Glide.with(this).load(photoBean.getImagePath()).centerCrop().into(img);
        }
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    public void isShowOkBt() {
        if (PhotoManager.currPhotos.size() > 0) {
            send_bt.setText("完成(" + PhotoManager.currPhotos.size() + "/" + PhotoManager.defaultLimit + ")");
            send_bt.setPressed(true);
            send_bt.setClickable(true);
            send_bt.setTextColor(Color.WHITE);
        } else {
            send_bt.setPressed(false);
            send_bt.setClickable(false);
            send_bt.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    // 完成按钮的监听
    private class GallerySendListener implements OnClickListener {
        public void onClick(View v) {
            PhotoManager.getInstance().finishActivities();
            PhotoManager.getInstance().onPhoto();
        }

    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((HackyViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((HackyViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
