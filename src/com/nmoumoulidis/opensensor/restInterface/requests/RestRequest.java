package com.nmoumoulidis.opensensor.restInterface.requests;

import com.nmoumoulidis.opensensor.view.MainActivity;

public class RestRequest 
{
	protected String baseUrl;
	protected String method;
	protected String relativeUrl;
	protected String accept;
	protected String data;
	protected MainActivity mainActivity;
	
	public RestRequest(String[] parameters) {
		this.baseUrl = parameters[0];
		this.method = parameters[1];
		this.relativeUrl = parameters[2];
		this.accept = parameters[3];
	}
	
	/**
	 * Copy Constructor.
	 */
	public RestRequest(RestRequest clonedRequest) {
		this.baseUrl = clonedRequest.getBaseUrl();
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

	public String getData() {
		return data;
	}

}