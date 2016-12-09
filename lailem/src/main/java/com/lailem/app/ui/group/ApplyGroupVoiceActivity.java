package com.lailem.app.ui.group;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.bean.Result;
import com.lailem.app.listener.OnAudioPlayListener;
import com.lailem.app.listener.OnAudioRecordListener;
import com.lailem.app.utils.AudioPlayUtil;
import com.lailem.app.utils.AudioRecordUtils;
import com.lailem.app.utils.Const;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.CircleProgressView;
import com.lailem.app.widget.TopBarView;
import com.socks.library.KLog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ApplyGroupVoiceActivity extends BaseActivity implements View.OnTouchListener, OnAudioRecordListener, OnAudioPlayListener {
    public static final int DURATION = 10 * 1000;//录音时间
    public static final int STATE_RECORD = 1;
    public static final int STATE_PLAY = 2;

    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.reRecord)
    TextView reRecord_tv;
    @Bind(R.id.submit)
    TextView submit_tv;
    @Bind(R.id.tip)
    TextView tip_tv;
    @Bind(R.id.progressView)
    CircleProgressView progressView;
    private Rect rect;
    private AudioRecordUtils recordUtils;
    private AudioRecordUtils recordUtilsForPermission;
    private int time;
    private String path;
    private int state = STATE_RECORD;
    private int dbCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_group_voice);
        ButterKnife.bind(this);
        initView();
        if (recordUtilsForPermission == null) {
            recordUtilsForPermission = new AudioRecordUtils();
            recordUtilsForPermission.setMaxDuration(DURATION);
            recordUtilsForPermission.setOnSoundRecordListener(new OnAudioRecordListener() {
                @Override
                public void onAudioRecordStart() {

                }

                @Override
                public void onAudioRecordDb(double db) {

                }

                @Override
                public void onAudioRecordEnd(String path, int time) {

                }

                @Override
                public void onAudioRecordTime(int time) {

                }

                @Override
                public void onAudioRecordCancel() {

                }
            });
        }
        recordUtilsForPermission.start(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordUtilsForPermission.stop();
            }
        }, 1000);
    }

    private void initView() {
        topbar.setTitle("申请加入群组").setBack("返回", R.drawable.ic_topbar_back, UIHelper.finish(this));
        showNormalTip();

        progressView.setDuration(DURATION);
        //初始化提示控件
        tip_tv.setOnTouchListener(this);
        rect = new Rect();
        tip_tv.post(new Runnable() {
            @Override
            public void run() {
                tip_tv.getDrawingRect(rect);
            }
        });
    }

    @OnClick(R.id.submit)
    public void clickSubmit() {
        if (!ac.isLogin()) {
            UIHelper.showLogin(_activity, false);
            return;
        }
        if (TextUtils.isEmpty(path)) {
            AppContext.showToast("录音文件不存在，请重新录制");
            return;
        }
        File applyVoice = new File(path);
        ApiClient.getApi().apply(this, ac.getLoginUid(), _Bundle.getString(Const.BUNDLE_KEY_GROUP_ID), null, applyVoice, time + "", null, null);
    }

    /**
     * 点击重录
     */
    @OnClick(R.id.reRecord)
    public void clickReRecord() {
        //停止播放
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
        //重置变量
        this.path = null;
        this.state = STATE_RECORD;
        //切换视图
        progressView.reset();
        showNormalTip();
        submit_tv.setEnabled(false);
        reRecord_tv.setVisibility(View.INVISIBLE);
    }

    @OnLongClick(R.id.tip)
    public boolean longClickTip() {
        KLog.i("longClickTip");
        //录制状态并没有文件在播放
        if (state == STATE_RECORD && !AudioPlayUtil.getInstance().isPlaying()) {
            if (recordUtils == null) {
                recordUtils = new AudioRecordUtils();
                recordUtils.setMaxDuration(DURATION);
                recordUtils.setOnSoundRecordListener(this);
            }
            recordUtils.start(this);
        }
        return true;
    }

    @OnClick(R.id.tip)
    public void clickTip() {
        KLog.i("clickTip");
        //播放
        if (state == STATE_PLAY) {
            if (TextUtils.isEmpty(path) || !new File(path).exists()) {
                AppContext.showToast("录音文件不存在，请重新录制");
                return;
            }
            if (AudioPlayUtil.getInstance().isPlaying()) {
                return;
            }
            AudioPlayUtil.getInstance().start(path, path, this);
            //开始播放
            showPlayingTip();
            progressView.start();
        }
    }

    @Override
    public void onApiStart(String tag) {
        super.onApiStart(tag);
        showWaitDialog();
    }

    @Override
    public void onApiSuccess(Result res, String tag) {
        super.onApiSuccess(res, tag);
        hideWaitDialog();
        if (res.isOK()) {
            AppContext.showToast("申请提交成功");
            finish();
        } else {
            if ("waitingForAudit".equals(res.errorCode)) {
                finish();
            }
            ac.handleErrorCode(this, res.errorCode, res.errorInfo);

        }
    }

    @Override
    protected void onApiError(String tag) {
        super.onApiError(tag);
        hideWaitDialog();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (state == STATE_RECORD && recordUtils != null && recordUtils.isRecording()) {
            float x = event.getX();
            float y = event.getY();
            boolean in = rect.contains((int) x, (int) y);
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    if (in) {
                        showRecordingTip();
                    } else {
                        showCancelRecordTip();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (in) {
                        recordUtils.stop();
                    } else {
                        recordUtils.cancel();
                    }
                    break;
            }
        }
        return false;
    }


    @Override
    public void onAudioRecordStart() {
        KLog.i("Start");
        //录制开始
        progressView.start();

        showRecordingTip();
        state = STATE_RECORD;
        this.path = null;
        submit_tv.setEnabled(false);
        reRecord_tv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAudioRecordDb(double db) {
    }

    @Override
    public void onAudioRecordEnd(String path, int time) {
        this.time = time;
        KLog.i("End");
        //录制完成
        progressView.stop();

        showRecordEndTip();
        submit_tv.setEnabled(true);
        reRecord_tv.setVisibility(View.VISIBLE);
        this.path = path;
        state = STATE_PLAY;
    }

    @Override
    public void onAudioRecordTime(int time) {
        //更新时间
        this.time = time;
    }

    @Override
    public void onAudioRecordCancel() {
        KLog.i("cancel");
        //取消录制
        progressView.reset();

        showNormalTip();
        this.path = null;
        submit_tv.setEnabled(false);
    }


    /**
     * 播放结束
     *
     * @param tag
     * @param path
     */
    @Override
    public void onAudioPlayEnd(String tag, String path) {
        KLog.i("playEnd");
        //播放完成
        progressView.stop();
        showRecordEndTip();
    }

    @Override
    public void onAudioException() {
        progressView.stop();
        showRecordEndTip();
        AppContext.showToast("播放失败");
    }

    /**
     * 显示录音中文字提示
     */
    private void showRecordingTip() {
        tip_tv.setText("录音...");
        tip_tv.setTextColor(getResources().getColor(R.color.orange));
    }

    /**
     * 显示录音完成文字提示
     */
    private void showRecordEndTip() {
        tip_tv.setText("播放");
        tip_tv.setTextColor(getResources().getColor(R.color.orange));
    }

    /**
     * 显示录音完成文字提示
     */
    private void showPlayingTip() {
        tip_tv.setText("播放中...");
        tip_tv.setTextColor(getResources().getColor(R.color.orange));
    }

    /**
     * 显示未开始录制文字提示
     */
    private void showNormalTip() {
        tip_tv.setText("按住即录");
        tip_tv.setTextColor(getResources().getColor(R.color.text_medium_2));
    }

    /**
     * 显示松手取消录制文字提示
     */
    private void showCancelRecordTip() {
        tip_tv.setText("松手取消录制");
        tip_tv.setTextColor(getResources().getColor(R.color.text_medium_2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AudioPlayUtil.getInstance().isPlaying()) {
            AudioPlayUtil.getInstance().stop();
        }
    }
}
