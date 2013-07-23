package com.nmoumoulidis.opensensor.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateManager 
{	
	private DateManager() {

	}
	
	public static String getToday() {
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
	
	public static String getAWeekBeforeDate() {
		SimpleDateFormat oldFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date today = oldFormat.parse(new Date().toString());
	//		Calendar.get
	//		Date someday = new Date("");
			return newFormat.format(today);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
