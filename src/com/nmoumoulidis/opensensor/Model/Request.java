package com.nmoumoulidis.opensensor.Model;

public class Request 
{
	private String method;
	private String relativeUrl;
	private String accept;
	private String data;
	
	public Request(String[] parameters) {
		this.method = parameters[0];
		this.relativeUrl = parameters[1];
		this.accept = parameters[2];
		if(method.equals("PUT")) {
			this.data = parameters[3];
		}
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
