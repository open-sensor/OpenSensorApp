package com.nmoumoulidis.opensensor.restInterface.requests.sensorstation;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.requests.RestRequest;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class RealTimeDataRequest extends RestRequest 
{
	private static final String METHOD = "GET";
	private static final String RELATIVE_URL = "/data";
	private static final String ACCEPT = "text/html";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	private String sensor;
	
	public RealTimeDataRequest(String sensor, ConnectedSensorActivity conSensActivity) 
							throws InvalidSensorException, NonAvailSensorException {
		super(PARAMETERS);
		SensorTracker sensorTrack = conSensActivity.getmSensorTracker();
		
		// Will throw one of 2 custom exceptions...
		sensorTrack.isSensorAvailable(sensor);
		
		// Update the baseUrl with the constructor params.
		this.relativeUrl = RELATIVE_URL + "/" + sensor;
	}

	/**
	 * Copy Constructor.
	 */
	public RealTimeDataRequest(RealTimeDataRequest clonedRequest) {
		super(clonedRequest);
		this.sensor = clonedRequest.sensor;
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
