package com.lailem.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.lailem.app.R;

public class ChatRightImageView extends ImageViewFit {
    Rect rect;
    NinePatch ninePatch;

    public ChatRightImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ChatRightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ChatRightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatRightImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_chat_right_empty);
        ninePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() == null)
            return;
        rect.left = getLeft();
        rect.right = getRight();
        rect.bottom = getBottom();
        rect.top = getTop();
        ninePatch.draw(canvas, rect);
    }


}
