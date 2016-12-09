package com.lailem.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by XuYang on 15/11/27.
 */
public class ArrowOnlineImageView extends ImageView {


    public ArrowOnlineImageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
    }

    public ArrowOnlineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArrowOnlineImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArrowOnlineImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
