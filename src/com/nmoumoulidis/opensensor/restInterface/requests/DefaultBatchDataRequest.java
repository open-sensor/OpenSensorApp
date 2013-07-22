package com.nmoumoulidis.opensensor.restInterface.requests;

public class DefaultBatchDataRequest extends RestRequest 
{
	private static final String METHOD = "GET";
	private static final  String RELATIVE_URL = "/data";
	private static final  String ACCEPT = "application/json";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	public DefaultBatchDataRequest() {
		super(PARAMETERS);
	}
	
	/**
	 * Copy Constructor.
	 */
	public DefaultBatchDataRequest(DefaultBatchDataRequest clonedRequest) {
		super(clonedRequest);
	}
}
