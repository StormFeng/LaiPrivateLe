package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.DInfo;
import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgDLike extends Msg {
	private DInfo dInfo;
	private UInfo uInfo;

	public DInfo getdInfo() {
		return dInfo;
	}

	public void setdInfo(DInfo dInfo) {
		this.dInfo = dInfo;
	}

	public UInfo getuInfo() {
		return uInfo;
	}

	public void setuInfo(UInfo uInfo) {
		this.uInfo = uInfo;
	}

}
