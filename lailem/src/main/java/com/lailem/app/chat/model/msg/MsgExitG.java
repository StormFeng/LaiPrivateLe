package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.GInfo;
import com.lailem.app.chat.model.inmsg.HInfo;
import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgExitG extends Msg {
	private GInfo gInfo;
	private UInfo uInfo;
	private HInfo hInfo;

	public GInfo getgInfo() {
		return gInfo;
	}

	public void setgInfo(GInfo gInfo) {
		this.gInfo = gInfo;
	}

	public UInfo getuInfo() {
		return uInfo;
	}

	public void setuInfo(UInfo uInfo) {
		this.uInfo = uInfo;
	}

	public HInfo gethInfo() {
		return hInfo;
	}

	public void sethInfo(HInfo hInfo) {
		this.hInfo = hInfo;
	}

}
