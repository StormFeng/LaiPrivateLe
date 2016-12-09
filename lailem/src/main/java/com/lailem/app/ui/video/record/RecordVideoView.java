package com.lailem.app.ui.video.record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lailem.app.utils.TDevice;

import java.util.Timer;
import java.util.TimerTask;

public class RecordVideoView extends View {
	int width, height;
	float centerX, centerY, centerXForDot, centerYForDot;
	Paint paint;
	float radius, radiusForDot;
	boolean init;
	int gray = Color.parseColor("#505050");
	int green = Color.parseColor("#88C057");
	int wihte = Color.parseColor("#999999");
	float lineWidth = (int) TDevice.dpToPixel(1.5f);
	RectF ovalGray, ovalGreen;
	float sweepAngle = 0;
	long timeLimitMax = 10000, timeLimitMin = 3000,duration;
	long freshTimeStep = 50;
	float angleStep = 360f * freshTimeStep / timeLimitMax;

	String tip1 = "按住即拍";
	String tip2 = "按住重录";
	String tip3 = "手滑出圈外即可取消";
	String tip4 = "松开即可取消";
	float tip1X, tip1Y, tip2X, tip2Y, tip3X, tip3Y, tip4X, tip4Y;
	float tip1TextSize = 30f, tip2TextSize = 30f, tip3TextSize = 25f, tip4TextSize = 25f;
	/**
	 * 是否正在录制
	 */
	boolean isRecording;
	/**
	 * 是否录制完成
	 */
	boolean isRecordingOver;

	boolean isCancel;

