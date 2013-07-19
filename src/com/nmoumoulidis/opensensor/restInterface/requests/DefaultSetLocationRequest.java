package com.nmoumoulidis.opensensor.restInterface.requests;

public class DefaultSetLocationRequest extends RestRequest 
{
	private static final String BASE_URL = "";
	private static final String METHOD = "PUT";
	private static final  String RELATIVE_URL = "/location";
	private static final  String ACCEPT = "text/html";
	private static final String[] PARAMETERS = {BASE_URL, METHOD, RELATIVE_URL, ACCEPT};
	
	public DefaultSetLocationRequest(String host, String port, String newLocation) {
		super(PARAMETERS);
		
		// Set the data for the PUT request.
		this.data = newLocation;
		// Update the baseUrl with the constructor params.
		this.baseUrl = "http://"+host+":"+port+"/senseapi";
	}
	
	/**
	 * Copy Constructor.
	 */
	public DefaultSetLocationRequest(DefaultSetLocationRequest clonedRequest) {
		super(clonedRequest);
		this.data = clonedRequest.data;
		this.baseUrl = clonedRequest.getBaseUrl();
	}

	/**
	 * Copy Constructor.
	 */
	public DefaultSetLocationRequest(RestRequest clonedRequest) {
		super(clonedRequest);
		this.baseUrl = clonedRequest.getBaseUrl();
		this.relativeUrl = clonedRequest.getRelativeUrl();
	}
}
