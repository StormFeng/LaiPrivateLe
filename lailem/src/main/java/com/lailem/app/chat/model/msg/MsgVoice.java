package com.lailem.app.chat.model.msg;

public class MsgVoice extends Msg {
	private String d;
	private String voice;
	private String localPath;

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	@Override
	public String toString() {
		return "MsgVoice [d=" + d + ", voice=" + voice + ", localPath=" + localPath + "]";
	}
	
	
	

}
