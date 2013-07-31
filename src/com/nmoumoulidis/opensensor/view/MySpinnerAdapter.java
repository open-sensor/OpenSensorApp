package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;

import com.nmoumoulidis.opensensor.model.SensorDictionary;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class MySpinnerAdapter 
{
	private ConnectedSensorActivity conSensActivity = null;
	private ServerActivity serverActivity = null;
	private ArrayList<String> allStoredSensorsArray;
	private ArrayList<String> allStoredSensorNamesArray;
	private ArrayList<String> allSensorNamesArray;
	private ArrayAdapter<String> sensorAdapter;
	
	public MySpinnerAdapter(Activity activity) {
		if(activity.getClass() == ConnectedSensorActivity.class) {
			this.conSensActivity = (ConnectedSensorActivity) activity;
		}
		else if(activity.getClass() == ServerActivity.class) {
			this.serverActivity = (ServerActivity) activity;
		}
	}
	
	public void populateSpinner() {
		if(conSensActivity != null) {
			allStoredSensorsArray = conSensActivity.getDbHelper().getAllStoredSensorTypes();
			allStoredSensorNamesArray = new ArrayList<String>();
			for(int i=0 ; i<allStoredSensorsArray.size() ; i++) {
				allStoredSensorNamesArray.add(SensorDictionary.validSensorNames.get(allStoredSensorsArray.get(i)));
			}
			
			// Link the spinner with the appropriate sensor names...
			sensorAdapter = new ArrayAdapter<String>(
					conSensActivity, 
					android.R.layout.simple_spinner_item,
					allStoredSensorNamesArray);
			sensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
			conSensActivity.getDataSpinner().setAdapter(sensorAdapter);
		}

		if(serverActivity != null) {
			allSensorNamesArray = new ArrayList<String>();
			allSensorNamesArray.add("All");
			for(int i=0 ; i<SensorDictionary.validSensors.length ; i++) {
				allSensorNamesArray.add(SensorDictionary.validSensorNames.get(SensorDictionary.validSensors[i]));
			}

			// Link the spinner with the all the sensor names from the Sensor Dictionary...
			sensorAdapter = new ArrayAdapter<String>(
					serverActivity,
					android.R.layout.simple_spinner_item,
					allSensorNamesArray);
			sensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
			serverActivity.getDataSpinner().setAdapter(sensorAdapter);
		}
	}
	
	public ArrayList<String> getAllStoredSensorsArray() {
		return allStoredSensorsArray;
	}
	
	public String getFirstSensorDisplayed() {
		if(allStoredSensorsArray.size() > 0) {
			return allStoredSensorsArray.get(0);
		}
		return null;
	}
}