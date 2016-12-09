package com.lailem.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lailem.app.R;

public class ImageViewFit extends ImageView {

    private float proportion = 0f;

    public ImageViewFit(Context context) {
        super(context);
    }

    public ImageViewFit(Context context, float proportion) {
        super(context);
        this.proportion = proportion;
    }

    public ImageViewFit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewFit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewFit, defStyle, 0);
        proportion = a.getFloat(R.styleable.ImageViewFit_proportion, 0);
        a.recycle();
    }

    public ImageViewFit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (proportion != 0) {
            setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth() * proportion));
        }

    }

}
