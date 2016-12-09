package com.lailem.app.tpl;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.bean.CreateActiveIntroImageBean;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by XuYang on 15/12/10.
 */
public class CreateActiveImageTpl extends BaseLinearTpl<Object> {

    @Bind(R.id.image)
    ImageView image_iv;

    private float maxWidth = TDevice.getScreenWidth() * 3f / 4;

    public CreateActiveImageTpl(Context context, ArrayList<Object> data) {
        super(context, data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_active_intro_image;
    }

    @Override
    public void setBean() {
        CreateActiveIntroImageBean bean = getBean();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) image_iv.getLayoutParams();
        params.width = (int) maxWidth;
        params.height = (int) ((maxWidth * bean.getHeight()) / bean.getWidth());
        image_iv.setLayoutParams(params);
        if (Func.checkImageTag(bean.getLocPath(), image_iv)) {
            Glide.with(_activity).load(bean.getLocPath()).placeholder(R.drawable.empty).error(R.drawable.empty).into(image_iv);
        }

    }

    private CreateActiveIntroImageBean getBean() {
        LinearLayout intro_ll = (LinearLayout) getParent();
        int index = intro_ll.indexOfChild(CreateActiveImageTpl.this);
        CreateActiveIntroImageBean bean = (CreateActiveIntroImageBean) data.get(index);
        return bean;
    }
}
