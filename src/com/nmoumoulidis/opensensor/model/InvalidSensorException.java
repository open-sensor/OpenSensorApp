package com.nmoumoulidis.opensensor.model;

public class InvalidSensorException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String message = "Exception: This sensor is not available.";
	
	public InvalidSensorException() {
		super(message);
	}
	
	public InvalidSensorException(String msg) {
		super(msg);
	}

}
