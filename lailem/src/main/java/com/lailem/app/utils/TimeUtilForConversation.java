package com.lailem.app.utils;

import java.util.Calendar;


/**
 * 时间分段工具类
 *
 * @author leeib
 */
public class TimeUtilForConversation {
    private static TimeUtilForConversation timeUtil;

    private TimeUtilForConversation() {

    }

    public static TimeUtilForConversation getInstance() {
        if (timeUtil == null) {
            timeUtil = new TimeUtilForConversation();
        }
        return timeUtil;
    }

    /**
     * 将对应的时间格式化
     *
     * @param timestamp 时间戳
     * @return 格式为 "下午 4：15"、 "凌晨 3：16"、 "上午 11：15"、
     * "晚上 10：15","周一  下午 4：15","10月12日   下午 4：15"
     * ,"2010年10月12日  下午 4：15"
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
            time = getWeekTime(day_of_week);
        } else if (year == tyear) {// 本年
            time = getYearTime(month, day_of_month);
        } else if (year < tyear) {// 去年以前
            time = getFreYearTime(year, month, day_of_month);
        } else {// 今天以后
            time = getFreYearTime(year, month, day_of_month);
        }

        return time;
    }

    /**
     * 将当前时间格式化，条件是当前时间与上一次记录时间的时间间隔大于等于timeSpace
     *
     * @param lastRecordTime 上一次记录的时间
     * @param timeSpace      当前时间与上一次记录时间的时间间隔，单位毫秒
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

    public boolean needRecordTime(long lastRecordTime, long timeSpace) {
        long currTime = System.currentTimeMillis();
        if (Math.abs(currTime - lastRecordTime) < timeSpace) {
            return false;
        }
        return true;
    }

    private String getDayTime(Calendar calendar) {
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return time;
    }

    private String getDayTime(int hour_of_day, int minute) {
        String time = hour_of_day + ":" + minute;
        return time;
    }

    private String getWeekTime(int day_of_week) {
        String time = "";
        if (Calendar.SUNDAY == day_of_week) {
            time = "星期日";
        } else if (Calendar.SATURDAY == day_of_week) {
            time = "星期六";
        } else if (Calendar.FRIDAY == day_of_week) {
            time = "星期五";
        } else if (Calendar.THURSDAY == day_of_week) {
            time = "星期四";
        } else if (Calendar.WEDNESDAY == day_of_week) {
            time = "星期三";
        } else if (Calendar.TUESDAY == day_of_week) {
            time = "星期二";
        } else if (Calendar.MONDAY == day_of_week) {
            time = "星期一";
        }
        return time;
    }

    private String getYearTime(int month, int day_of_month) {
        return month + "/" + day_of_month;
    }

    private String getFreYearTime(int year, int month, int day_of_month) {
        return year + "/" + month + "/" + day_of_month;
    }

}
