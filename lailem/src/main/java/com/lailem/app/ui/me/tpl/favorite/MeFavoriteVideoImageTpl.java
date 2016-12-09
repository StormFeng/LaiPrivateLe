package com.lailem.app.ui.me.tpl.favorite;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.adapter.ImageViewerAdapter;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.jsonbean.activegroup.Pic;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.FlowLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/21.
 */
public class MeFavoriteVideoImageTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.fl)
    FlowLayout fl;

    private DynamicBean.PublishInfo videoBean;
    private DynamicBean.PublishInfo imageBean;

    public MeFavoriteVideoImageTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_new_video_image;
    }

    @Override
    protected void initView() {
        super.initView();
        int DEFAULT = (int) (TDevice.getScreenWidth() * 0.77f);

        LayoutParams params = (LayoutParams) fl.getLayoutParams();
        params.width = DEFAULT;
        params.height = LayoutParams.WRAP_CONTENT;
        fl.setLayoutParams(params);

        int ITEM_DEFAULT = (int) ((DEFAULT - TDevice.dpToPixel(5) * 2) / 3);
        for (int i = 0; i < fl.getChildCount(); i++) {
            FrameLayout frameLayout = (FrameLayout) fl.getChildAt(i);
            ImageView image = (ImageView) frameLayout.getChildAt(0);
            FrameLayout.LayoutParams imageParams = (FrameLayout.LayoutParams) image.getLayoutParams();
            imageParams.width = ITEM_DEFAULT;
            imageParams.height = ITEM_DEFAULT;
            image.setLayoutParams(imageParams);
        }
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        Object[] objArr = (Object[]) wrapper.getObject();
        videoBean = (DynamicBean.PublishInfo) objArr[0];
        imageBean = (DynamicBean.PublishInfo) objArr[1];

        for (int i = 0; i < fl.getChildCount(); i++) {
            FrameLayout frameLayout = (FrameLayout) fl.getChildAt(i);
            ImageView image = (ImageView) frameLayout.getChildAt(0);
            ImageView gifMark = (ImageView) frameLayout.getChildAt(1);
            if (i == 0) {
                ImageView playVideo = (ImageView) frameLayout.getChildAt(2);
                if (videoBean == null) {
                    playVideo.setVisibility(GONE);
                } else {
                    gifMark.setVisibility(GONE);
                    playVideo.setVisibility(VISIBLE);
                    String previewUrl = ApiClient.getFileUrl(videoBean.getPreviewPic());
                    if (Func.checkImageTag(previewUrl, image)) {
                        Glide.with(_activity).load(previewUrl).centerCrop().placeholder(R.drawable.empty).error(R.drawable.empty).into(image);
                    }
                    continue;
                }
            }

            if (imageBean.getPics() != null && i < imageBean.getPics().size() + (videoBean == null ? 0 : 1)) {
                frameLayout.setVisibility(VISIBLE);
                final Pic pic = imageBean.getPics().get(videoBean == null ? i : i - 1);
                //gif标记
                if (!TextUtils.isEmpty(pic.getgFilename()) && pic.getgFilename().endsWith(".gif")) {
                    gifMark.setVisibility(VISIBLE);
                } else {
                    gifMark.setVisibility(GONE);
                }
                //图片显示
                String imageUrl = ApiClient.getFileUrl(pic.gettFilename());
                if (Func.checkImageTag(imageUrl, image)) {
                    Glide.with(_activity).load(imageUrl).centerCrop().placeholder(R.drawable.empty).error(R.drawable.empty).into(image);
                }
                final int index = i;
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<ImageViewerAdapter.ImageBean> imageBeans = new ArrayList<ImageViewerAdapter.ImageBean>();
                        for (int j = 0; j < imageBean.getPics().size(); j++) {
                            Pic p = imageBean.getPics().get(j);
                            String url = "";
                            String thumb = ApiClient.getFileUrl(p.gettFilename());
                            if (!TextUtils.isEmpty(p.getgFilename()) && p.getgFilename().endsWith(".gif")) {
                                url = ApiClient.getFileUrl(p.getgFilename());
                            } else {
                                url = ApiClient.getFileUrl(p.getFilename());
                            }
                            ImageViewerAdapter.ImageBean b = new ImageViewerAdapter.ImageBean(url, thumb);
                            imageBeans.add(b);
                        }
                        if (videoBean == null) {
                            UIHelper.showImages(_activity, imageBeans, index);
                        } else {
                            UIHelper.showImages(_activity, imageBeans, index - 1);
                        }

                    }
                });
            } else {
                frameLayout.setVisibility(GONE);
            }

        }
    }

    @OnClick(R.id.playVideo)
    public void playVideo() {
        if (videoBean != null) {
            String videoUrl = ApiClient.getFileUrl(videoBean.getFilename());
            String imageUrl = ApiClient.getFileUrl(videoBean.getPreviewPic());
            UIHelper.showPlayVideo((Activity) getContext(), videoUrl, imageUrl);
        }
    }

}
