package com.nmoumoulidis.opensensor.model;

/**
 * 
 * @author Nikos Moumoulidis
 *
 */
public class NonAvailSensorException extends Exception 
{
	private static final long serialVersionUID = 1L;

	private static final String message = "Exception: This sensor is not available.";
	
	public NonAvailSensorException() {
		super(message);
	}
	
	public NonAvailSensorException(String msg) {
		super(msg);
	}
}
