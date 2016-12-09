package com.lailem.app.ui.group.tpl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/22.
 */
public class GroupHomeSingleImageTpl extends BaseTpl<DynamicBean.PublishInfo> {
    @Bind(R.id.image)
    ImageView image_iv;
    @Bind(R.id.gifMark)
    ImageView gifMark_iv;

    private int DEFAULT = 100;
    private DynamicBean.PublishInfo bean;
    private Pic pic;

    public GroupHomeSingleImageTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_single_image;
    }

    @Override
    protected void initView() {
        super.initView();
        DEFAULT = (int) (TDevice.getScreenWidth() * 0.67f);
    }

    @Override
    public void setBean(DynamicBean.PublishInfo bean, int position) {
        this.bean = bean;
        ArrayList<Pic> pics = bean.getPics();
        if (pics != null && pics.size() > 0) {
            this.pic = pics.get(0);
            //gif标记
            if (!TextUtils.isEmpty(pic.getgFilename()) && pic.getgFilename().endsWith(".gif")) {
                gifMark_iv.setVisibility(VISIBLE);
            } else {
                gifMark_iv.setVisibility(GONE);
            }

            //布局宽高
            int width = 0;
            int height = 0;
            try {
                width = Integer.parseInt(pic.getW());
                height = Integer.parseInt(pic.getH());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (width == 0 || height == 0) {
                width = DEFAULT;
                height = DEFAULT;
            }
            if (width > height) {
                float rate = height * 1.0f / width;
                width = DEFAULT;
                height = (int) (DEFAULT * rate);
            } else if (width < height) {
                float rate = width * 1.0f / height;
                height = DEFAULT;
                width = (int) (DEFAULT * rate);
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            image_iv.setLayoutParams(params);

            //显示图片
            String imageUrl = ApiClient.getFileUrl(pic.gettFilename());
            if (Func.checkImageTag(imageUrl, image_iv)) {
                Glide.with(_activity).load(imageUrl).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
            }
        } else {
            this.pic = null;
            gifMark_iv.setVisibility(GONE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DEFAULT, DEFAULT);
            image_iv.setLayoutParams(params);
            image_iv.setImageResource(R.drawable.empty);
        }
    }

    @OnClick(R.id.image)
    public void clickImage() {
        if (this.pic != null) {
            ArrayList<ImageViewerAdapter.ImageBean> beans = new ArrayList<ImageViewerAdapter.ImageBean>();
            String url = "";
            String thumb = ApiClient.getFileUrl(pic.gettFilename());
            if (!TextUtils.isEmpty(pic.getgFilename()) && pic.getgFilename().endsWith(".gif")) {
                url = ApiClient.getFileUrl(pic.getgFilename());
            } else {
                url = ApiClient.getFileUrl(pic.getFilename());
            }

            ImageViewerAdapter.ImageBean bean = new ImageViewerAdapter.ImageBean(url, thumb);
            beans.add(bean);
            UIHelper.showImages(_activity, beans, 0);
        }
    }
}
