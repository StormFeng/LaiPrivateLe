package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.GInfoMore;
import com.lailem.app.chat.model.inmsg.VInfo;

public class MsgAGApply extends Msg {
	private GInfoMore gInfo;
	private VInfo vInfo;

	public GInfoMore getgInfo() {
		return gInfo;
	}

	public void setgInfo(GInfoMore gInfo) {
		this.gInfo = gInfo;
	}

	public VInfo getvInfo() {
		return vInfo;
	}

	public void setvInfo(VInfo vInfo) {
		this.vInfo = vInfo;
	}

}
