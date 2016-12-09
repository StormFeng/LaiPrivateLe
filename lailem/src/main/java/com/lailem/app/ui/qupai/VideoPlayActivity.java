package com.lailem.app.ui.qupai;

import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.api.ApiClient;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.FileUtils;
import com.midian.fastdevelop.afinal.http.AjaxCallBack;
import com.socks.library.KLog;

import java.io.File;

public class VideoPlayActivity extends BaseActivity {
    private SurfaceView sv;
    private ProgressBar progressBar;
    private Button btn_play;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int currentPosition = 0;
    FrameLayout container;
    private boolean isPlaying;
    String videoPath;
    String previewImagePath;

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();

        setContentView(R.layout.activity_video_play);
        setTranslucentStatus(true, R.color.black);
        videoPath = getIntent().getStringExtra(VideoRecordActivity.DATA_FOR_VIDEO_PATH);
        previewImagePath = getIntent().getStringExtra(VideoRecordActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }


    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sv = (SurfaceView) findViewById(R.id.sv);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, screenWidth);
        params.gravity = Gravity.CENTER_VERTICAL;
        sv.setLayoutParams(params);
        sv.getHolder().addCallback(callback);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(onClickListener);
        btn_play.setText("暂停");
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        container = (FrameLayout) findViewById(R.id.container);
        container.setOnClickListener(onClickListener);
        sv.postDelayed(new Runnable() {
            @Override
            public void run() {
                //surfaceview 显示完成才播放
                if (TextUtils.isEmpty(videoPath)) {
                    finish();
                    AppContext.showToast("视频路径错误");
                } else {
                    if (videoPath.startsWith("http")) {
                        downloadVideo();
                    } else {
                        initMediaPlay(videoPath);
                    }
                }
            }
        }, 500);
    }

    private void initMediaPlay(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "视频文件路径错误", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mediaPlayer.setDataSource(file.getAbsolutePath());
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(sv.getHolder());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
//                    // 设置进度条的最大进度为视频流的最大播放时长
//                    seekBar.setMax(mediaPlayer.getDuration());
//                    isPlaying = true;
//                    // 开始线程，更新进度条的刻度
//                    new Thread() {
//
//                        @Override
//                        public void run() {
//                            try {
//                                isPlaying = true;
//                                while (isPlaying) {
//                                    int current = mediaPlayer
//                                            .getCurrentPosition();
//                                    seekBar.setProgress(current);
//                                    sleep(500);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();

                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    isPlaying = false;
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    finish();
//                    AppContext.showToast("播放出错");
                    isPlaying = false;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadVideo() {
        KLog.i("videoPath:::" + videoPath);
        String filePath = FileUtils.getVideoDir(_activity) + File.separator + videoPath.hashCode() + ".mp4";
        if (FileUtils.checkFileExists(filePath)) {//本地有缓存
            KLog.i("本地有缓存:::" + videoPath);
            initMediaPlay(filePath);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getApi().getHttpUtils().download(videoPath, filePath, true, new AjaxCallBack<File>() {
            @Override
            public boolean isProgress() {
                return super.isProgress();
            }

            @Override
            public int getRate() {
                return super.getRate();
            }

            @Override
            public AjaxCallBack<File> progress(boolean progress, int rate) {
                KLog.i("progress:::" + progress);
                KLog.i("rate:::" + rate);
                return super.progress(progress, rate);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                KLog.i("count:::" + count);
                KLog.i("current:::" + current);

            }

            @Override
            public void onSuccess(File file, String requestTag) {
                super.onSuccess(file, requestTag);
                initMediaPlay(file.getAbsolutePath());
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg, String requestTag) {
                super.onFailure(t, errorNo, strMsg, requestTag);
                finish();
                KLog.i("errorNo:::" + errorNo + ",strMsg:::" + ",requestTag:::" + requestTag + ",t:::" + t.getMessage());
                AppContext.showToast("加载失败");
            }
        });
    }


    private Callback callback = new Callback() {
        // SurfaceHolder被修改的时候回调
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                currentPosition = mediaPlayer.getCurrentPosition();
//                mediaPlayer.stop();
//            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//            if (currentPosition > 0) {
//                // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
//                play(currentPosition);
//                currentPosition = 0;
//            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

    };

    private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            KLog.i("1111:::" + progress);
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                // 设置当前播放的位置
                mediaPlayer.seekTo(progress);
                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {

                    }
                });
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_play:
                    if (isPlaying) {
                        btn_play.setText("播放");
                        mediaPlayer.pause();
                        isPlaying = false;
                    } else {
                        btn_play.setText("暂停");
                        mediaPlayer.start();
                        isPlaying = true;
                    }
                    break;
                case R.id.container:
                    finish();
                    break;

            }
        }
    };


    /*
     * 释放资源
     */
    protected void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }


}