	public static final int HANDLE_INVALIDATE = 0;
	public static final int HANDLE_STOP = 1;
	public static final int HANDLE_STOP_CANCEL = 2;

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message message) {
			switch (message.what) {
			case HANDLE_INVALIDATE:
				invalidate();
				break;
			case HANDLE_STOP:
				stop(false);
				break;
			case HANDLE_STOP_CANCEL:
				stop(true);
				break;

			}
			return false;
		}
	});

	public RecordVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public RecordVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public RecordVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RecordVideoView(Context context) {
		super(context);
		init();
	}

	private void init() {
		post(new Runnable() {

			@Override
			public void run() {
				width = getWidth();
				height = getHeight();
				paint = new Paint();
				paint.setAntiAlias(true);
				paint.setStrokeWidth(lineWidth);
				centerX = width / 2;
				centerY = (int) (height * 0.4);
				centerXForDot = width / 2;
				radius = (int) TDevice.dpToPixel(65f);
				radiusForDot = (int) TDevice.dpToPixel(3.85f);
				centerYForDot = centerY - radius;
				ovalGray = new RectF();
				ovalGray.left = centerX - radius;
				ovalGray.right = centerX + radius;
				ovalGray.top = centerY - radius;
				ovalGray.bottom = centerY + radius;
				ovalGreen = new RectF();
				ovalGreen.left = centerX - radius;
				ovalGreen.right = centerX + radius;
				ovalGreen.top = centerY - radius;
				ovalGreen.bottom = centerY + radius;
				int[] tip1Size = getScreenSize(tip1);
				int[] tip2Size = getScreenSize(tip2);
				int[] tip3Size = getScreenSize(tip3);
				int[] tip4Size = getScreenSize(tip4);
				tip1X = centerX - tip1Size[0];
				tip1Y = centerY + tip1Size[1] / 2;
				tip2X = centerX - tip2Size[0];
				tip2Y = centerY + tip2Size[1] / 2;
				tip3X = centerX - tip3Size[0];
				tip3Y = centerY + radius + tip3Size[1] / 2 + TDevice.dpToPixel(33.6f);
				tip4X = centerX - tip4Size[0];
				tip4Y = centerY + radius + tip4Size[1] / 2 + TDevice.dpToPixel(33.6f);

				init = true;
			}
		});
	}

	Timer timer;

	private void start() {
		isRecording = true;
		isRecordingOver = false;
		isCancel = false;
		reset();
		if (onRecordVideoViewListener != null) {
			onRecordVideoViewListener.onRecordVideoStart();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 改变绿圆角度
				sweepAngle += angleStep;
				// 改变小圆点的中心
				double radians = Math.toRadians(sweepAngle);
				centerXForDot = centerX + (float) (radius * Math.sin(radians));
				centerYForDot = centerY - (float) (radius * Math.cos(radians));
				// 更新视图
				handler.sendEmptyMessage(0);
                duration += freshTimeStep;
				if (sweepAngle >= 360) {
					handler.sendEmptyMessage(HANDLE_STOP);
				}
			}
		}, freshTimeStep, freshTimeStep);
	}

	private void stop(boolean isCancel) {
		isRecording = false;
		isRecordingOver = true;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (isCancel) {
			reset();
			if (onRecordVideoViewListener != null)
				onRecordVideoViewListener.onRecordVideoCancel();
		} else {
			if (onRecordVideoViewListener != null) {
				if(duration>=timeLimitMin){
					onRecordVideoViewListener.onRecordVideoStop(duration);
				}else{
					reset();
					onRecordVideoViewListener.onRecordVideoTimeNotEnough(duration);
				}
			}
		}

		// 停止之后 不能通过timer刷新视图
		handler.sendEmptyMessage(HANDLE_INVALIDATE);

	}

	private void reset() {
		sweepAngle = 0;
		centerXForDot = width / 2;
		centerYForDot = centerY - radius;
		duration = 0;
	}

	public void setTimeLimit(long timeLimit) {
		this.timeLimitMax = timeLimit;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isCircleIn(event.getX(), event.getY())) {
				start();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isCircleIn(event.getX(), event.getY())) {
				isCancel = false;
			} else {// 圈外取消
				isCancel = true;
			}
			break;

		case MotionEvent.ACTION_UP:
			if (isRecording) {// 正在录制才存在停止
				if (isCancel) {
					stop(true);
				} else {
					stop(false);
				}
			}
			break;
		}
		return true;
	}

	private boolean isCircleIn(float x, float y) {
		if (Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)) <= radius) {
			return true;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (init) {
			canvas.drawColor(Color.BLACK);
			drawCircle(canvas);
			drawText(canvas);
		}
		super.onDraw(canvas);
	}

	private void drawCircle(Canvas canvas) {
		// 画灰圆
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(gray);
		canvas.drawArc(ovalGray, 0, 360, false, paint);
		// 画绿圆
		paint.setColor(green);
		canvas.drawArc(ovalGreen, 270, sweepAngle, false, paint);
		// 画移动小圆点
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(centerXForDot, centerYForDot, radiusForDot, paint);
	}

	private void drawText(Canvas canvas) {
		paint.setColor(wihte);
		if (isRecordingOver) {
			paint.setTextSize(tip2TextSize);
			canvas.drawText(tip2, tip2X, tip2Y, paint);
		} else {
			paint.setTextSize(tip1TextSize);
			canvas.drawText(tip1, tip1X, tip1Y, paint);
		}

		if (isRecording && !isCancel) {
			paint.setColor(gray);
			paint.setTextSize(tip3TextSize);
			canvas.drawText(tip3, tip3X, tip3Y, paint);
		}
		if (isCancel) {
			paint.setColor(gray);
			paint.setTextSize(tip4TextSize);
			canvas.drawText(tip4, tip4X, tip4Y, paint);
		}

	}

	private int[] getScreenSize(String str) {
		Rect rect = new Rect();
		paint.getTextBounds(str, 0, str.length(), rect);
		int strwid = rect.width();
		int strhei = rect.height();
		return new int[] { strwid, strhei };

	}

	OnRecordVideoViewListener onRecordVideoViewListener;

	public void setOnRecordVideoViewListener(OnRecordVideoViewListener onRecordVideoViewListener) {
		this.onRecordVideoViewListener = onRecordVideoViewListener;
	}

	public interface OnRecordVideoViewListener {
		/**
		 * 开始录制视频
		 */
		public void onRecordVideoStart();

		/**
		 * 停止
		 */
		public void onRecordVideoStop(long duration);

		/**
		 * 取消
		 */
		public void onRecordVideoCancel();
		
		/**
		 * 录制时间太短
		 */
		public void onRecordVideoTimeNotEnough(long duration);
	}
}
