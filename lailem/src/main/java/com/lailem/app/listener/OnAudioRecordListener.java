package com.lailem.app.listener;

public interface OnAudioRecordListener {
	/**
	 * 录制开始
	 */
	void onAudioRecordStart();

	/**
	 * 录制中,根据设置的更新周期调用，默认200ms
	 * 
	 * @param db
	 *            声音分贝值，0 dB 到90.3 dB
	 */
	void onAudioRecordDb(double db);

	/**
	 * 录制结束
	 * 
	 * @param path
	 *            声音文件路径
	 */
	void onAudioRecordEnd(String path,int time);

	/**
	 * 录制时间，每隔一秒调用一次
	 * 
	 * @param time
	 *            单位秒
	 */
	void onAudioRecordTime(int time);
	
	
	void onAudioRecordCancel();

}
