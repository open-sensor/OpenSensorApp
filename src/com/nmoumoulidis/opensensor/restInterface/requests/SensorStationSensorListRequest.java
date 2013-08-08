package com.nmoumoulidis.opensensor.restInterface.requests;

/**
 * Struct-styled class specifically storing REST request attributes for 
 * sensor-list requests towards the OpenSensor Station.
 * @author Nikos Moumoulidis
 *
 */
public class SensorStationSensorListRequest extends SensorStationRestRequest 
{
	private static final String METHOD = "GET";
	private static final  String RELATIVE_URL = "/sensorlist";
	private static final  String ACCEPT = "application/json";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	public SensorStationSensorListRequest() {
		super(PARAMETERS);
	}

	/**
	 * Copy Constructor.
	 */
	public SensorStationSensorListRequest(SensorStationSensorListRequest clonedRequest) {
		super(clonedRequest);
	}
}
