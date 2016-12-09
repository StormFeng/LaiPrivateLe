package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgALike extends Msg {
	private String aId;
	private UInfo uInfo;

	public String getaId() {
		return aId;
	}

	public void setaId(String aId) {
		this.aId = aId;
	}

	public UInfo getuInfo() {
		return uInfo;
	}

	public void setuInfo(UInfo uInfo) {
		this.uInfo = uInfo;
	}

}
