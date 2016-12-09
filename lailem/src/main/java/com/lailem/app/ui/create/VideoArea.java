package com.lailem.app.ui.create;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.ui.create_old.dynamic.model.VideoModel;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by XuYang on 15/12/17.
 */
public class VideoArea extends FrameLayout {

    @Bind(R.id.autoFitView)
    View autoFitView;
    @Bind(R.id.viewFlipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.add)
    ImageView add_iv;
    @Bind(R.id.image)
    ImageView image_iv;
    @Bind(R.id.duration)
    TextView duration_tv;


    private OnChangeListener onChangeListener;

    private VideoModel model;

    public VideoArea(Context context) {
        super(context);
        init(context);
    }

    public VideoArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public VideoArea(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_create_dynamic_action_bar_video, this);
        ButterKnife.bind(this);

        FrameLayout.LayoutParams autoFileViewParams = (LayoutParams) autoFitView.getLayoutParams();
        autoFileViewParams.height = (int) (TDevice.getScreenWidth() * 0.5f);
        autoFileViewParams.width = autoFileViewParams.height;
        autoFitView.setLayoutParams(autoFileViewParams);


        FrameLayout.LayoutParams addParams = (LayoutParams) add_iv.getLayoutParams();
        addParams.height = (int) (TDevice.getScreenWidth() * 0.45f);
        addParams.width = addParams.height;
        add_iv.setLayoutParams(addParams);
    }

    @OnClick({R.id.add, R.id.image, R.id.delete})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.add:
                UIHelper.showVideoRecord((Activity) getContext());
                break;
            case R.id.image:
                if (model != null) {
                    UIHelper.showPlayVideo((Activity) getContext(), model.getContent().getFilename(), model.getContent().getPreviewPic());
                }
                break;
            case R.id.delete:
                viewFlipper.setDisplayedChild(0);
                this.model = null;
                if (onChangeListener != null) {
                    onChangeListener.onChangeListener(this, false, 0);
                }
                break;
        }
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void render(VideoModel model) {
        this.model = model;
        if (Func.checkImageTag(model.getContent().getPreviewPic(), image_iv)) {
            Glide.with(getContext()).load(model.getContent().getPreviewPic()).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(image_iv);
        }
        duration_tv.setText("时长 " + Func.formatDuration(model.getContent().getDuration()));

        viewFlipper.setDisplayedChild(1);
        if (onChangeListener != null) {
            onChangeListener.onChangeListener(this, true, 1);
        }
    }

    public VideoModel getModel() {
        return model;
    }
}
