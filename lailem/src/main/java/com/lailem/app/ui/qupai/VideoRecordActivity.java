package com.lailem.app.ui.qupai;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.FailureCallback;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.qupai.sdk.utils.AppGlobalSetting;
import com.google.common.io.Files;
import com.lailem.app.AppContext;
import com.lailem.app.R;
import com.lailem.app.base.BaseActivity;
import com.lailem.app.utils.FileUtils;
import com.lailem.app.utils.UIHelper;
import com.lailem.app.widget.TopBarView;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoRecordActivity extends BaseActivity {

    public static final int REQUEST_CODE_FOR_VIDEO_PATH = 777;
    public static final int RESULT_CODE_FOR_VIDEO_PATH = RESULT_OK;
    public static final String DATA_FOR_VIDEO_PATH = "videoPath";
    public static final String DATA_FOR_PREVIEW_IMAGE_PATH = "previewPicPath";
    public static final String DATA_FOR_DURATION = "duration";
    /**
     * 默认时长
     */
    public static int DEFAULT_DURATION_LIMIT = 10;
    /**
     * 默认码率
     */
    public static int DEFAULT_BITRATE = 1000 * 1000;
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static String WATER_MARK_PATH = "assets://Qupai/watermark/laile-logo.png";
    public static int RECORDE_SHOW = 10001;
    public final static String PREF_VIDEO_EXIST_USER = "Qupai_has_video_exist_in_user_list_pref";
    private MediaPlayer mediaPlayer;
    @Bind(R.id.topbar)
    TopBarView topbar;
    @Bind(R.id.reRecord)
    Button reRecordBtn;
    @Bind(R.id.sv)
    SurfaceView sv;
    File videoFile, thumFile;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showRecode();
    }

    private void initView() {
        setContentView(R.layout.activity_video_record);
        ButterKnife.bind(this);
        topbar.setBackgroundColor(Color.BLACK);
        topbar.setTitle("预览").setLeftText("取消", UIHelper.finish(this)).setRightText("完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                over();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, screenWidth);
        params.gravity = Gravity.CENTER_VERTICAL;
        sv.setLayoutParams(params);
        sv.getHolder().addCallback(callback);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        // SurfaceHolder被修改的时候回调
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            initMediaPlay(videoFile.getAbsolutePath());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

    };

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
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    finish();
                    AppContext.showToast("播放出错");
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.reRecord)
    public void clickReRecord() {
        release();
        showRecode();
    }

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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void showRecode() {
        QupaiService qupaiService = AlibabaSDK.getService(QupaiService.class);

        if (qupaiService == null) {
            finish();
            AppContext.showToast("失败");
            return;
        }

        // 引导，只显示一次，这里用SharedPreferences记录
        final AppGlobalSetting sp = new AppGlobalSetting(
                getApplicationContext());
        Boolean isGuideShow = sp.getBooleanGlobalItem(PREF_VIDEO_EXIST_USER,
                true);

        qupaiService.showRecordPage(this, RECORDE_SHOW, isGuideShow,
                new FailureCallback() {
                    @Override
                    public void onFailure(int i, String s) {
                        KLog.i("onFailure:" + s + "CODE" + i);
                        finish();
                        AppContext.showToast("失败");
                    }
                });

        sp.saveGlobalConfigItem(PREF_VIDEO_EXIST_USER, false);

    }

    public static void init(final Context context) {
        /**
         * 集成必须要做的初始化
         */
        AlibabaSDK.turnOnDebug();
        AlibabaSDK.asyncInit(context, new InitResultCallback() {
            @Override
            public void onSuccess() {
                KLog.i("qupai初始化成功");
                QupaiService qupaiService = AlibabaSDK
                        .getService(QupaiService.class);

                VideoSessionCreateInfo info = new VideoSessionCreateInfo.Builder()
                        .setOutputDurationLimit(DEFAULT_DURATION_LIMIT)
                        .setOutputVideoBitrate(DEFAULT_BITRATE)
                        .setHasImporter(false).setHasEditorPage(true)
                        .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)
                        .setBeautyProgress(80).setBeautySkinOn(true).build();

                qupaiService.initRecord(info);

                if (qupaiService != null) {
                    qupaiService.addMusic(0, "Athena",
                            "assets://Qupai/music/Athena");
                    qupaiService.addMusic(1, "Box Clever",
                            "assets://Qupai/music/Box Clever");
                    qupaiService.addMusic(2, "Byebye love",
                            "assets://Qupai/music/Byebye love");
                    qupaiService.addMusic(3, "chuangfeng",
                            "assets://Qupai/music/chuangfeng");
                    qupaiService.addMusic(4, "Early days",
                            "assets://Qupai/music/Early days");
                    qupaiService.addMusic(5, "Faraway",
                            "assets://Qupai/music/Faraway");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                KLog.i("qupai初始化失败：" + s);
            }
        });
    }

    private void over() {
        if (videoFile != null && thumFile != null) {
            Intent intent = new Intent();
            intent.putExtra(DATA_FOR_VIDEO_PATH, videoFile.getAbsolutePath());
            intent.putExtra(DATA_FOR_PREVIEW_IMAGE_PATH, thumFile.getAbsolutePath());
            intent.putExtra(DATA_FOR_DURATION, duration);
            setResult(RESULT_CODE_FOR_VIDEO_PATH, intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            Bundle bundle = data.getBundleExtra("qupai.edit.result");
            String videoPath = bundle.getString("path");
            String[] thum = bundle.getStringArray("thumbnail");
            KLog.i("videoPath:::" + videoPath);
            KLog.i("thum[0]::::" + thum[0]);

            videoFile = FileUtils.createFileWithSuffix(".mp4", _activity);
            thumFile = FileUtils.createFileWithSuffix(".jpg", _activity);
            try {
                Files.move(new File(videoPath), videoFile);
                Files.move(new File(thum[0]), thumFile);
            } catch (IOException e) {
                Toast.makeText(this, "拷贝失败", Toast.LENGTH_LONG).show();
                finish();
                e.printStackTrace();
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /**
                         * 清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作。
                         * 上面的拷贝操作请自行实现，第一版本的copyVideoFile接口不再使用
                         */
                        QupaiService qupaiService = AlibabaSDK
                                .getService(QupaiService.class);
                        qupaiService.deleteDraft(getApplicationContext(), data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            over();
//            initView();


        } else {
            if (resultCode == RESULT_CANCELED) {
                KLog.i("取消视频录制");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                }).start();
            }
        }

    }

}
