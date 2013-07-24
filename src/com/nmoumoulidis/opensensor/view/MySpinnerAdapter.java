package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;

import com.nmoumoulidis.opensensor.model.SensorDictionary;

import android.widget.ArrayAdapter;

public class MySpinnerAdapter 
{
	private ConnectedSensorActivity conSensActivity;
	private ArrayList<String> allSensorsArray;
	private ArrayList<String> allSensorNamesArray;
	
	public MySpinnerAdapter(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
	}
	
	public void populateSpinner() {
		allSensorsArray = conSensActivity.getDbHelper().getAllStoredSensorTypes();
		allSensorNamesArray = new ArrayList<String>();
		for(int i=0 ; i<allSensorsArray.size() ; i++) {
			allSensorNamesArray.add(SensorDictionary.validSensorNames.get(allSensorsArray.get(i)));
		}
		
		// Link the spinner with the appropriate sensor names...
		ArrayAdapter<String> sensorAdapter = new ArrayAdapter<String>(
				conSensActivity, 
				android.R.layout.simple_spinner_item,
				allSensorNamesArray);
		sensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		conSensActivity.getDataSpinner().setAdapter(sensorAdapter);
	}
	
	public ArrayList<String> getAllSensorsArray() {
		return allSensorsArray;
	}

	public String getFirstSensorDisplayed() {
		if(allSensorsArray.size() > 0) {
			return allSensorsArray.get(0);
		}
		return null;
	}
}