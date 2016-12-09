package com.lailem.app.chat.listener;

import com.lailem.app.dao.Message;

public interface OnSendListener {
	/**
	 * 发送成功
	 * @param message
	 */
   public void onSended(Message message);
   /**
    * 发送失败
    * @param message
    */
   public void onSendFail(Message message);
   /**
    * 文件上传中
    * @param message
    * @param progress
    */
   public void onSendUploading(Message message,String progress);
}
