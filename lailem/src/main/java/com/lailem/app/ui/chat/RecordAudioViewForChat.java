package com.lailem.app.ui.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.lailem.app.utils.TDevice;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RecordAudioViewForChat extends View {

	Paint paint;
	public int width;
	public int heigth;
	public int xOfCenterOfCircle, yOfCenterOfCircle, yOfCenterOfLine;
	public int radius, radiusForBigDot, radiusForSmallDot;
	int redColor = Color.parseColor("#FFE56032");
	int grayColor = Color.parseColor("#FFABABAB");
	ArrayList<String> times;
	public ArrayList<Point> points;
	ArrayList<RectF> lines;
	String tip1 = "移出即可取消", tip2 = "松开即可取消";
	String currTip = tip1;
	int timeX, timeY;
	int tipX, tipY;
	float tipTextSize = 42;
	float timeTextSize = 100;
	int lineSpace = (int) TDevice.dpToPixel(6f);
	int lineWidth = (int) TDevice.dpToPixel(3.5f);
	int lineMaxHeight = (int) TDevice.dpToPixel(40f);
	int lineMinHeight = (int) TDevice.dpToPixel(20f);
	int defaultLineTop;
	int defaultLineBottom;
	int lineHeightHelfRange = (lineMaxHeight - lineMinHeight) / 2;
	int dotCount = 30, lineCount = 15;

	Timer timer;
	TimerTask timerTask;

	int index;

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message message) {
			invalidate();
			return false;
		}
	});

	public RecordAudioViewForChat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public RecordAudioViewForChat(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public RecordAudioViewForChat(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
	}

	public RecordAudioViewForChat(Context context) {
		super(context);
		init();
	}

	public void init() {
		this.post(new Runnable() {

			@Override
			public void run() {
				width = getWidth();
				heigth = getHeight();
				setVisibility(View.GONE);
				paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(Color.RED);
				xOfCenterOfCircle = (int) (width * 0.5);
				yOfCenterOfCircle = (int) (heigth * 0.35);
				radius = (int) TDevice.dpToPixel(90f);
				radiusForBigDot = (int) TDevice.dpToPixel(12f) / 2;
				radiusForSmallDot = (int) TDevice.dpToPixel(7f) / 2;
				// 初始化小圆点
				points = new ArrayList<Point>();
				int perDegree = 360 / dotCount;
				for (int i = 0; i < dotCount; i++) {
					Point point = new Point();
					double radians = Math.toRadians(i * perDegree);
					double xSpace = radius * Math.sin(radians);
					double ySpace = radius * Math.cos(radians);
					point.x = (int) (xOfCenterOfCircle + xSpace);
					point.y = (int) (yOfCenterOfCircle - ySpace);
					points.add(point);
				}
				// 初始化时间值
				times = new ArrayList<String>();
				for (int i = 0; i < 61; i++) {
					if ((i + "").length() == 1) {
						times.add("0" + i);
					} else {
						times.add(i + "");
					}
				}

				// 初始化时间与提示文字的x\y
				paint.setTextSize(timeTextSize);
				int[] timeScreenSize = getScreenSize(times.get(0));
				timeX = xOfCenterOfCircle - timeScreenSize[0] / 2;
				timeY = yOfCenterOfCircle + timeScreenSize[1] / 2;
				paint.setTextSize(tipTextSize);
				int[] tipScreenSize = getScreenSize(tip1);
				tipX = xOfCenterOfCircle - tipScreenSize[0] / 2;
				tipY = yOfCenterOfCircle + tipScreenSize[1] / 2 + radius / 2;

				// 初始化线
				lines = new ArrayList<RectF>();
				int firstLeft = (width - (lineCount * lineWidth + (lineCount - 1) * lineSpace)) / 2;
				yOfCenterOfLine = yOfCenterOfCircle + 2 * radius;
				defaultLineTop = yOfCenterOfLine - lineMinHeight / 2;
				defaultLineBottom = defaultLineTop + lineMinHeight;
				for (int i = 0; i < lineCount; i++) {
					RectF rectF = new RectF();
					rectF.left = firstLeft + i * (lineWidth + lineSpace);
					rectF.right = firstLeft + (i + 1) * lineWidth + i * lineSpace;
					rectF.top = defaultLineTop;
					rectF.bottom = defaultLineBottom;
					lines.add(rectF);
				}

			}
		});

	}

	public void start() {
		flagForStop = true;
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				index++;
				if (index == 60) {
					stop();
				}
				handler.sendEmptyMessage(100);
			}
		};
		timer.schedule(timerTask, 0, 1000);
		final ArrayList<RectF> selectLines = new ArrayList<RectF>();
		final Random random = new Random();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (flagForStop) {
					int randomCount = (int) (lineCount * db / 100);
					for (int i = 0; i < randomCount; i++) {
						RectF rectF = lines.get(random.nextInt(lineCount));
						int range = random.nextInt(lineHeightHelfRange);
						rectF.top = defaultLineTop - range;
						rectF.bottom = defaultLineBottom + range;
						selectLines.add(rectF);
					}
					handler.sendEmptyMessage(100);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (RectF rectF : selectLines) {
						rectF.top = defaultLineTop;
						rectF.bottom = defaultLineBottom;
					}
					selectLines.clear();
					handler.sendEmptyMessage(200);

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		}).start();

	}

	boolean flagForStop;

	public void stop() {
		if (flagForStop) {
			flagForStop = false;
			timer.cancel();
			index = 0;
			currTip = tip1;
			flagForChangeTipSock = true;
		}
	}

	boolean flagForChangeTipSock;

	public void showTip1() {
		if (flagForChangeTipSock) {
			currTip = tip1;
			invalidate();
			flagForChangeTipSock = false;
		}
	}

	public void showTip2() {
		if (!flagForChangeTipSock) {
			currTip = tip2;
			invalidate();
			flagForChangeTipSock = true;
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawDots(canvas);
		drawTime(canvas);
		drawTip(canvas);
		drawLines(canvas);

	}

	private void drawDots(Canvas canvas) {
		int size = points.size();
		for (int i = 0; i < size; i++) {
			Point point = points.get(i);
			if (i < index / 2) {
				paint.setColor(redColor);
			} else {
				paint.setColor(grayColor);
			}
			if (i == 0) {
				canvas.drawCircle(point.x, point.y, radiusForBigDot, paint);
			} else {
				canvas.drawCircle(point.x, point.y, radiusForSmallDot, paint);
			}
		}
	}

	private void drawTime(Canvas canvas) {
		paint.setColor(Color.WHITE);
		paint.setTextSize(timeTextSize);
		String time = times.get(index);
		canvas.drawText(time, timeX, timeY, paint);
	}

	private void drawTip(Canvas canvas) {
		paint.setColor(grayColor);
		paint.setTextSize(tipTextSize);
		canvas.drawText(currTip, tipX, tipY, paint);
	}

	private void drawLines(Canvas canvas) {
		for (int i = 0; i < lineCount; i++) {
			RectF rectF = lines.get(i);
			canvas.drawRoundRect(rectF, 8, 8, paint);
		}

	}

	private int[] getScreenSize(String str) {
		Rect rect = new Rect();
		paint.getTextBounds(str, 0, str.length(), rect);
		int strwid = rect.width();
		int strhei = rect.height();
		return new int[] { strwid, strhei };

	}

	double db;

	public void setDb(double db) {
		this.db = db;
	}

}
