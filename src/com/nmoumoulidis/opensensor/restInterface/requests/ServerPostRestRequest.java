package com.nmoumoulidis.opensensor.restInterface.requests;

public class ServerPostRestRequest 
{
	private final static String hostname = "dyn-205-245.cs.st-andrews.ac.uk";
//	private final static String port = "80";
	private final static String baseUrlString = "/senseapi/data";
	private final static String method = "POST";
	private final static String accept = "text/html";
	private final static String baseUrl = "http://"+hostname+baseUrlString;
	private String dataToSend="";
	
	public ServerPostRestRequest(String data) {
		this.dataToSend = data;
	}

	/**
	 * Copy Constructor.
	 */
	public ServerPostRestRequest(ServerPostRestRequest clonedRequest) {
		this.dataToSend = clonedRequest.dataToSend;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getData() {
		return dataToSend;
	}

	public String getMethod() {
		return method;
	}

	public String getAccept() {
		return accept;
	}
}
