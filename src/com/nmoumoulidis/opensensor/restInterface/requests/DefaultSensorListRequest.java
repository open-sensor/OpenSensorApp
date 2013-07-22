package com.nmoumoulidis.opensensor.restInterface.requests;

public class DefaultSensorListRequest extends RestRequest 
{
	private static final String METHOD = "GET";
	private static final  String RELATIVE_URL = "/sensorlist";
	private static final  String ACCEPT = "application/json";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	public DefaultSensorListRequest() {
		super(PARAMETERS);
	}

	/**
	 * Copy Constructor.
	 */
	public DefaultSensorListRequest(DefaultSensorListRequest clonedRequest) {
		super(clonedRequest);
	}
}
