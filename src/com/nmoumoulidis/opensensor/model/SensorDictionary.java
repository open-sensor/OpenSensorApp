package com.nmoumoulidis.opensensor.model;

public final class SensorDictionary 
{
	public static final String TEMPERATURE = "temp";
	public static final String HUMIDITY = "humid";
	public static final String LIGHT = "light";
	public static final String PRESSURE = "press";
	public static final String MAGNETIC_FIELD = "magn";
	public static final String SOUND = "sound";
	public static final String CARBON_DIOXIDE_GAS = "carbdx";
	
	private static final String[] validSensors = { TEMPERATURE, 
													HUMIDITY,
													LIGHT,
													PRESSURE,
													MAGNETIC_FIELD,
													SOUND,
													CARBON_DIOXIDE_GAS};
	
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
