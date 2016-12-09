package com.lailem.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lailem.app.R;
import com.lailem.app.utils.TDevice;

import java.util.Random;

/**
 * Created by XuYang on 15/12/23.
 */
public class VoiceVisulaImageView extends ImageView {

    public static final int STATE_NONE = 0;
    public static final int STATE_ANIM = 1;

    private Context context;

    private Bitmap noneBit;

    private final int totalCount = 24;

    private int state = STATE_NONE;

    private Paint paint;

    private Random random;

    private float gap = 10;

    private Handler handler;
    private Runnable runnable;
    private final int fps = 200;

    public VoiceVisulaImageView(Context context) {
        super(context);
        init(context);
    }

    public VoiceVisulaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceVisulaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public VoiceVisulaImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        noneBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_c_d_voice_visual_none);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#E56032"));

        random = new Random();

        gap = TDevice.dpToPixel(5f);

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
        int width = getWidth();
        int height = getHeight();
        if (width != 0 && height != 0) {
            int bW = noneBit.getWidth();
            int bH = noneBit.getHeight();
            if (state == STATE_ANIM) {
                for (int i = 0; i < totalCount; i++) {
                    int x = random.nextInt(bH - 3) + 3;
                    float left = i * (gap + bW);
                    float top = height - x;
                    float right = left + bW;
                    float bottom = height;
                    canvas.drawRect(left, top, right, bottom, paint);
                }
            } else {
                for (int i = 0; i < totalCount; i++) {
                    float left = i * (gap + bW);
                    float top = height - bH;
                    canvas.drawBitmap(noneBit, left, top, paint);
                }
            }
        }
    }

    public void start() {
        state = STATE_ANIM;
        handler.post(runnable);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
        state = STATE_NONE;
        postInvalidate();
    }
}
