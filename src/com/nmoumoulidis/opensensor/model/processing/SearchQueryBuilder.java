package com.nmoumoulidis.opensensor.model.processing;

import java.util.ArrayList;

import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class SearchQueryBuilder 
{
	private ConnectedSensorActivity conSensActivity;
	private ArrayList<String> sensorsArray;
	private String sensorToSearch;
	
	private String DateTo;
	private String DateFrom;
	
	public SearchQueryBuilder(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
		this.sensorsArray = conSensActivity.getSpinnerAdapter().getAllStoredSensorsArray();
		
		// Set default sensor as the first one populated in the spinner.
		this.sensorToSearch = conSensActivity.getSpinnerAdapter().getFirstSensorDisplayed();
		// Set default dates as today...
		this.DateTo = DateManager.getTodayString();
		this.DateFrom = DateManager.getTodayString();
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

	public void setDateToSearch(String dateTo) {
		this.DateTo = dateTo;
	}
	
	public void setDateFromSearch(String dateFrom) {
		this.DateFrom = dateFrom;
	}

	public String getSensorToSearch() {
		return sensorToSearch;
	}
	
	public String getDateTo() {
		return DateTo;
	}

	public String getDateFrom() {
		return DateFrom;
	}
}
