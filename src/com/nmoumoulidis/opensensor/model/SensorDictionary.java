package com.nmoumoulidis.opensensor.model;

import java.util.HashMap;

/**
 * Stores sensor codes and names of the OpenSensor system.
 * Can be extended in the future for support of more sensors.
 * Used as validation point for sensor codes/names retrieved from 
 * other parts of the system such as the OpenSensor Server and Station.
 * @author Nikos Moumoulidis
 *
 */
public final class SensorDictionary 
{
	public static final String TEMPERATURE = "temp";
	public static final String HUMIDITY = "humid";
	public static final String LIGHT = "light";
	public static final String PRESSURE = "press";
	public static final String MAGNETIC_FIELD = "magn";
	public static final String SOUND = "sound";
	public static final String CARBON_DIOXIDE_GAS = "carbdx";
	
	public static final String[] validSensors = { TEMPERATURE, 
													HUMIDITY,
													LIGHT,
													PRESSURE,
													MAGNETIC_FIELD,
													SOUND,
													CARBON_DIOXIDE_GAS};
	
	
	
	public static final HashMap<String, String> validSensorNames;
	static 
	{
		validSensorNames = new HashMap<String, String>();
		validSensorNames.put(TEMPERATURE, "Temperature");
		validSensorNames.put(HUMIDITY, "Humidity");
		validSensorNames.put(LIGHT, "Light");
		validSensorNames.put(PRESSURE, "Pressure");
		validSensorNames.put(MAGNETIC_FIELD, "Magnetic Field");
		validSensorNames.put(SOUND, "Sound");
		validSensorNames.put(CARBON_DIOXIDE_GAS, "Carbon Dioxide");
	}

	// Private constructor to prevent instantiation.
	private SensorDictionary() {
		
	}
	
	public static boolean isValidSensor(String sensor) {
		for (int i=0 ; i<validSensors.length ; i++) {
			if(sensor.equals(validSensors[i])) {
				return true;
			}
		}
		return false;
	}
}
