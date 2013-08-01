package com.nmoumoulidis.opensensor.restInterface.requests;


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
