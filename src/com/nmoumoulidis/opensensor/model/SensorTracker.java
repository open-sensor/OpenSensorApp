package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores/Tracks the list of currently available sensors on the Wi-Fi connected
 * OpenSensor Station, and provides utility functions for them.
 * @author Nikos Moumoulidis
 *
 */
public class SensorTracker implements Parcelable
{
	private ArrayList<String> connectedSensorList;
//	private ArrayList<Sensor> phoneSensorList;

	public SensorTracker() {
		this.connectedSensorList = new ArrayList<String>();
//		this.phoneSensorList = new ArrayList<Sensor>();
	}
	
	public SensorTracker(Parcel source) {
		connectedSensorList = new ArrayList<String>();
		source.readStringList(connectedSensorList);
//		this.phoneSensorList = new ArrayList<Sensor>();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringList(connectedSensorList);
	}
	
    public static final Parcelable.Creator<SensorTracker> CREATOR
	    = new Parcelable.Creator<SensorTracker>() {
    	
		@Override
		public SensorTracker createFromParcel(Parcel in) {
		    return new SensorTracker(in);
		}
		
		@Override
		public SensorTracker[] newArray(int size) {
		    return new SensorTracker[size];
		}
	};
	
	// ----------------------- Connected Sensor Management ----------------------------
	public ArrayList<String> getConnectedSensorList() {
		return connectedSensorList;
	}

	public String getStringSensorList() {
		String availableSensorsString="";
		for(int i=0 ; i<connectedSensorList.size() ; i++) {
			availableSensorsString += connectedSensorList.get(i)+" ";
		}
		return availableSensorsString;
	}

	public ArrayList<String> getConnectedSensorNames() {
		ArrayList<String> sensorNames = new ArrayList<String>();
		for(int i=0 ; i<connectedSensorList.size() ; i++) {
			String newValidName = SensorDictionary.validSensorNames.get(connectedSensorList.get(i));
			sensorNames.add(newValidName);
		}
		return sensorNames;
	}
	
	public String findSensorByName(String name) {
		for(int i=0 ; i<SensorDictionary.validSensors.length ; i++) {
				if(name.equals(SensorDictionary.validSensorNames.get(SensorDictionary.validSensors[i]))) {
					return SensorDictionary.validSensors[i];
				}
		}
		return null;
	}

	// Returns true if the sensor list was set successfully and false
	// if not (due to invalidity of one or more sensor names in the list).
	public void setConnectedSensorList(ArrayList<String> connectedSensorList) {
		this.connectedSensorList = connectedSensorList;
	}

	// Checks if a wifi connected sensor is available (and in extent valid).
	public boolean isSensorAvailable(String sensor) 
			throws NonAvailSensorException, InvalidSensorException {
		for(int i=0 ; i<connectedSensorList.size() ; i++) {
			if(sensor.equals(connectedSensorList.get(i))) {
				return true;
			}
		}
		
		// If sensor is not valid...
		if(!SensorDictionary.isValidSensor(sensor)) {
			throw new InvalidSensorException();
		}// If sensor is not available...
		else {
			throw new NonAvailSensorException();
		}
	}

/*	
	// ----------------------- Phone Sensor Management ----------------------------
	// Overloaded
	// Checks if a phone sensor is available.
	public boolean isSensorAvailable(Sensor sensor) {
		for(int i=0 ; i<phoneSensorList.size() ; i++) {
			if(sensor.getType() == phoneSensorList.get(i).getType()) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Sensor> getPhoneSensorList() {
		return phoneSensorList;
	}
	*/
}
