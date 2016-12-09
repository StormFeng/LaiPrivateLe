package com.lailem.app.chat.model.msg;

import com.lailem.app.chat.model.inmsg.GInfo;
import com.lailem.app.chat.model.inmsg.HInfo;
import com.lailem.app.chat.model.inmsg.UInfo;

public class MsgJoinG extends Msg {
	private HInfo hInfo;
	private GInfo gInfo;
	private String way;
	private UInfo uInfo;

	public HInfo gethInfo() {
		return hInfo;
	}

	public void sethInfo(HInfo hInfo) {
		this.hInfo = hInfo;
	}

	public GInfo getgInfo() {
		return gInfo;
	}

	public void setgInfo(GInfo gInfo) {
		this.gInfo = gInfo;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public UInfo getuInfo() {
		return uInfo;
	}

	public void setuInfo(UInfo uInfo) {
		this.uInfo = uInfo;
	}

}
