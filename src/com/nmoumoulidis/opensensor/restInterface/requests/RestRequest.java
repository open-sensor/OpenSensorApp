package com.nmoumoulidis.opensensor.restInterface.requests;

import com.nmoumoulidis.opensensor.view.MainActivity;

public class RestRequest 
{
	protected final static String hostname = "slug";
	protected final static String port = "81";
	protected final static String baseUrlString = "/senseapi";
	
	protected String baseUrl = "http://"+hostname+":"+port+baseUrlString;
	protected String method;
	protected String relativeUrl;
	protected String accept;
	protected MainActivity mainActivity;
	
	public RestRequest(String[] parameters) {
		this.method = parameters[0];
		this.relativeUrl = parameters[1];
		this.accept = parameters[2];
	}

	/**
	 * Copy Constructor.
	 */
	public RestRequest(RestRequest clonedRequest) {
		this.method = clonedRequest.getMethod();
		this.relativeUrl = clonedRequest.getRelativeUrl();
		this.accept = clonedRequest.getAccept();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getMethod() {
		return method;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}

	public String getAccept() {
		return accept;
	}
}