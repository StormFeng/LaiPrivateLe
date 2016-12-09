package com.lailem.app.ui.create_old.dynamic.tpl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;
import com.socks.library.KLog;

import butterknife.Bind;

public class CreateVideoTpl extends BaseTpl<ObjectWrapper> implements OnClickListener {
    @Bind(R.id.playImage)
    ImageView playImageView;
    @Bind(R.id.previewImage)
    ImageView previewImageView;
    VideoModel bean;


    public CreateVideoTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_create_video;
    }

    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.bean = (VideoModel) wrapper.getObject();
        setItemFillParent(getChildAt(0));
        if (Func.checkImageTag(bean.getContent().getPreviewPic(), previewImageView)) {
            Glide.with(_activity).load(bean.getContent().getPreviewPic()).placeholder(R.drawable.empty).error(R.drawable.empty).into(previewImageView);
        }

        previewImageView.setOnClickListener(this);
    }

    private void setItemFillParent(View item) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item.getLayoutParams();
        params.width = (int) TDevice.getScreenWidth();
        setLayoutParams(params);
    }

    // DSLV时 此方法不起作用
    @Override
    protected void onItemClick() {
        super.onItemClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previewImage:
                KLog.i("videoPath:::" + bean.getContent().getFilename());
                KLog.i("previewImagePath:::" + bean.getContent().getPreviewPic());
                UIHelper.showPlayVideo(_activity, bean.getContent().getFilename(), bean.getContent().getPreviewPic());
                break;
        }

    }

}
