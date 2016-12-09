package com.lailem.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.socks.library.KLog;

/**
 * Created by XuYang on 15/4/22.
 */
public class ImageViewTest extends ImageView implements GestureDetector.OnGestureListener {

    private GestureDetector dragDetector;
    private GestureDetector detector;
    private GestureDetector.OnGestureListener onGestureListener;
    private OnClickListener onClickListener;

    public ImageViewTest(Context context) {
        super(context);
        init(context);
    }

    public ImageViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        detector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void setDetector(GestureDetector detector) {
        this.dragDetector = detector;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        KLog.i("Down");
        if (onGestureListener != null) {
            onGestureListener.onDown(e);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        KLog.i("onShowPress");
        if (onGestureListener != null) {
            onGestureListener.onShowPress(e);
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        KLog.i("onSingleTapUp");
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
        if (onGestureListener != null) {
            onGestureListener.onSingleTapUp(e);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        KLog.i("onScroll");
        if (onGestureListener != null) {
            onGestureListener.onScroll(e1, e2, distanceX, distanceY);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        KLog.i("onLongPress");
        if (onGestureListener != null) {
            onGestureListener.onLongPress(e);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        KLog.i("onFling");
        if (onGestureListener != null) {
            onGestureListener.onFling(e1, e2, velocityX, velocityY);
        }
        return false;
    }

    public void setDelegateGestureListener(GestureDetector.OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.onClickListener = l;
    }
}
