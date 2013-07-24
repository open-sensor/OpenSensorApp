package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;

import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class SearchQueryBuilder 
{
	private ConnectedSensorActivity conSensActivity;
	private ArrayList<String> sensorsArray;
	private String sensorToSearch;
	
	private int[] DateTo = new int[3];
	private int[] DateFrom = new int[3];
	
	public SearchQueryBuilder(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
		this.sensorsArray = conSensActivity.getSpinnerAdapter().getAllSensorsArray();
		this.sensorToSearch = conSensActivity.getSpinnerAdapter().getFirstSensorDisplayed();
	}

	public void setSensorsArray(ArrayList<String> sensorsArray) {
		this.sensorsArray = sensorsArray;
	}

	public String getSensorToSearch() {
		return sensorToSearch;
	}

	public void setSensorToSearch(String sensorName) {
		String sensorType = conSensActivity.getmSensorTracker().findSensorByName(sensorName);
		for(int i=0 ; i<sensorsArray.size() ; i++) {
			if(sensorsArray.get(i).equals(sensorType)) {
				this.sensorToSearch = sensorType;
				return;
			}
		}
	}

	public void setDateToSearch(int year, int month, int day) {
		this.DateTo[0] = year;
		this.DateTo[1] = month + 1; // DatePicker months are indexed from 0 onwards..
		this.DateTo[2] = day;
	}
	
	public void setDateFromSearch(int year, int month, int day) {
		this.DateFrom[0] = year;
		this.DateFrom[1] = month + 1; // DatePicker months are indexed from 0 onwards..
		this.DateFrom[2] = day;
	}

	public String getDateTo() {
		return DateTo[2]+"-"+DateTo[1]+"-"+DateTo[0];
	}

	public String getDateFrom() {
		return DateFrom[2]+"-"+DateFrom[1]+"-"+DateFrom[0];
	}
}
