package com.lailem.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.lailem.app.R;

public class CircleProgressView extends ImageView {

    private static final long DEFAULT_DURATION = 1000*20;
    private static final float DEFAULT_START_ANGLE = 270;

	private Animation animation;
	private Paint progressPaint;
	protected boolean isReady;

	private RectF rect;
    private float mDrawAngle;
    private long duration = DEFAULT_DURATION;
    private float startAngle = DEFAULT_START_ANGLE;
    private int lineColor = R.color.orange;
    private float lineWidth = 4;
    private Paint ovalPaint;

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CircleProgressView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		progressPaint.setColor(getResources().getColor(lineColor));
		progressPaint.setStyle(Style.STROKE);
        progressPaint.setStrokeWidth(lineWidth);

        ovalPaint = new Paint();
        ovalPaint.setAntiAlias(true);
        ovalPaint.setColor(getResources().getColor(lineColor));
        ovalPaint.setStyle(Style.FILL);

        post(new Runnable() {

            @Override
            public void run() {
                rect = new RectF();
                rect.left = getLeft() + lineWidth*2;
                rect.right = getRight() - lineWidth*2;
                rect.top = getTop() + lineWidth*2;
                rect.bottom = getBottom() - lineWidth*2;

                isReady = true;
            }
        });
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isReady) {
			canvas.drawArc(rect, startAngle, mDrawAngle, false, progressPaint);
            canvas.save();
            canvas.rotate(mDrawAngle,getWidth()/2,getHeight()/2);
            canvas.drawCircle(getWidth() / 2, lineWidth*2, lineWidth+lineWidth/2, ovalPaint);
            canvas.restore();
		}
		super.onDraw(canvas);
	}

    public void start() {
        reset();
        if (animation == null) {
            animation = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if(interpolatedTime > 0) {
                        mDrawAngle = interpolatedTime * 360;
                        invalidate();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return false;
                }
            };
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());
        }
        if(animation.hasStarted()){
            animation.reset();
        }
        startAnimation(animation);
    }

    public void reset() {
        stop();
        mDrawAngle = 0;
        invalidate();
    }

    public void stop() {
        if(animation!=null){
            animation.cancel();
        }
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
