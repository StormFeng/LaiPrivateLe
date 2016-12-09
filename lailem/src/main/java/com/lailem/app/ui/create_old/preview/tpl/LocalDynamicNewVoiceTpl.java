package com.lailem.app.ui.create_old.preview.tpl;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lailem.app.R;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.tpl.BaseTpl;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.Const;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by XuYang on 15/12/21.
 */
public class LocalDynamicNewVoiceTpl extends BaseTpl<ObjectWrapper> {
    @Bind(R.id.voiceVisual)
    ImageView voiceVisual_iv;
    @Bind(R.id.duration)
    TextView duration_tv;

    private AnimationDrawable ad;
    private int position;
    private VoiceModel bean;

    public LocalDynamicNewVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_group_home_new_voice;
    }


    @Override
    public void setBean(ObjectWrapper wrapper, int position) {
        this.position = position;
        this.bean = (VoiceModel) wrapper.getObject();
        duration_tv.setText(bean.getContent().getDuration() + "\"");
        int playIndex = -1;
        Object obj = adapter.getTag(Const.INDEX_VOICE_PLAYING);
        if (obj != null) {
            playIndex = (int) obj;
        }
        if (playIndex == position) {
            voiceVisual_iv.setImageResource(R.drawable.ic_dynamic_voice_visual);
            ad = (AnimationDrawable) voiceVisual_iv.getDrawable();
            ad.start();
        } else {
            voiceVisual_iv.setImageResource(R.drawable.ic_dynamic_voice_visual_three);
        }
    }

    @OnClick(R.id.voiceArea)
    public void clickVoiceArea() {
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
            AudioPlayUtil.getInstance().start(bean.getContent().getFilename(), String.valueOf(position), this, new OnAudioPlayListener() {
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AudioPlayUtil.getInstance().stop();
    }

}
