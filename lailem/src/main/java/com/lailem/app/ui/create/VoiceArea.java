package com.lailem.app.ui.create;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.listener.OnAudioRecordListener;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.AudioRecordUtils;
import com.lailem.app.utils.Func;
import com.lailem.app.widget.ControlImageView;
import com.lailem.app.widget.VoiceVisulaImageView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by XuYang on 15/12/17.
 */
public class VoiceArea extends FrameLayout implements OnAudioRecordListener, View.OnTouchListener, OnAudioPlayListener {

    public static final int STATE_READY_RECORD = 1;//待录制
    public static final int STATE_RECORDING = 2;//录制中
    public static final int STATE_READY_PLAY = 3;//待播放
    public static final int STATE_PLAYING = 4;//播放中

    @Bind(R.id.voiceVisual)
    VoiceVisulaImageView voiceVisual;
    @Bind(R.id.time)
    TextView time_tv;
    @Bind(R.id.controlArea)
    View controlArea;
    @Bind(R.id.control)
    ControlImageView control;
    @Bind(R.id.retryArea)
    View retryArea;
    @Bind(R.id.tip)
    TextView tip_tv;

    private int state = STATE_READY_RECORD;

    private AudioRecordUtils recordUtil;

    private int time;
    private String path;

    private OnChangeListener onChangeListener;

    private VoiceModel model;

    public VoiceArea(Context context) {
        super(context);
        init(context);
    }

    public VoiceArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceArea(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public VoiceArea(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_create_dynamic_action_bar_voice, this);
        ButterKnife.bind(this);

        control.setOnTouchListener(this);
        updateState(STATE_READY_RECORD);

    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public VoiceModel getModel() {
        //检查语音文件
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            return null;
        }
        model = new VoiceModel();
        model.getContent().setDuration(time + "");
        model.getContent().setFilename(path);
        model.getContent().setText("");
        return model;
    }

    /**
     * 点击控制view
     */
    @OnClick(R.id.control)
    public void clickControl() {
        if (state == STATE_READY_PLAY) {
            updateState(STATE_PLAYING);
            AudioPlayUtil.getInstance().start(path, path, this);
        } else if (state == STATE_PLAYING) {
            updateState(STATE_READY_PLAY);
            AudioPlayUtil.getInstance().stop();
        }
    }

    /**
     * 长按控制view
     *
     * @return
     */
    @OnLongClick(R.id.control)
    public boolean longClickControl() {
        if (state == STATE_READY_RECORD) {
            //开始录制
            if (recordUtil == null) {
                recordUtil = new AudioRecordUtils();
                recordUtil.setMaxDuration(60);
                recordUtil.setUpdatePeriod(100);
                recordUtil.setOnSoundRecordListener(this);
            }

            recordUtil.start(getContext());
        }
        return true;
    }

    /**
     * 更改视图状态
     *
     * @param state
     */
    private void updateState(int state) {
        this.state = state;
        switch (state) {
            case STATE_READY_RECORD:
                //待录制
                retryArea.setVisibility(GONE);
                control.setImageResource(R.drawable.ic_adddynamic_voice_micro);
                tip_tv.setText("按住即可录音");
                voiceVisual.stop();
                control.stop();
                break;
            case STATE_RECORDING:
                //录制中
                retryArea.setVisibility(GONE);
                control.setImageResource(R.drawable.ic_adddynamic_voice_recording);
                tip_tv.setText("松开停止录音");
                voiceVisual.start();
                control.stop();
                break;
            case STATE_READY_PLAY:
                //待播放
                retryArea.setVisibility(VISIBLE);
                control.setImageResource(R.drawable.ic_adddynamic_voice_play);
                tip_tv.setText("点击播放录音");
                voiceVisual.stop();
                control.stop();
                break;
            case STATE_PLAYING:
                //播放中
                retryArea.setVisibility(VISIBLE);
                control.setImageResource(R.drawable.ic_adddynamic_voice_playing);
                tip_tv.setText("点击播放录音");
                voiceVisual.stop();
                control.start(time * 1000);
                break;
        }
    }

    /**
     * 重置
     */
    private void reset() {
        this.path = "";
        this.model = null;
        updateTime(0);
        updateState(STATE_READY_RECORD);
        if (recordUtil.isRecording()) {
            recordUtil.stop();
        }
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }

        if (onChangeListener != null) {
            onChangeListener.onChangeListener(this, false, 0);
        }
    }

    /**
     * 更新录制时间
     *
     * @param time
     */
    private void updateTime(int time) {
        this.time = time;
        time_tv.setText(Func.formatDuration(time + "") + "");
    }

    @Override
    public void onAudioRecordStart() {
        updateState(STATE_RECORDING);
    }

    @Override
    public void onAudioRecordDb(double db) {

    }

    @Override
    public void onAudioRecordEnd(String path, int time) {
        this.path = path;
        this.time = time;
        updateState(STATE_READY_PLAY);
        updateTime(time);
        if (onChangeListener != null) {
            onChangeListener.onChangeListener(this, true, 1);
        }
    }

    @Override
    public void onAudioRecordTime(int time) {
        updateTime(time);
    }

    @Override
    public void onAudioRecordCancel() {
        reset();
    }

    @Override
    public void onAudioPlayEnd(String tag, String path) {
        this.state = STATE_READY_PLAY;
        retryArea.setVisibility(VISIBLE);
        control.setImageResource(R.drawable.ic_adddynamic_voice_play);
        tip_tv.setText("点击播放录音");
        voiceVisual.stop();
    }

    @Override
    public void onAudioException() {
        this.state = STATE_READY_PLAY;
        retryArea.setVisibility(VISIBLE);
        control.setImageResource(R.drawable.ic_adddynamic_voice_play);
        tip_tv.setText("点击播放录音");
        voiceVisual.stop();
        AppContext.showToast("播放失败");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (state == STATE_RECORDING) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_UP:
                    recordUtil.stop();
                    break;
            }
        }
        return false;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (recordUtil != null) {
            recordUtil.stop();
        }
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
    }

    /**
     * 点击重录
     */
    @OnClick(R.id.retry_iv)
    public void clickReRecord() {
        reset();
    }
}
