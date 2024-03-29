package com.nmoumoulidis.opensensor.model.processing;

import java.util.ArrayList;

import com.nmoumoulidis.opensensor.model.SensorDictionary;
import com.nmoumoulidis.opensensor.view.ServerActivity;

/**
 * Struct-styled class for storing query parameters and providing full
 * query for REST requests to the OpenSensor Server.
 * @author Nikos Moumoulidis
 *
 */
public class ServerSearchQueryBuilder 
{
	private ServerActivity serverActivity;
	private ArrayList<String> sensorsArray;
	private String sensorToSearch = null;
	
	private String DateTo = null;
	private String DateFrom = null;
	
	public ServerSearchQueryBuilder(ServerActivity serverActivity) {
		this.serverActivity = serverActivity;
		this.sensorsArray = new ArrayList<String>();
		for(int i=0 ; i<SensorDictionary.validSensors.length ; i++) {
			sensorsArray.add(SensorDictionary.validSensors[i]);
		}
	}

	public void setSensorToSearch(String sensorName) {
		if(sensorName.equals("All")) {
			this.sensorToSearch = null;
			return;
		}
		for(int i=0 ; i<sensorsArray.size() ; i++) {
			if(sensorName.equals(SensorDictionary.validSensorNames.get(sensorsArray.get(i)))) {
				this.sensorToSearch = sensorsArray.get(i);
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
	
	public String getLocation() {
		return serverActivity.getLocationEditText().getText().toString();
	}
	
	public void clearSearchFilters() {
		serverActivity.getLocationEditText().setText("");
		serverActivity.getDataSpinner().setSelection(0);
		this.sensorToSearch = null;
		this.DateTo = null;
		this.DateFrom = null;
	}
	
	public String getURLQueryPart() {
		String query="";
		if(sensorToSearch != null){
			query += "&sensor_name="+this.sensorToSearch;
		}
		if(DateFrom != null) {
			query += "&datefrom="+this.DateFrom;
		}
		if(DateTo != null) {
			query += "&dateto="+this.DateTo;
		}
		return query;
	}
}
