package com.lailem.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lailem.app.utils.TDevice;
import com.socks.library.KLog;

/**
 * Created by XuYang on 15/12/23.
 */
public class ControlImageView extends ImageView {

    private Paint paint;
    private Context context;
    private int arcColor = Color.parseColor("#9FE56032");
    private int pointColor = Color.parseColor("#E56032");
    private long duration;
    private Handler handler;
    private Runnable runnable;
    private long fps = 10;
    private float pointRadius = 2;

    private boolean isAnim = false;

    private long startTime;

    public ControlImageView(Context context) {
        super(context);
        init(context);
    }

    public ControlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ControlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ControlImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        pointRadius = TDevice.dpToPixel(2);

        paint = new Paint();
        paint.setAntiAlias(true);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                postInvalidate();
                handler.postDelayed(runnable, fps);
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        if (isAnim) {
            long time = System.currentTimeMillis() - startTime;
            KLog.e("time=" + time);
            float degree = (time * 360f / duration);
            KLog.e("degree=" + degree);
            if (width != 0 && height != 0) {
                canvas.save();
                //画弧
                paint.setColor(arcColor);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(pointRadius * 2 * 3 / 4);
                float left = pointRadius;
                float top = pointRadius;
                float right = width - pointRadius;
                float bottom = height - pointRadius;
                RectF rect = new RectF(left, top, right, bottom);
                canvas.drawArc(rect, -90, degree, false, paint);
                canvas.save();
                //画点
                paint.setColor(pointColor);
                paint.setStyle(Paint.Style.FILL);
                canvas.rotate(degree + 90, width / 2, height / 2);
                canvas.drawCircle(pointRadius, width / 2, pointRadius, paint);
                canvas.restore();
            }
            if (degree >= 360) {
                stop();
            }
        }
    }

    public void start(long duration) {
        this.duration = duration;
        isAnim = true;
        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable, fps);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
        isAnim = false;
        startTime = 0;
        postInvalidate();
    }
}
