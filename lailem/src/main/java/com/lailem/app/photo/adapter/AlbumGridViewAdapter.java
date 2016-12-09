package com.lailem.app.photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.photo.bean.PhotoBean;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 *
 * @author king
 * @version 2014年10月18日 下午11:49:35
 * @QQ:595163260
 */
public class AlbumGridViewAdapter extends BaseAdapter {
    final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<PhotoBean> dataList;
    private ArrayList<PhotoBean> selectedDataList;
    private DisplayMetrics dm;

    private int maxWidth;
    private int maxHeight;
    private Object options;
    private OnItemClickListener mOnItemClickListener;

    public AlbumGridViewAdapter(Context c, ArrayList<PhotoBean> dataList, ArrayList<PhotoBean> selectedDataList) {
        mContext = c;
        this.dataList = dataList;
        this.selectedDataList = selectedDataList;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        maxHeight = (int) TDevice.dpToPixel(100);
        maxWidth = (int) ((TDevice.getScreenWidth() - TDevice.dpToPixel(20)) / 4.0f);
    }

    public int getCount() {
        return dataList.size();
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_photo_camera_select_imageview, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.imageView.setMaxHeight(maxHeight);
            viewHolder.imageView.setMaxWidth(maxWidth);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            viewHolder.chooseIV = (ImageView) convertView.findViewById(R.id.choosedbt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PhotoBean item = dataList.get(position);
        int oW = (int) (TDevice.getScreenWidth() / 4);
        int oH = oW;
        Glide.with(mContext).load(item.getImagePath()).asBitmap().thumbnail(0.2f).override(oW, oH).placeholder(R.drawable.empty).error(R.drawable.empty).into(viewHolder.imageView);
        viewHolder.toggleButton.setTag(position);
        viewHolder.chooseIV.setTag(position);
        viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.chooseIV));
        if (selectedDataList.contains(dataList.get(position))) {
            viewHolder.toggleButton.setChecked(true);
            viewHolder.chooseIV.setVisibility(View.VISIBLE);
        } else {
            viewHolder.toggleButton.setChecked(false);
            viewHolder.chooseIV.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnItemClickListener {
        public void onItemClick(ToggleButton view, int position, boolean isChecked, ImageView chooseBt);
    }

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        public ImageView imageView;
        public ToggleButton toggleButton;
        public ImageView chooseIV;
        public TextView textView;
    }

    private class ToggleClickListener implements OnClickListener {
        ImageView chooseIV;

        public ToggleClickListener(ImageView choosebt) {
            this.chooseIV = choosebt;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(), chooseIV);
                }
            }
        }
    }

}
