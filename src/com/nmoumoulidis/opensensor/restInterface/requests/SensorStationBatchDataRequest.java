package com.nmoumoulidis.opensensor.restInterface.requests;


public class SensorStationBatchDataRequest extends SensorStationRestRequest 
{
	private static final String METHOD = "GET";
	private static final  String RELATIVE_URL = "/data";
	private static final  String ACCEPT = "application/json";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	public SensorStationBatchDataRequest() {
		super(PARAMETERS);
	}
	
	/**
	 * Copy Constructor.
	 */
	public SensorStationBatchDataRequest(SensorStationBatchDataRequest clonedRequest) {
		super(clonedRequest);
	}
}
