//package com.lailem.app.ui.video.play;
//
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
//import io.vov.vitamio.MediaPlayer.OnCompletionListener;
//import io.vov.vitamio.MediaPlayer.OnInfoListener;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//import android.content.Context;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lailem.app.AppContext;
//import com.lailem.app.R;
//import com.lailem.app.utils.StringUtils;
//import com.lailem.app.utils.TLog;
//
//public class VideoPlayView extends LinearLayout implements OnInfoListener, OnBufferingUpdateListener, OnClickListener, OnCompletionListener {
//
//	private String path;
//	private Uri uri;
//	private VideoView videoView;
//	private TextView downloadRateView, loadRateView;
//	Context context;
//	ImageView previewIV, playIcon;
//	RelativeLayout containerRL;
//	LinearLayout loadingLL;
//	OnVideoPlayListener onVideoPlayListener;
//
//	public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		init(context);
//	}
//
//	public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//		init(context);
//	}
//
//	public VideoPlayView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//
//	public VideoPlayView(Context context) {
//		super(context);
//		init(context);
//	}
//
//	private void init(Context context) {
//		this.context = context;
//		inflate(context, R.layout.video_player_view, this);
//		videoView = (VideoView) findViewById(R.id.buffer);
//		downloadRateView = (TextView) findViewById(R.id.download_rate);
//		loadRateView = (TextView) findViewById(R.id.load_rate);
//		previewIV = (ImageView) findViewById(R.id.image);
//		containerRL = (RelativeLayout) findViewById(R.id.container);
//		containerRL.setOnClickListener(this);
//		loadingLL = (LinearLayout) findViewById(R.id.loading);
//		hideLoading();
//		playIcon = (ImageView) findViewById(R.id.play);
//		playIcon.setOnClickListener(this);
//	}
//
//	public void setData(String videoPath, String imagePath) {
//		this.path = videoPath;
//		if (!TextUtils.isEmpty(imagePath)) {
//			AppContext.getInstance().imageLoader.displayImage(StringUtils.getUri(imagePath), previewIV);
//		} else {
//			hidePreviewImage();
//		}
//	}
//
//	public void setOnVideoPlayListener(OnVideoPlayListener onVideoPlayListener) {
//		this.onVideoPlayListener = onVideoPlayListener;
//	}
//
//	public void start() {
//		if (path == "") {
//			Toast.makeText(context, "Please edit VideoBuffer Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
//			return;
//		} else {
//			uri = Uri.parse(path);
//			videoView.setVideoURI(uri);
//			// videoView.setMediaController(new MediaController(context));
//			videoView.requestFocus();
//			videoView.setOnInfoListener(this);
//			videoView.setOnBufferingUpdateListener(this);
//			videoView.setOnCompletionListener(this);
//			videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//				@Override
//				public void onPrepared(MediaPlayer mediaPlayer) {
//					mediaPlayer.setPlaybackSpeed(1.0f);
//					TLog.analytics("videoView准备好");
//				}
//			});
//		}
//	}
//
//	private void pause() {
//		if (videoView.isPlaying())
//			videoView.pause();
//	}
//
//	public void stop() {
//		if (videoView.isPlaying())
//			videoView.stopPlayback();
//	}
//
//	@Override
//	public boolean onInfo(MediaPlayer mp, int what, int extra) {
//		switch (what) {
//		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//			if (videoView.isPlaying()) {
//				videoView.pause();
//				downloadRateView.setText("");
//				loadRateView.setText("");
//				showLoading();
//			}
//			TLog.analytics("开始缓冲");
//			break;
//		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//			TLog.analytics("缓冲结束");
//			videoView.start();
//			hideLoading();
//			hidePreviewImage();
//			break;
//		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//			downloadRateView.setText("" + extra + "kb/s" + "  ");
//			TLog.analytics("下载速度:::" + extra + "kb/s");
//			break;
//		}
//		return true;
//	}
//
//	@Override
//	public void onBufferingUpdate(MediaPlayer mp, int percent) {
//		loadRateView.setText(percent + "%");
//		TLog.analytics("缓冲:::" + percent + "%");
//	}
//
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.container:
//			pause();
//			if (onVideoPlayListener != null) {
//				onVideoPlayListener.onVideoPlayClick();
//			} else {
//				showPlayIcon();
//			}
//			break;
//
//		case R.id.play:
//			if (isPlayCompletion) {
//			    showLoading();
//			    isPlayCompletion = false;
//			}
//			hidePlayIcon();
//			videoView.start();
//			break;
//		}
//
//	}
//
//	boolean isPlayCompletion;
//	@Override
//	public void onCompletion(MediaPlayer mp) {
//		isPlayCompletion = true;
//		videoView.seekTo(0);
//		// 设置区别播放与暂停的标志
//		playIcon.setVisibility(View.VISIBLE);
//	}
//
//	private void showLoading() {
//		loadingLL.setVisibility(View.VISIBLE);
//	}
//
//	private void hideLoading() {
//		loadingLL.setVisibility(View.GONE);
//	}
//
//	private void showPlayIcon() {
//		playIcon.setVisibility(View.VISIBLE);
//	}
//
//	private void hidePlayIcon() {
//		playIcon.setVisibility(View.GONE);
//	}
//
//	private void showPreviewImage() {
//		previewIV.setVisibility(View.VISIBLE);
//	}
//
//	private void hidePreviewImage() {
//		previewIV.setVisibility(View.GONE);
//	}
//
//	public interface OnVideoPlayListener {
//		public void onVideoPlayClick();
//	}
//
//}
