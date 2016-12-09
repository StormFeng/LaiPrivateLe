package com.lailem.app.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.utils.TDevice;
import com.nineoldandroids.view.ViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by XuYang on 15/10/12.
 */
public class CloudTagView extends FrameLayout {
    private static final long DURATION = 800;
    @Bind(R.id.top1)
    TextView top1;
    @Bind(R.id.top2)
    TextView top2;
    @Bind(R.id.top3)
    TextView top3;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.bottom1)
    TextView bottom1;
    @Bind(R.id.bottom2)
    TextView bottom2;
    @Bind(R.id.bottom3)
    TextView bottom3;
    @Bind(R.id.bottom4)
    TextView bottom4;
    @Bind(R.id.bottom5)
    TextView bottom5;
    private ObjectAnimator centerAnim;
    private ObjectAnimator top1Anim;
    private ObjectAnimator top2Anim;
    private ObjectAnimator top3Anim;
    private ObjectAnimator bottom1Anim;
    private ObjectAnimator bottom2Anim;
    private ObjectAnimator bottom3Anim;
    private ObjectAnimator bottom4Anim;
    private ObjectAnimator bottom5Anim;

    private long distance = (long) TDevice.dpToPixel(40);


    public CloudTagView(Context context) {
        super(context);
        init(context);
    }

    public CloudTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CloudTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CloudTagView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_cloud_tag, this);
        ButterKnife.bind(this, this);

    }


    public void configTags(String[] tags) {
        center.setText(tags[0]);
        top1.setText(tags[1]);
        top2.setText(tags[2]);
        top3.setText(tags[3]);
        bottom1.setText(tags[4]);
        bottom2.setText(tags[5]);
        bottom3.setText(tags[6]);
        bottom4.setText(tags[7]);
        bottom5.setText(tags[8]);

    }

    public void start() {
        startCenterAnim();
        startTop1Anim();
        startTop2Anim();
        startTop3Anim();
        startBottom1Anim();
        startBottom2Anim();
        startBottom3Anim();
        startBottom4Anim();
        startBottom5Anim();

    }

    private void startCenterAnim() {
        if (centerAnim == null) {
            centerAnim = ObjectAnimator.ofFloat(center, "in", 1.5f, 1);
            centerAnim.setDuration(DURATION);
            centerAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(center, cVal);
                    ViewHelper.setScaleY(center, cVal);
                }
            });
        }
        if (centerAnim.isStarted()) {
            centerAnim.cancel();
        }
        centerAnim.start();
    }

    private void startTop1Anim() {
        if (top1Anim == null) {
            final float x = ViewHelper.getX(top1);
            final float y = ViewHelper.getY(top1);
            top1Anim = ObjectAnimator.ofFloat(top1, "in", 1.5f, 1);
            top1Anim.setDuration(DURATION);
            top1Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(top1, cVal);
                    ViewHelper.setScaleY(top1, cVal);
                    ViewHelper.setX(top1, x - distance * (cVal - 1));
                    ViewHelper.setY(top1, y - distance * (cVal - 1));
                }
            });
        }
        if (top1Anim.isStarted()) {
            top1Anim.cancel();
        }
        top1Anim.start();
    }

    private void startTop2Anim() {
        if (top2Anim == null) {
            final float x = ViewHelper.getX(top2);
            final float y = ViewHelper.getY(top2);
            top2Anim = ObjectAnimator.ofFloat(top2, "in", 1.5f, 1);
            top2Anim.setDuration(DURATION);
            top2Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(top2, cVal);
                    ViewHelper.setScaleY(top2, cVal);
                    ViewHelper.setX(top2, x + distance * (cVal - 1));
                    ViewHelper.setY(top2, y - distance * (cVal - 1));
                }
            });
        }
        if (top2Anim.isStarted()) {
            top2Anim.cancel();
        }
        top2Anim.start();
    }

    private void startTop3Anim() {
        if (top3Anim == null) {
            final float x = ViewHelper.getX(top3);
            final float y = ViewHelper.getY(top3);
            top3Anim = ObjectAnimator.ofFloat(top3, "in", 1.5f, 1);
            top3Anim.setDuration(DURATION);
            top3Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(top3, cVal);
                    ViewHelper.setScaleY(top3, cVal);
                    ViewHelper.setX(top3, x - distance * (cVal - 1));
                }
            });
        }
        if (top3Anim.isStarted()) {
            top3Anim.cancel();
        }
        top3Anim.start();
    }

    private void startBottom1Anim() {
        if (bottom1Anim == null) {
            final float x = ViewHelper.getX(bottom1);
            final float y = ViewHelper.getY(bottom1);
            bottom1Anim = ObjectAnimator.ofFloat(bottom1, "in", 1.5f, 1);
            bottom1Anim.setDuration(DURATION);
            bottom1Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(bottom1, cVal);
                    ViewHelper.setScaleY(bottom1, cVal);
                    ViewHelper.setX(bottom1, x - distance * (cVal - 1));
                    ViewHelper.setY(bottom1, y + distance * (cVal - 1));
                }
            });
        }
        if (bottom1Anim.isStarted()) {
            bottom1Anim.cancel();
        }
        bottom1Anim.start();
    }

    private void startBottom2Anim() {
        if (bottom2Anim == null) {
            final float x = ViewHelper.getX(bottom2);
            final float y = ViewHelper.getY(bottom2);
            bottom2Anim = ObjectAnimator.ofFloat(bottom2, "in", 1.5f, 1);
            bottom2Anim.setDuration(DURATION);
            bottom2Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(bottom2, cVal);
                    ViewHelper.setScaleY(bottom2, cVal);
                    ViewHelper.setX(bottom2, x + distance * (cVal - 1));
                    ViewHelper.setY(bottom2, y + distance * (cVal - 1));
                }
            });
        }
        if (bottom2Anim.isStarted()) {
            bottom2Anim.cancel();
        }
        bottom2Anim.start();
    }

    private void startBottom3Anim() {
        if (bottom3Anim == null) {
            final float x = ViewHelper.getX(bottom3);
            final float y = ViewHelper.getY(bottom3);
            bottom3Anim = ObjectAnimator.ofFloat(bottom3, "in", 1.5f, 1);
            bottom3Anim.setDuration(DURATION);
            bottom3Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(bottom3, cVal);
                    ViewHelper.setScaleY(bottom3, cVal);
                    ViewHelper.setY(bottom3, y + distance * (cVal - 1));
                }
            });
        }
        if (bottom3Anim.isStarted()) {
            bottom3Anim.cancel();
        }
        bottom3Anim.start();
    }

    private void startBottom4Anim() {
        if (bottom4Anim == null) {
            final float x = ViewHelper.getX(bottom4);
            final float y = ViewHelper.getY(bottom4);
            bottom4Anim = ObjectAnimator.ofFloat(bottom4, "in", 1.5f, 1);
            bottom4Anim.setDuration(DURATION);
            bottom4Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(bottom4, cVal);
                    ViewHelper.setScaleY(bottom4, cVal);
                    ViewHelper.setX(bottom4, x - distance * (cVal - 1));
                    ViewHelper.setY(bottom4, y + distance * (cVal - 1));
                }
            });
        }
        if (bottom4Anim.isStarted()) {
            bottom4Anim.cancel();
        }
        bottom4Anim.start();
    }

    private void startBottom5Anim() {
        if (bottom5Anim == null) {
            final float x = ViewHelper.getX(bottom5);
            final float y = ViewHelper.getY(bottom5);
            bottom5Anim = ObjectAnimator.ofFloat(bottom5, "in", 1.5f, 1);
            bottom5Anim.setDuration(DURATION);
            bottom5Anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (float) animation.getAnimatedValue();
                    ViewHelper.setScaleX(bottom5, cVal);
                    ViewHelper.setScaleY(bottom5, cVal);
                    ViewHelper.setX(bottom5, x + distance * (cVal - 1));
                    ViewHelper.setY(bottom5, y + distance * (cVal - 1));
                }
            });
        }
        if (bottom5Anim.isStarted()) {
            bottom5Anim.cancel();
        }
        bottom5Anim.start();
    }

}
