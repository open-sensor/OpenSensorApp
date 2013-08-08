package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;

import com.nmoumoulidis.opensensor.model.SensorDictionary;

import android.app.Activity;
import android.widget.ArrayAdapter;

/**
 * UI utility class for populating the spinners (drop-down menus) that live
 * in the {@link SensorStationActivity} and {@link ServerActivity}.
 * @author Nikos Moumoulidis
 *
 */
public class GeneralSpinnerAdapter 
{
	private SensorStationActivity conSensActivity = null;
	private ServerActivity serverActivity = null;
	private ArrayList<String> allStoredSensorsArray;
	private ArrayList<String> allStoredSensorNamesArray;
	private ArrayList<String> allSensorNamesArray;
	private ArrayAdapter<String> sensorAdapter;
	
	public GeneralSpinnerAdapter(Activity activity) {
		if(activity.getClass() == SensorStationActivity.class) {
			this.conSensActivity = (SensorStationActivity) activity;
		}
		else if(activity.getClass() == ServerActivity.class) {
			this.serverActivity = (ServerActivity) activity;
		}
	}
	
	/**
	 * The drop-down menu for {@link SensorStationActivity} is populated based on 
	 * the available sensor types on the just-retrieved data from the Wi-Fi connected
	 * OpenSensor Station, that are stored in the on-phone SQLite database.
	 */
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