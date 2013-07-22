package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DataValidator 
{
	private String data;
	private JSONParser jsonParser;
	private int dataLost;
	private int allData;
	
	public DataValidator(String data) {
		this.data = data;
		this.jsonParser = new JSONParser(data, this);
		this.dataLost = 0;
		this.allData = 0;
	}

	public ArrayList<HashMap<String,String>> validateBatchData() {
		// Validation is performed within the parser in this case.
		return jsonParser.parseData();
	}

	public int getDataLossPercentage() {
		return (dataLost * 100) / allData;
	}
	
	public void incrementAllData() {
		this.allData++;
	}
	
	public void incrementDatalost() {
		this.dataLost++;
	}

	public ArrayList<String> validateSensorList() throws InvalidSensorException {
		boolean isListValid = true;
		ArrayList<String> sensorList = jsonParser.parseSensorList();
		
		for(int i=0 ; i<sensorList.size() ; i++) {
			if(!SensorDictionary.isValidSensor(sensorList.get(i))) {
				isListValid = false;
			}
		}
	
		if(isListValid) {
			return sensorList;
		}
		else {
			throw new InvalidSensorException("Exception: The provided sensorlist "
											+"contains an invalid sensor...");
		}
	}

	public String validateRealTimeData() throws NumberFormatException {
		// This will throw an exception if the data is invalid.
		Double.valueOf(this.data);
		// If it doesn't, return the data.
		return data;
	}

	/* 
	 * Could implement datetime validation
	 * and
	 * Location validation (coordinate validation).
	 * */
}
