package com.nmoumoulidis.opensensor.restInterface.requests;

public class DefaultSetLocationRequest extends RestRequest 
{
	private static final String METHOD = "PUT";
	private static final  String RELATIVE_URL = "/location";
	private static final  String ACCEPT = "text/html";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	private String data;
	
	public DefaultSetLocationRequest(String newLocation) {
		super(PARAMETERS);
		
		// Set the data for the PUT request.
		this.data = newLocation;
	}

	/**
	 * Copy Constructor.
	 */
	public DefaultSetLocationRequest(DefaultSetLocationRequest clonedRequest) {
		super(clonedRequest);
		this.data = clonedRequest.data;
	}

	public String getData() {
		return data;
	}
}