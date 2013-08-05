package com.nmoumoulidis.opensensor.model.processing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public final class DateManager
{
	// Private constructor to prevent instantiation.
	private DateManager() {

	}

	public static String getTodayString() {
		SimpleDateFormat oldFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
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
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DATE, -7);
		String sevenDaysBefore = newFormat.format(calendar.getTime());
		return sevenDaysBefore;
	}
	
	public static String getAMonthBeforeString() {
		Date today = getTodayDate();
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.MONTH, -1);
		String aMonthBefore = newFormat.format(calendar.getTime());
		return aMonthBefore;
	}
	
	public static String fixDatePickerFormat(int year, int month, int day) {
		int monthPlusOne  = month + 1;
		String strMonth=""+monthPlusOne;
		String strDay=""+day;
		if(monthPlusOne < 10) {
			strMonth = "0"+monthPlusOne;
		}
		if(day < 10) {
			strDay = "0"+day;
		}

		return year+"-"+strMonth+"-"+strDay;
	}

	public static ArrayList<HashMap<String,String>> transformDateBeforeInsert(ArrayList<HashMap<String,String>> dataMap){
		SimpleDateFormat normalFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat stupidAmericanFormat = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0 ; i<dataMap.size() ; i++) {
    		// Transform datetime into date...
    		String date = dataMap.get(i).get("datetime").substring(0, 10);
    		dataMap.get(i).put("datetime", date);
    		
    		// Transform date from dd-MM-yyyy to yyyy-MM-dd...
			String tempDateString = dataMap.get(i).get("datetime");
			try {
				Date tempDate = normalFormat.parse(tempDateString);
				tempDateString = stupidAmericanFormat.format(tempDate);
				dataMap.get(i).put("datetime", tempDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dataMap;
	}
}
