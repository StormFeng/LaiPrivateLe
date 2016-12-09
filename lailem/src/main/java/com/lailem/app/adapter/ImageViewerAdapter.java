package com.lailem.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.bean.Base;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewerAdapter extends PagerAdapter {
    private Context context;
    private AppContext ac;
    private ArrayList<ImageBean> imageBeans;

    public ImageViewerAdapter(Context context, ArrayList<ImageBean> imageBeans) {
        this.imageBeans = imageBeans;
        this.context = context;
        this.ac = (AppContext) context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return imageBeans.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View item = View.inflate(context, R.layout.item_imageviewer, null);
        final PhotoView photoView = (PhotoView) item.findViewById(R.id.image);
        final ProgressBar progressBar = (ProgressBar) item.findViewById(R.id.progressBar);
        final ImageView fail_iv = (ImageView) item.findViewById(R.id.fail);
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
        fail_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
        fail_iv.setVisibility(View.GONE);
        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(context).load(imageBeans.get(position).getThumb());
        Glide.with(context).load(imageBeans.get(position).getUrl()).thumbnail(thumbnailRequest).crossFade(0).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                fail_iv.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                fail_iv.setVisibility(View.GONE);
                return false;
            }
        }).into(photoView);
        container.addView(item, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public static class ImageBean extends Base {
        public String url;
        public String thumb;
        public String mimeType;
        public boolean isChecked = false;

        public ImageBean(String url, boolean isChecked) {
            super();
            this.url = url;
            this.isChecked = isChecked;
        }

        public ImageBean(String url, String thumb, boolean isChecked) {
            super();
            this.url = url;
            this.thumb = thumb;
            this.isChecked = isChecked;
        }

        public ImageBean(String url) {
            this.url = url;
        }

        public ImageBean(String url, String thumb) {
            this.url = url;
            this.thumb = thumb;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getThumb() {
            return thumb;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

}
