package com.lailem.app.tpl;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.bean.ObjectWrapper;
import com.lailem.app.jsonbean.activegroup.GroupDatabaseBean;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.TDevice;

import java.util.ArrayList;

import butterknife.Bind;

public class DataBankVoiceTpl extends BaseTpl<ObjectWrapper> {
    public static final String PLAYING_VIEW = "playing_view";

    @Bind(R.id.voiceLayout)
    LinearLayout imageLayout;
    private Animation anim;

    public DataBankVoiceTpl(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_data_bank_voice;
    }

    @Override
    protected void initView() {
        super.initView();
        int width = (int) ((TDevice.getScreenWidth() - TDevice.dpToPixel(40)) / 3);
        int height = width;
        for (int i = 0; i < imageLayout.getChildCount(); i++) {
            FrameLayout fl = (FrameLayout) imageLayout.getChildAt(i);
            ImageView iv = (ImageView) fl.getChildAt(0);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv.getLayoutParams();
            params.width = width;
            params.height = height;
            iv.setLayoutParams(params);
        }
    }

    @Override
    public void setBean(ObjectWrapper wrapper, final int position) {
        ArrayList<GroupDatabaseBean.Data> bean = (ArrayList<GroupDatabaseBean.Data>) wrapper.getObject();
        for (int i = 0; i < imageLayout.getChildCount(); i++) {
            FrameLayout fl = (FrameLayout) imageLayout.getChildAt(i);
            if (i < bean.size()) {
                final GroupDatabaseBean.Data itemData = bean.get(i);
                fl.setVisibility(VISIBLE);
                ImageView playVoice_iv = (ImageView) fl.findViewById(R.id.playVoice);
                final ImageView playProgress_iv = (ImageView) fl.findViewById(R.id.playProgress);
                TextView name_tv = (TextView) fl.findViewById(R.id.name);
                TextView duration_tv = (TextView) fl.findViewById(R.id.duration);

                duration_tv.setText(itemData.getDuration() + "\"");
                name_tv.setText(itemData.getNickname());

                final int index = i;

                //判断是否执行播放动画
                View playView = (View) adapter.getTag(PLAYING_VIEW);
                if (adapter.getCheckedPosition() == position && playView != null && playProgress_iv.equals(playView)) {
                    startPalyAnim(playProgress_iv);
                } else {
                    stopPalyAnim(playProgress_iv);
                }

                fl.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getCheckedPosition() == position && playProgress_iv == adapter.getTag(PLAYING_VIEW)) {
                            AudioPlayUtil.getInstance().stop();
                            stopPalyAnim(playProgress_iv);
                        } else {
                            if (adapter.getTag(PLAYING_VIEW) != null) {
                                stopPalyAnim((ImageView) adapter.getTag(PLAYING_VIEW));
                            }
                            adapter.putTag(PLAYING_VIEW, playProgress_iv);
                            adapter.setCheckedPosition(position);
                            startPalyAnim(playProgress_iv);
                            AudioPlayUtil.getInstance().start(ApiClient.getFileUrl(itemData.getFilename()), PLAYING_VIEW, new OnAudioPlayListener() {
                                @Override
                                public void onAudioPlayEnd(String tag, String path) {
                                    adapter.removeTag(PLAYING_VIEW);
                                    adapter.setCheckedPosition(-1);
                                    stopPalyAnim(playProgress_iv);
                                }

                                @Override
                                public void onAudioException() {
                                    adapter.removeTag(PLAYING_VIEW);
                                    adapter.setCheckedPosition(-1);
                                    stopPalyAnim(playProgress_iv);
                                    AppContext.showToast("播放失败");
                                }
                            });
                        }
                    }
                });
            } else {
                fl.setVisibility(INVISIBLE);
            }
        }
    }


    private void startPalyAnim(ImageView playProgress_iv) {
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

    private void stopPalyAnim(ImageView playProgress_iv) {
        if (anim != null) {
            anim.cancel();
        }
        playProgress_iv.clearAnimation();
        playProgress_iv.setVisibility(INVISIBLE);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
    }

}
