package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	/*
	 * 通过字符串获得时间戳
	 */
	public static Long getTimeStamp(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d;
		Long l = null;
		try {
			d = sdf.parse(user_time);
			l = d.getTime();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return l;

	}

	public static String getTime(String user_time) {
		String re_time = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d;
		Long l = null;
		try {
			d = sdf.parse(user_time);
			l = getTimeStamp(user_time);
			long lcc_time = Long.valueOf(l.toString());
			re_time = sdf.format(new Date(lcc_time));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return re_time;

	}

	/* 将字符串转为时间戳 */
	public static long getStringToDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	/**
	 * @Description描述:判断某一个时间点是否在某一个时间段内
	 * @Author作者:dbj
	 * @Date日期:2016-2-01 上午:10:28
	 */
	public static Boolean isPeriodTime(int startHour, int startMinute, int endHour, int endMinute) {
		Calendar cal = Calendar.getInstance();// 当前日期
		int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
		int minute = cal.get(Calendar.MINUTE);// 获取分钟
		int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
		final int start = startHour * 60 + startMinute * 60;// 起始时间 17:20的分钟数
		final int end = endHour * 60 + endMinute * 60;// 结束时间 19:00的分钟数
		if (minuteOfDay >= start && minuteOfDay <= end) {
			System.out.println("在外围内");
			return true;
		} else {
			System.out.println("在外围外");
			return false;
		}

	}

}
