package com.lailem.app.photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.photo.activity.AlbumActivity;
import com.lailem.app.photo.activity.ShowAllPhotoActivity;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

/**
 * 这个是显示所有包含图片的文件夹的适配器
 *
 * @author king
 * @version 2014年10月18日 下午11:49:44
 * @QQ:595163260
 */
public class FolderAdapter extends BaseAdapter {

    final String TAG = getClass().getSimpleName();
    ViewHolder holder = null;
    private Context mContext;
    private Intent mIntent;
    private DisplayMetrics dm;

    public FolderAdapter(Context c) {
        init(c);
    }

    // 初始化
    public void init(Context c) {
        mContext = c;
        mIntent = ((Activity) mContext).getIntent();
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public int getCount() {
        return AlbumActivity.contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_photo_camera_select_folder, null);
            holder = new ViewHolder();
            holder.backImage = (ImageView) convertView.findViewById(R.id.file_back);
            holder.imageView = (ImageView) convertView.findViewById(R.id.file_image);
            holder.choose_back = (ImageView) convertView.findViewById(R.id.choose_back);
            holder.folderName = (TextView) convertView.findViewById(R.id.name);
            holder.fileNum = (TextView) convertView.findViewById(R.id.filenum);
            holder.imageView.setAdjustViewBounds(true);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (AlbumActivity.contentList.get(position).imageList != null) {

            // 封面图片路径
            String path = AlbumActivity.contentList.get(position).imageList.get(0).getImagePath();
            // 给folderName设置值为文件夹名称
            holder.folderName.setText(AlbumActivity.contentList.get(position).bucketName);

            // 给fileNum设置文件夹内图片数量
            holder.fileNum.setText("" + AlbumActivity.contentList.get(position).count);

            PhotoBean item = AlbumActivity.contentList.get(position).imageList.get(0);

            int oW = (int) (TDevice.getScreenWidth() / 4);
            int oH = oW;
            Glide.with(mContext).load(item.getImagePath()).asBitmap().thumbnail(0.2f).override(oW, oH).placeholder(R.drawable.empty).error(R.drawable.empty).into(holder.imageView);
            // 为封面添加监听
        }
        holder.imageView.setOnClickListener(new ImageViewClickListener(position, mIntent, holder.choose_back));
        return convertView;
    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

    private class ViewHolder {
        //
        public ImageView backImage;
        // 封面
        public ImageView imageView;
        public ImageView choose_back;
        // 文件夹名称
        public TextView folderName;
        // 文件夹里面的图片数量
        public TextView fileNum;
    }

    // 为每一个文件夹构建的监听器
    private class ImageViewClickListener implements OnClickListener {
        private int position;
        private Intent intent;
        private ImageView choose_back;

        public ImageViewClickListener(int position, Intent intent, ImageView choose_back) {
            this.position = position;
            this.intent = intent;
            this.choose_back = choose_back;
        }

        public void onClick(View v) {
            ShowAllPhotoActivity.dataList = (ArrayList<PhotoBean>) AlbumActivity.contentList.get(position).imageList;
            Intent intent = new Intent();
            String folderName = AlbumActivity.contentList.get(position).bucketName;
            intent.putExtra("folderName", folderName);
            intent.setClass(mContext, ShowAllPhotoActivity.class);
            mContext.startActivity(intent);
            choose_back.setVisibility(v.VISIBLE);
        }
    }

}
