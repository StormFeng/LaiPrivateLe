package com.lailem.app.widget.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.base.BaseListAdapter;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.Const;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicVoiceView extends LinearLayout {
    @Bind(R.id.content)
    TextView content_tv;
    @Bind(R.id.duration)
    TextView duration_tv;
    @Bind(R.id.playProgress)
    ImageView playProgress_iv;

    private String voiceUrl;

    private Animation anim;
    private BaseListAdapter adapter;
    private int position;

    public DynamicVoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DynamicVoiceView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_dynamic_voice, this);
        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.playVoice)
    public void playVoice() {
        int playIndex = -1;
        Object obj = adapter.getTag(Const.INDEX_VOICE_PLAYING);
        if (obj != null) {
            playIndex = (int) obj;
        }
        if (playIndex == position) {
            AudioPlayUtil.getInstance().stop();
            adapter.putTag(Const.INDEX_VOICE_PLAYING, -1);
            adapter.notifyDataSetChanged();
        } else {
            AudioPlayUtil.getInstance().start(voiceUrl, String.valueOf(position), this, new OnAudioPlayListener() {
                @Override
                public void onAudioPlayEnd(String tag, String path) {
                    adapter.putTag(Const.INDEX_VOICE_PLAYING, -1);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onAudioException() {
                    adapter.putTag(Const.INDEX_VOICE_PLAYING, -1);
                    adapter.notifyDataSetChanged();
                }
            });
            adapter.putTag(Const.INDEX_VOICE_PLAYING, position);
            adapter.notifyDataSetChanged();
        }
    }

    public void render(BaseListAdapter adapter, String duration, String content, String voiceUrl, int position) {
        duration_tv.setText(duration + "\"");
        content_tv.setText(content);
        this.voiceUrl = voiceUrl;
        this.adapter = adapter;
        this.position = position;

        int playIndex = -1;
        Object obj = adapter.getTag(Const.INDEX_VOICE_PLAYING);
        if (obj != null) {
            playIndex = (int) obj;
        }
        if (playIndex == position) {
            startPalyAnim();
        } else {
            stopPalyAnim();
        }
    }

    public void stop() {
        AudioPlayUtil.getInstance().stop();
    }

    public void startPalyAnim() {
        playProgress_iv.setVisibility(VISIBLE);
        if (anim == null) {
            anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            anim.setInterpolator(new LinearInterpolator());
        }
        if (anim.hasStarted()) {
            anim.reset();
        }
        playProgress_iv.startAnimation(anim);
    }

    public void stopPalyAnim() {
        if (anim != null) {
            anim.cancel();
        }
        playProgress_iv.clearAnimation();
        playProgress_iv.setVisibility(INVISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

}
