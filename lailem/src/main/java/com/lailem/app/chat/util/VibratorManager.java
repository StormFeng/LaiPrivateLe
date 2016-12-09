package com.lailem.app.chat.util;

import android.content.Context;
import android.os.Vibrator;

public class VibratorManager {
	private static VibratorManager instance;
	private Vibrator vibrator;

	private VibratorManager(Context context) {
		init(context);
	}

	public static VibratorManager getInstance(Context context) {
		if (instance == null)
			instance = new VibratorManager(context);
		return instance;
	}

	private void init(Context context) {
		vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);

	}

	public void vibrate() {
		if (vibrator != null) {
//			long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
//			vibrator.vibrate(pattern, 2); // 重复两次上面的pattern 如果只想震动一次，index设为
			vibrator.vibrate(200);
		}
	}

	public void cancel() {
		if (vibrator != null)
			vibrator.cancel();
	}

}
