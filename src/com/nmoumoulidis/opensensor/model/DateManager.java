package com.nmoumoulidis.opensensor.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateManager
{
	private DateManager() {

	}

	public static String getTodayString() {
		SimpleDateFormat oldFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date today = oldFormat.parse(new Date().toString());
			return newFormat.format(today);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getTodayDate() {
		SimpleDateFormat oldFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		try {
			Date today = oldFormat.parse(new Date().toString());
			return today;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSevenDaysBeforeString() {
		Date today = getTodayDate();
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, -7);
		String sevenDaysBefore = newFormat.format(calendar.getTime());
		return sevenDaysBefore;
	}
	
	public static String getAMonthBeforeString() {
		Date today = getTodayDate();
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -1);
		String aMonthBefore = newFormat.format(calendar.getTime());
		return aMonthBefore;
	}
}
