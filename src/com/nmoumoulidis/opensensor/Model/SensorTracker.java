package com.nmoumoulidis.opensensor.Model;

import java.util.ArrayList;

import android.hardware.Sensor;

public class SensorTracker 
{
	private ArrayList<String> wifiConnectedSensorList;
	private ArrayList<Sensor> phoneSensorList;

	public SensorTracker() {
		this.wifiConnectedSensorList = new ArrayList<String>();
		this.phoneSensorList = new ArrayList<Sensor>();
	}
	
	public SensorTracker(ArrayList<String> connectedSensorList) {
		this.wifiConnectedSensorList = connectedSensorList;
		this.phoneSensorList = new ArrayList<Sensor>();
	}
	
	public ArrayList<String> getWifiConnectedSensorList() {
		return wifiConnectedSensorList;
	}

	public void setWifiConnectedSensorList(ArrayList<String> wifiConnectedSensorList) {
		this.wifiConnectedSensorList = wifiConnectedSensorList;
	}

	public ArrayList<Sensor> getPhoneSensorList() {
		return phoneSensorList;
	}
	
}
