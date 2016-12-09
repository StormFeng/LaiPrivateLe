package com.lailem.app.ui.create_old.dynamic;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.listener.OnAudioRecordListener;
import com.lailem.app.ui.create_old.dynamic.model.VoiceModel;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.AudioRecordUtils;
import com.lailem.app.utils.Func;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CreateDynamicVoiceActivity extends BaseActivity implements OnAudioRecordListener, View.OnTouchListener, OnAudioPlayListener {
    public static final int REQUEST_VOICE = 2002;
    public static final String BUNDLE_KEY_VOICE = "voice";

    public static final int STATE_READY_RECORD = 1;//待录制
    public static final int STATE_RECORDING = 2;//录制中
    public static final int STATE_READY_PLAY = 3;//带播放
    public static final int STATE_PLAYING = 4;//播放中

    @Bind(R.id.topbar)
    TopBarView topbar;
    //控制view
    @Bind(R.id.control)
    View controlView;
    //话筒
    @Bind(R.id.micro)
    ImageView micro_iv;
    //静止圆环
    @Bind(R.id.circle)
    ImageView circle_iv;
    //旋转圆环
    @Bind(R.id.loading)
    ImageView loading_iv;
    //播放
    @Bind(R.id.play)
    TextView play_tv;
    //重录
    @Bind(R.id.reRecord)
    TextView reRecord_tv;
    //时间
    @Bind(R.id.time)
    TextView time_tv;
    //识别的文本
    @Bind(R.id.content)
    EditText content_tv;

    private int state = STATE_READY_RECORD;

    private Animation animation;

    private AudioRecordUtils recordUtil;

    private int time;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dynamic_voice);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topbar.setTitle("发表录音").setLeftImageButton(R.drawable.ic_topbar_close, UIHelper.finish(this)).setRightText("完成", new OnClickListener() {

            @Override
            public void onClick(View v) {
                submit();
            }
        }).getRight_tv().setEnabled(false);

        controlView.setOnTouchListener(this);
        updateState(STATE_READY_RECORD);
    }

    private void submit() {
        //检查语音文件
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            AppContext.showToast("录音文件不存在，请重新录制");
            return;
        }
        VoiceModel voiceModel = new VoiceModel();
        voiceModel.getContent().setDuration(time + "");
        voiceModel.getContent().setFilename(path);
        voiceModel.getContent().setText(content_tv.getText().toString().trim());
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_VOICE, voiceModel);
        setResult(RESULT_OK, bundle);
        finish();
    }

    @OnClick(R.id.clear)
    public void clear() {
        content_tv.setText("");
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

            recordUtil.start(this);
        }
        return true;
    }

    /**
     * 点击重录
     */
    @OnClick(R.id.reRecord)
    public void clickReRecord() {
        reset();
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
                micro_iv.setVisibility(View.VISIBLE);
                circle_iv.setVisibility(View.VISIBLE);
                stopLoadingAnim();
                loading_iv.setVisibility(View.GONE);
                play_tv.setVisibility(View.GONE);
                reRecord_tv.setVisibility(View.GONE);
                topbar.getRight_tv().setEnabled(false);
                break;
            case STATE_RECORDING:
                micro_iv.setVisibility(View.VISIBLE);
                circle_iv.setVisibility(View.GONE);
                loading_iv.setVisibility(View.VISIBLE);
                startLoadingAnim();
                play_tv.setVisibility(View.GONE);
                reRecord_tv.setVisibility(View.GONE);
                topbar.getRight_tv().setEnabled(false);
                break;
            case STATE_READY_PLAY:
                micro_iv.setVisibility(View.GONE);
                circle_iv.setVisibility(View.VISIBLE);
                stopLoadingAnim();
                loading_iv.setVisibility(View.GONE);
                play_tv.setVisibility(View.VISIBLE);
                play_tv.setText("立即播放");
                reRecord_tv.setVisibility(View.VISIBLE);
                topbar.getRight_tv().setEnabled(true);
                break;
            case STATE_PLAYING:
                micro_iv.setVisibility(View.GONE);
                circle_iv.setVisibility(View.GONE);
                loading_iv.setVisibility(View.VISIBLE);
                startLoadingAnim();
                play_tv.setVisibility(View.VISIBLE);
                play_tv.setText("播放中...");
                reRecord_tv.setVisibility(View.VISIBLE);
                topbar.getRight_tv().setEnabled(false);
                break;
        }
    }

    /**
     * 更新录制时间
     *
     * @param time
     */
    private void updateTime(int time) {
        this.time = time;
        time_tv.setText(Func.formatDuration(time + ""));
    }

    /**
     * 执行录制中或播放中动画
     */
    private void startLoadingAnim() {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
            animation.setInterpolator(new LinearInterpolator());
        }
        if (animation.hasStarted()) {
            animation.reset();
        }
        loading_iv.startAnimation(animation);
    }

    /**
     * 停止录制中或播放中动画
     */
    private void stopLoadingAnim() {
        if (animation != null) {
            animation.cancel();
            loading_iv.clearAnimation();
        }
    }

    /**
     * 重置
     */
    private void reset() {
        this.path = "";
        updateTime(0);
        updateState(STATE_READY_RECORD);
        if (recordUtil.isRecording()) {
            recordUtil.stop();
        }
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
        content_tv.setText("");
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
    }

    @Override
    public void onAudioException() {
        updateState(STATE_READY_PLAY);
        updateTime(time);
        AppContext.showToast("播放失败");
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
        updateState(STATE_READY_PLAY);
    }

    @Override
    protected void onDestroy() {
        if (recordUtil != null) {
            recordUtil.stop();
        }
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
        super.onDestroy();
    }
}
