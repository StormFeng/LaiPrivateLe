//package com.lailem.app.ui.video.record;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//import com.lailem.app.AppContext;
//import com.lailem.app.R;
//import com.lailem.app.base.BaseActivity;
//import com.lailem.app.ui.video.play.VideoPlayView;
//import com.lailem.app.ui.video.record.RecordVideoView.OnRecordVideoViewListener;
//import com.lailem.app.utils.NetworkUtils;
//import com.lailem.app.utils.TLog;
//import com.lailem.app.utils.VideoUtils;
//import com.lailem.app.widget.TopBarView;
//import com.yixia.camera.FFMpegUtils;
//import com.yixia.camera.MediaRecorder;
//import com.yixia.camera.MediaRecorder.OnErrorListener;
//import com.yixia.camera.MediaRecorder.OnPreparedListener;
//import com.yixia.camera.MediaRecorderFilter;
//import com.yixia.camera.VCamera;
//import com.yixia.camera.model.MediaObject;
//import com.yixia.camera.model.MediaObject.MediaPart;
//import com.yixia.camera.util.DeviceUtils;
//import com.yixia.camera.util.FileUtils;
//import com.yixia.camera.view.CameraNdkView;
//
///**
// * 视频录制
// *
// * @author tangjun@yixia.com
// *
// */
//public class MediaRecorderActivity extends BaseActivity implements OnErrorListener, OnPreparedListener, OnRecordVideoViewListener {
//	@Bind(R.id.topbar)
//	TopBarView topbar;
//
//	/** 录制最长时间 */
//	public final static int RECORD_TIME_MAX = 10 * 1000;
//	/** 录制最小时间 */
//	public final static int RECORD_TIME_MIN = 3 * 1000;
//
//	private CameraNdkView surfaceView;
//	private MediaRecorderFilter mediaRecorder;
//	private MediaObject mMediaObject;
//	private int mWindowWidth;
//	/** 是否是点击状态 */
//	private volatile boolean mReleased, mStartEncoding;
//	@Bind(R.id.record_video_view)
//	RecordVideoView recordVideoView;
//	@Bind(R.id.videoContainer)
//	RelativeLayout videoContainer;
//	@Bind(R.id.play_preview)
//	VideoPlayView videoPlayView;
//	String previewImagePath;
//	int duration;
//
//	public static final int REQUEST_CODE_FOR_VIDEO_PATH = 777;
//	public static final int RESULT_CODE_FOR_VIDEO_PATH = RESULT_OK;
//	public static final String DATA_FOR_VIDEO_PATH = "videoPath";
//	public static final String DATA_FOR_PREVIEW_IMAGE_PATH = "previewPicPath";
//	public static final String DATA_FOR_DURATION = "duration";
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止锁屏
//		mWindowWidth = DeviceUtils.getScreenWidth(this);
//		setContentView(R.layout.activity_media_recorder);
//		ButterKnife.bind(this);
//		init();
//	}
//
//	private void init() {
//
//	    topbar.setBgColor(Color.BLACK);
//	    topbar.getRight_tv().setEnabled(false);
//		topbar.setTitle("").setLeftImageButton(R.drawable.ic_close, new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				if (mMediaObject != null)
//					mMediaObject.delete();
//				if (mediaRecorder != null) {
//					mediaRecorder.release();
//				}
//				finish();
//
//			}
//		}).setRightText("完成", new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent data = new Intent();
//				data.putExtra(DATA_FOR_VIDEO_PATH, mMediaObject.getOutputTempVideoPath());
//				data.putExtra(DATA_FOR_PREVIEW_IMAGE_PATH, previewImagePath);
//				data.putExtra(DATA_FOR_DURATION, duration+"");
//				setResult(RESULT_CODE_FOR_VIDEO_PATH, data);
//				if (mediaRecorder != null) {
//					mediaRecorder.release();
//				}
//				finish();
//
//			}
//		});
//		surfaceView = (CameraNdkView) findViewById(R.id.record_preview);
//		videoContainer.getLayoutParams().height = mWindowWidth;
//		recordVideoView.setOnRecordVideoViewListener(this);
//		videoPlayView.setVisibility(View.GONE);
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//
//		if (mediaRecorder == null)
//			initMediaRecorder();
//		else {
//			mediaRecorder.setSurfaceHolder(surfaceView.getHolder());
//			mediaRecorder.prepare();
//		}
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		if (mediaRecorder != null && !mReleased) {
//			mediaRecorder.release();
//		}
//	}
//
//	@Override
//	public void onBackPressed() {
//
//		if (mMediaObject != null)
//			mMediaObject.delete();
//		if (mediaRecorder != null) {
//			mediaRecorder.release();
//		}
//		super.onBackPressed();
//	}
//
//	private void initMediaRecorder() {
//		mediaRecorder = new MediaRecorderFilter();
//		mediaRecorder.setOnErrorListener(this);
//		mediaRecorder.setOnPreparedListener(this);
//		// WIFI下800k码率，其他情况（4G/3G/2G）600K码率
//		mediaRecorder.setVideoBitRate(NetworkUtils.isWifi(this) ? MediaRecorder.VIDEO_BITRATE_MEDIUM : MediaRecorder.VIDEO_BITRATE_NORMAL);
//		mediaRecorder.setSurfaceView(surfaceView);
//		String key = com.lailem.app.utils.FileUtils.createFileName();
//		mMediaObject = mediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath() + key);
//		if (mMediaObject != null) {
//			mediaRecorder.prepare();
//			mediaRecorder.setCameraFilter(MediaRecorderFilter.CAMERA_FILTER_NO);
//		} else {
//			Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
//			finish();
//		}
//	}
//
//	private void startEncoding() {
//		// 检测磁盘空间
//		if (FileUtils.showFileAvailable() < 200) {
//			Toast.makeText(this, "磁盘空间不足", Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		if (!isFinishing() && mediaRecorder != null && mMediaObject != null && !mStartEncoding) {
//			mStartEncoding = true;
//
//			new AsyncTask<Void, Void, Boolean>() {
//
//				@Override
//				protected void onPreExecute() {
//					super.onPreExecute();
//				}
//
//				@Override
//				protected Boolean doInBackground(Void... params) {
//					boolean result = FFMpegUtils.videoTranscoding(mMediaObject, mMediaObject.getOutputTempVideoPath(), mWindowWidth, false);
//					if (result && mediaRecorder != null) {
//						mediaRecorder.release();
//						mReleased = true;
//					}
//					return result;
//				}
//
//				@Override
//				protected void onCancelled() {
//					super.onCancelled();
//					mStartEncoding = false;
//				}
//
//				@Override
//				protected void onPostExecute(Boolean result) {
//					super.onPostExecute(result);
//					if (result) {
//						previewImagePath = VideoUtils.getPreviewImagePath(mMediaObject.getOutputTempVideoPath(), _activity);
//						videoPlayView.setVisibility(View.VISIBLE);
//						surfaceView.setVisibility(View.GONE);
//						videoPlayView.setData(mMediaObject.getOutputTempVideoPath(), previewImagePath);
//						videoPlayView.start();
//						hideWaitDialog();
//					    topbar.getRight_tv().setEnabled(true);
//					} else {
//						Toast.makeText(MediaRecorderActivity.this, "转码失败", Toast.LENGTH_SHORT).show();
//					    topbar.getRight_tv().setEnabled(false);
//					}
//					mStartEncoding = false;
//				}
//			}.execute();
//		}
//	}
//
//	@Override
//	public void onVideoError(int what, int extra) {
//		TLog.error("[MediaRecorderActvity]onVideoError: what" + what + " extra:" + extra);
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				Toast.makeText(MediaRecorderActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//
//	@Override
//	public void onAudioError(int what, String message) {
//		TLog.error("[MediaRecorderActvity]onAudioError: what" + what + " message:" + message);
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				Toast.makeText(MediaRecorderActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//
//	@Override
//	public void onPrepared() {
//		// if (mediaRecorder != null) {
//		// // 自动对焦
//		// mediaRecorder.autoFocus(new AutoFocusCallback() {
//		//
//		// @Override
//		// public void onAutoFocus(boolean success, Camera camera) {
//		// if (success) {
//		//
//		// }
//		// }
//		// });
//		// }
//	}
//
//	@Override
//	public void onRecordVideoStart() {
//		if (mediaRecorder == null || mMediaObject == null) {
//			return;
//		}
//		if (surfaceView.getVisibility() != View.VISIBLE) {
//			surfaceView.setVisibility(View.VISIBLE);
//			mediaRecorder.release();
//			initMediaRecorder();
//		}
//		videoPlayView.stop();
//		videoPlayView.setVisibility(View.GONE);
//		mediaRecorder.startRecord();
//		TLog.analytics("开始录制");
//
//	}
//
//	@Override
//	public void onRecordVideoStop(long duration) {
//		this.duration = (int) (duration/1000);
//		showWaitDialog();
//		if (mediaRecorder == null || mMediaObject == null) {
//			return;
//		}
//		mediaRecorder.stopRecord();
//		startEncoding();
//		TLog.analytics("停止录制");
//
//	}
//
//	@Override
//	public void onRecordVideoCancel() {
//		deleteCurrPart();
//		TLog.analytics("取消录制");
//	    topbar.getRight_tv().setEnabled(false);
//
//	}
//
//	@Override
//	public void onRecordVideoTimeNotEnough(long duration) {
//		deleteCurrPart();
//		this.duration = (int) (duration/1000);
//		AppContext.showToast("录制时间太短");
//	    topbar.getRight_tv().setEnabled(false);
//
//
//	}
//
//	private void deleteCurrPart(){
//		if (mMediaObject != null) {
//			MediaPart part = mMediaObject.getCurrentPart();
//			if (part != null) {
//				if (part.remove) {
//					//确认删除分块
//					part.remove = false;
//					mMediaObject.removePart(part, true);
//				} else {
//					part.remove = true;
//				}
//			}
//		}
//	}
//}
