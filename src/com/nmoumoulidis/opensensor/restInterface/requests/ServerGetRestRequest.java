package com.nmoumoulidis.opensensor.restInterface.requests;

public class ServerGetRestRequest 
{
	private final static String hostname = "dyn-205-245.cs.st-andrews.ac.uk";
//	private final static String port = "80";
	private final static String baseUrlString = "/senseapi/data";
	private final static String aggregateParameterString = "?aggregate=true";
	private final static String method = "GET";
	private final static String accept = "application/json";
	private final static String baseUrl = 
			"http://"+hostname+baseUrlString+aggregateParameterString;
	private String relativeUrl="";
	
	public ServerGetRestRequest(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	/**
	 * Copy Constructor.
	 */
	public ServerGetRestRequest(ServerGetRestRequest clonedRequest) {
		this.relativeUrl = clonedRequest.relativeUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}
	
	public String getFullUrl() {
		return baseUrl+relativeUrl;
	}

	public String getMethod() {
		return method;
	}

	public String getAccept() {
		return accept;
	}
}
