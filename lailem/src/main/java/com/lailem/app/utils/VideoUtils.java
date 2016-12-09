package com.lailem.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.FileOutputStream;

public class VideoUtils {

	public static String getPreviewImagePath(String videoPath, Context context) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(videoPath);
		Bitmap bitmap = retriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
		File file = FileUtils.createFileWithSuffix(".jpg", context);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 80, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			retriever.release();
		}
		return file.getAbsolutePath();
	}
}
