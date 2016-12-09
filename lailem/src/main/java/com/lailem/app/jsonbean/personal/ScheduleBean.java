package com.lailem.app.jsonbean.personal;

import com.google.gson.JsonSyntaxException;
import com.lailem.app.AppException;
import com.lailem.app.bean.Result;

import java.util.ArrayList;
/**
 * 个人中心-我的日程
 * @author MD-12
 *
 */
public class ScheduleBean extends Result {
	private ArrayList<Schedule> scheduleList;// 日程列表 array<object>

	public ArrayList<Schedule> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(ArrayList<Schedule> scheduleList) {
		this.scheduleList = scheduleList;
	}

	public static ScheduleBean parse(String json) throws AppException {
		ScheduleBean res = new ScheduleBean();
		try {
			res = gson.fromJson(json, ScheduleBean.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			throw AppException.json(e);
		}
		return res;
	}

	public static class Schedule extends Result {
		// 待完善
	}
}
