package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.CInfo;
import com.lailem.app.chat.model.inmsg.GInfo;

public class MsgGMiss extends Msg {
	private GInfo gInfo;

	private CInfo cInfo;

	public GInfo getgInfo() {
		return gInfo;
	}

	public void setgInfo(GInfo gInfo) {
		this.gInfo = gInfo;
	}

	public CInfo getcInfo() {
		return cInfo;
	}

	public void setcInfo(CInfo cInfo) {
		this.cInfo = cInfo;
	}

}
