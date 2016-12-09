package com.lailem.app.ui.me.tpl.dynamic;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.jsonbean.activegroup.DynamicBean;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/22.
 */
public class MeDynamicSingleVideoTpl extends BaseTpl<DynamicBean.PublishInfo> {
    @Bind(R.id.image)
    ImageView image_iv;
    private DynamicBean.PublishInfo bean;
    private String imageUrl;

    private int DEFAULT = 100;

    public MeDynamicSingleVideoTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_single_video;
    }

    @Override
    protected void initView() {
        super.initView();
        DEFAULT = (int) (TDevice.getScreenWidth() * 0.67f);
    }

    @Override
    public void setBean(DynamicBean.PublishInfo bean, int position) {
        this.bean = bean;
        //显示图片
        imageUrl = ApiClient.getFileUrl(bean.getPreviewPic());
        if (Func.checkImageTag(imageUrl, image_iv)) {
            Glide.with(_activity).load(imageUrl).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
        }

        //布局宽高
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DEFAULT, DEFAULT);
        image_iv.setLayoutParams(params);

    }

    @OnClick(R.id.playVideo)
    public void playVideo() {
        String videoUrl = ApiClient.getFileUrl(bean.getFilename());
        UIHelper.showPlayVideo((Activity) getContext(), videoUrl, imageUrl);
    }
}
