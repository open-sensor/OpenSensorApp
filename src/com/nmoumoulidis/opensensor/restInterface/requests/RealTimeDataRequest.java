package com.nmoumoulidis.opensensor.restInterface.requests;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class RealTimeDataRequest extends RestRequest 
{
	private static final String BASE_URL = "";
	private static final String METHOD = "GET";
	private static final  String RELATIVE_URL = "/data";
	private static final  String ACCEPT = "text/html";
	private static final String[] PARAMETERS = {BASE_URL, METHOD, RELATIVE_URL, ACCEPT};
	
	private String hostname;
	private String port;
	private String sensor;
	
	public RealTimeDataRequest(String host, String port, String sensor, ConnectedSensorActivity conSensActivity) 
							throws InvalidSensorException, NonAvailSensorException {
		super(PARAMETERS);
		
		SensorTracker sensorTrack = conSensActivity.getmSensorTracker();
		
		// Will throw one of 2 custom exceptions...
		sensorTrack.isSensorAvailable(sensor);
		
		// Update the baseUrl with the constructor params.
		this.baseUrl = "http://"+host+":"+port+"/senseapi";
		this.relativeUrl = RELATIVE_URL + "/" + sensor;
	}
	
	/**
	 * Copy Constructor.
	 */
	public RealTimeDataRequest(RealTimeDataRequest clonedRequest) {
		super(clonedRequest);
		this.hostname = clonedRequest.hostname;
		this.port = clonedRequest.port;
		this.sensor = clonedRequest.sensor;
		this.baseUrl = clonedRequest.getBaseUrl();
		this.relativeUrl = clonedRequest.getRelativeUrl();
	}
	
	/**
	 * Copy Constructor.
	 */
	public RealTimeDataRequest(RestRequest clonedRequest) {
		super(clonedRequest);
		this.baseUrl = clonedRequest.getBaseUrl();
		this.relativeUrl = clonedRequest.getRelativeUrl();
	}
	
	public String getHostname() {
		return hostname;
	}

	public String getPort() {
		return port;
	}

	public String getSensor() {
		return sensor;
	}
}
