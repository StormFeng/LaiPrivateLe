package com.lailem.app.chat.model.msg;

public class MsgGUpdate extends Msg {
	private String gId;
	private String gIntro;
	private String gName;
	private String gSSPic;

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public String getgIntro() {
		return gIntro;
	}

	public void setgIntro(String gIntro) {
		this.gIntro = gIntro;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public String getgSSPic() {
		return gSSPic;
	}

	public void setgSSPic(String gSSPic) {
		this.gSSPic = gSSPic;
	}

}
