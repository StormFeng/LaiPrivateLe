package com.lailem.app.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.lailem.app.AppContext;
import com.lailem.app.listener.OnAudioRecordListener;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecordUtils {

    MediaRecorder recorder;
    OnAudioRecordListener onAudioRecordListener;
    static int maxDuration = 60;
    String path;
    Timer timer;
    long updatePeriod = 200;
    Handler handler;
    int time;
    public static int handler_record_start = 0;
    public static int handler_record_db = 1;
    public static int handler_record_stop = 2;
    public static int handler_record_time = 3;
    public static int handler_record_cancel = 4;
    private boolean isRecording;
    private double lastDb;

    public AudioRecordUtils() {

    }

    public void setOnSoundRecordListener(OnAudioRecordListener onAudioRecordListener) {
        this.onAudioRecordListener = onAudioRecordListener;
    }

    /**
     * 设置声音录制的最大时长
     *
     * @param maxDuration
     */
    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * 设置录音刷新分贝值的周期（默认200毫秒）,分贝值会通过OnSoundRecordListener监听器抛出
     */
    public void setUpdatePeriod(long updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public void start(Context context) {
        time = 0;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        path = FileUtils.createFileWithSuffix(".amr", context).getAbsolutePath();
        recorder.setOutputFile(path);

        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // recorder.setMaxDuration(maxDuration);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRecording = true;
        onAudioRecordListener.onAudioRecordStart();
        recorder.start();
        dealListener();
    }

    private void dealListener() {
        if (onAudioRecordListener != null) {
            onAudioRecordListener.onAudioRecordStart();
            if (handler == null) {
                handler = new Handler(new Callback() {

                    @Override
                    public boolean handleMessage(Message msg) {
                        if (msg.what == handler_record_db) {
                            lastDb = getDb();
                            onAudioRecordListener.onAudioRecordDb(lastDb);
                        } else if (msg.what == handler_record_time) {
                            onAudioRecordListener.onAudioRecordTime(time);
                        } else if (msg.what == handler_record_stop) {
                            isRecording = false;
                            if (lastDb > 0) {
                                onAudioRecordListener.onAudioRecordEnd(path, time);
                            } else {
                                AppContext.showToast("录音失败，检查录音权限是否开启");
                                onAudioRecordListener.onAudioRecordCancel();
                            }
                        } else if (msg.what == handler_record_cancel) {
                            isRecording = false;
                            onAudioRecordListener.onAudioRecordCancel();
                        }
                        return false;
                    }
                });
            }
            timer = new Timer();
            TimerTask dbTimerTask = new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(handler_record_db);
                }
            };
            TimerTask timeTimerTask = new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(handler_record_time);
                    if (time >= maxDuration) {
                        stop();
                    }
                    time++;
                }
            };
            timer.schedule(dbTimerTask, 0, updatePeriod);
            timer.schedule(timeTimerTask, 0, 1000);
        }
    }

    public String stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
        handler.sendEmptyMessage(handler_record_stop);
        return path;
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
        handler.sendEmptyMessage(handler_record_cancel);
    }

    // 0 dB 到90.3 dB
    private double getDb() {
        if (recorder != null) {
            double ratio = (double) recorder.getMaxAmplitude();
            if (ratio > 1)
                return 20 * Math.log10(ratio);
        }

        return 0;
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public MediaRecorder getRecorder() {
        return recorder;
    }
}
