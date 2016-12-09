package com.lailem.app.widget.dynamic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lailem.app.R;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.TDevice;
import com.lailem.app.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DynamicVideoView extends FrameLayout {
    @Bind(R.id.videoPreview)
    ImageView videoPreview_iv;
    private String previewImageUrl;
    private String videoUrl;

    public DynamicVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicVideoView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_dynamic_video, this);
        ButterKnife.bind(this, this);
//        videoPreview_iv.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Drawable drawable = videoPreview_iv.getDrawable();
//                        if (drawable != null) {
//                            drawable.mutate().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        Drawable drawableUp = videoPreview_iv.getDrawable();
//                        if (drawableUp != null) {
//                            drawableUp.mutate().clearColorFilter();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });

    }

    @OnClick(R.id.videoPreview)
    public void playVideo() {
        UIHelper.showPlayVideo((Activity) getContext(), videoUrl, previewImageUrl);
    }

    public void render(String previewImageUrl, String videoUrl) {
        this.previewImageUrl = previewImageUrl;
        this.videoUrl = videoUrl;
        if (previewImageUrl != null && previewImageUrl.startsWith("file://")) {
            if (Func.checkImageTag(previewImageUrl.replace("file://", ""), videoPreview_iv)) {
                Glide.with(getContext()).load(previewImageUrl.replace("file://", "")).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(videoPreview_iv);
            }
        } else {
            if (Func.checkImageTag(previewImageUrl, videoPreview_iv)) {
                Glide.with(getContext()).load(previewImageUrl).placeholder(R.drawable.empty).error(R.drawable.empty).bitmapTransform(new CenterCrop(getContext()), new RoundedCornersTransformation(getContext(), (int) TDevice.dpToPixel(4f), 0, RoundedCornersTransformation.CornerType.ALL)).into(videoPreview_iv);
            }
        }
    }

}
