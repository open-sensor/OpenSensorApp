package com.nmoumoulidis.opensensor.restInterface.requests;

/**
 * Struct-styled class for storing attributes of generic REST requests towards 
 * the OpenSensor Station. Extended as a parent class by specific-request
 * type classes (e.g. batch-data, sensor-list).
 * @author Nikos Moumoulidis
 *
 */
public class SensorStationRestRequest 
{
	protected static String hostname = "slug.lan";
	protected final static String port = "81";
	protected final static String baseUrlString = "/senseapi";
	
	protected String baseUrl = "http://"+hostname+":"+port+baseUrlString;
	protected String method;
	protected String relativeUrl;
	protected String accept;
	
	public SensorStationRestRequest(String[] parameters) {
		this.method = parameters[0];
		this.relativeUrl = parameters[1];
		this.accept = parameters[2];
	}

	/**
	 * Copy Constructor.
	 */
	public SensorStationRestRequest(SensorStationRestRequest clonedRequest) {
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