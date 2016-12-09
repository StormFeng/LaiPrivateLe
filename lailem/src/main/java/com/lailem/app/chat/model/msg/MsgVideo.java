package com.lailem.app.chat.model.msg;

public class MsgVideo extends Msg {
	private String d;
	private String video;
	private String localPath;
	private String pic;
	private String localPicPath;

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getLocalPicPath() {
		return localPicPath;
	}

	public void setLocalPicPath(String localPicPath) {
		this.localPicPath = localPicPath;
	}

}
