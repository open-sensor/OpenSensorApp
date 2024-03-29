package com.nmoumoulidis.opensensor.restInterface.requests;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;

/**
 * Struct-styled class specifically storing REST request attributes for 
 * real-time data requests towards the OpenSensor Station.
 * @author Nikos Moumoulidis
 *
 */
public class SensorStationRealTimeDataRequest extends SensorStationRestRequest 
{
	private static final String METHOD = "GET";
	private static final String RELATIVE_URL = "/data";
	private static final String ACCEPT = "text/html";
	private static final String[] PARAMETERS = {METHOD, RELATIVE_URL, ACCEPT};
	
	private String sensor;
	
	public SensorStationRealTimeDataRequest(String sensor, SensorStationActivity conSensActivity) 
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
	public SensorStationRealTimeDataRequest(SensorStationRealTimeDataRequest clonedRequest) {
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
