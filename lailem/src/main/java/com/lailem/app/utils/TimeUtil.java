package com.lailem.app.utils;

import java.util.Calendar;


/**
 * 时间分段工具类
 * 
 * @author leeib
 * 
 */
public class TimeUtil {
	private static TimeUtil timeUtil;

	private TimeUtil() {

	}

	public static TimeUtil getInstance() {
		if (timeUtil == null) {
			timeUtil = new TimeUtil();
		}
		return timeUtil;
	}

	/**
	 * 将对应的时间格式化
	 * 
	 * @param timestamp
	 *            时间戳
	 * @return 格式为 "下午 4：15"、 "凌晨 3：16"、 "上午 11：15"、
	 *         "晚上 10：15","周一  下午 4：15","10月12日   下午 4：15"
	 *         ,"2010年10月12日  下午 4：15"
	 */
	public String getTime(long timestamp) {
		String time = "";
		Calendar calendar = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
		int week_of_month = calendar.get(Calendar.WEEK_OF_MONTH);
		int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
		int hour_of_day = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		int tyear = today.get(Calendar.YEAR);
		int tmonth = today.get(Calendar.MONTH) + 1;
		int tweek_of_month = today.get(Calendar.WEEK_OF_MONTH);
		int tday_of_month = today.get(Calendar.DAY_OF_MONTH);

		if (year == tyear && month == tmonth && day_of_month == tday_of_month) {// 今天
			time = getDayTime(calendar);
		} else if (year == tyear && month == tmonth && week_of_month == tweek_of_month) {// 本周
			time = getWeekTime(day_of_week, hour_of_day, minute);
		} else if (year == tyear) {// 本年
			time = getYearTime(month, day_of_month, hour_of_day, minute);
		} else if (year < tyear) {// 去年以前
			time = getFreYearTime(year, month, day_of_month, hour_of_day, minute);
		} else {// 今天以后
			time = getFreYearTime(year, month, day_of_month, hour_of_day, minute);
		}

		return time;
	}

	/**
	 * 将当前时间格式化，条件是当前时间与上一次记录时间的时间间隔大于等于timeSpace
	 * 
	 * @param lastRecordTime
	 *            上一次记录的时间
	 * @param timeSpace
	 *            当前时间与上一次记录时间的时间间隔，单位毫秒
	 * @return 格式为 "下午 4：15"、 "凌晨 3：16"、 "上午 11：15"、 "晚上 10：15"，当不满足记录条件时返回"".
	 */
	public String getCurrTime(String lastRecordTime, long timeSpace) {
		String time = "";
		long currTime = System.currentTimeMillis();
		if (StringUtils.isEmpty(lastRecordTime) || Math.abs(currTime - Long.parseLong(lastRecordTime)) < timeSpace) {
			return time;
		}
		time = getDayTime(Calendar.getInstance());
		return time;
	}

	/**
	 * 将当前时间格式化
	 * 
	 * @return 格式为 "下午 4：15"、 "凌晨 3：16"、 "上午 11：15"、 "晚上 10：15"
	 */
	public String getCurrTime() {
		return getDayTime(Calendar.getInstance());
	}
	
	public boolean needRecordTime(long lastRecordTime, long timeSpace){
		long currTime = System.currentTimeMillis();
		if (Math.abs(currTime - lastRecordTime) < timeSpace) {
			return false;
		}
		return true;
	}

	private String getDayTime(Calendar calendar) {
		String time = "";
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour >= 18) {
			time = "晚上" + hour + ":" + calendar.get(Calendar.MINUTE);
		} else if (hour >= 13) {
			time = "下午" + hour + ":" + calendar.get(Calendar.MINUTE);
		} else if (hour >= 12) {
			time = "中午" + hour + ":" + calendar.get(Calendar.MINUTE);
		} else if (hour >= 6) {
			time = "上午" + hour + ":" + calendar.get(Calendar.MINUTE);
		} else {
			time = "凌晨" + hour + ":" + calendar.get(Calendar.MINUTE);
		}
		return time;
	}

	private String getDayTime(int hour_of_day, int minute) {
		String time = "";
		if (hour_of_day >= 18) {
			time = "晚上" + hour_of_day + ":" + minute;
		} else if (hour_of_day >= 13) {
			time = "下午" + hour_of_day + ":" + minute;
		} else if (hour_of_day >= 12) {
			time = "中午" + hour_of_day + ":" + minute;
		} else if (hour_of_day >= 6) {
			time = "上午" + hour_of_day + ":" + minute;
		} else {
			time = "凌晨" + hour_of_day + ":" + minute;
		}

		return time;
	}

	private String getWeekTime(int day_of_week, int hour_of_day, int minute) {
		String time = "";
		if (Calendar.SUNDAY == day_of_week) {
			time = "周日  " + getDayTime(hour_of_day, minute);
		} else if (Calendar.SATURDAY == day_of_week) {
			time = "周六  " + getDayTime(hour_of_day, minute);
		} else if (Calendar.FRIDAY == day_of_week) {
			time = "周五  " + getDayTime(hour_of_day, minute);
		} else if (Calendar.THURSDAY == day_of_week) {
			time = "周四  " + getDayTime(hour_of_day, minute);
		} else if (Calendar.WEDNESDAY == day_of_week) {
			time = "周三  " + getDayTime(hour_of_day, minute);
		} else if (Calendar.TUESDAY == day_of_week) {
			time = "周二  " + getDayTime(hour_of_day, minute);
		} else if (Calendar.MONDAY == day_of_week) {
			time = "周一  " + getDayTime(hour_of_day, minute);
		}
		return time;
	}

	private String getYearTime(int month, int day_of_month, int hour_of_day, int minute) {
		return month + "月" + day_of_month + "日  " + getDayTime(hour_of_day, minute);
	}

	private String getFreYearTime(int year, int month, int day_of_month, int hour_of_day, int minute) {
		return year + "年" + month + "月" + day_of_month + "日  " + getDayTime(hour_of_day, minute);
	}

}
