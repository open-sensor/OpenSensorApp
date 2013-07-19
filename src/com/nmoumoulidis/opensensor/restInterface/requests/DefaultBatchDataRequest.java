package com.nmoumoulidis.opensensor.restInterface.requests;

public class DefaultBatchDataRequest extends RestRequest {
	private static final String BASE_URL = "";
	private static final String METHOD = "GET";
	private static final  String RELATIVE_URL = "/data";
	private static final  String ACCEPT = "application/json";
	private static final String[] PARAMETERS = {BASE_URL, METHOD, RELATIVE_URL, ACCEPT};
	
	public DefaultBatchDataRequest(String host, String port) {
		super(PARAMETERS);

		// Update the baseUrl with the constructor params.
		this.baseUrl = "http://"+host+":"+port+"/senseapi";
	}
	
	/**
	 * Copy Constructor.
	 */
	public DefaultBatchDataRequest(DefaultBatchDataRequest clonedRequest) {
		super(clonedRequest);
		this.baseUrl = clonedRequest.getBaseUrl();
	}
	
	/**
	 * Copy Constructor.
	 */
	public DefaultBatchDataRequest(RestRequest clonedRequest) {
		super(clonedRequest);
		this.baseUrl = clonedRequest.getBaseUrl();
		this.relativeUrl = clonedRequest.getRelativeUrl();
	}
}
