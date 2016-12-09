//package com.lailem.app.ui.video.play;
//
//import android.os.Bundle;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//import com.lailem.app.R;
//import com.lailem.app.base.BaseActivity;
//import com.lailem.app.ui.video.play.VideoPlayView.OnVideoPlayListener;
//import com.lailem.app.ui.video.record.MediaRecorderActivity;
//import com.lailem.app.utils.TDevice;
//
//public class PlayVideoActivity extends BaseActivity implements OnVideoPlayListener {
//
//	@Bind(R.id.videoPlayView)
//	VideoPlayView videoPlayView;
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.activity_play_video);
//		ButterKnife.bind(this);
//		initView();
//		initData();
//	}
//
//	private void initView() {
//		videoPlayView.post(new Runnable() {
//
//			@Override
//			public void run() {
//				LinearLayout.LayoutParams params = (LayoutParams) videoPlayView.getLayoutParams();
//				params.width = (int) TDevice.getScreenWidth();
//				params.height  = params.width;
//				videoPlayView.setLayoutParams(params);
//			}
//		});
//	}
//
//	private void initData(){
//		String videoPath = getIntent().getStringExtra(MediaRecorderActivity.DATA_FOR_VIDEO_PATH);
//		String previewImagePath = getIntent().getStringExtra(MediaRecorderActivity.DATA_FOR_PREVIEW_IMAGE_PATH);
//		videoPlayView.setData(videoPath, previewImagePath);
//		videoPlayView.setOnVideoPlayListener(this);
//		videoPlayView.start();
//	}
//
//	@Override
//	public void onVideoPlayClick() {
//		finish();
//	}
//}
