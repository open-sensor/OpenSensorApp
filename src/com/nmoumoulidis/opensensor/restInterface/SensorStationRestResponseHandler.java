package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.nmoumoulidis.opensensor.model.processing.DataValidator;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationRealTimeDataRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationRestRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationSetLocationRequest;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;

public class SensorStationRestResponseHandler
{
	private HttpEntity entity;
	private int statusCode;
	private String body;
	private SensorStationRestRequest sensorStationRestRequest;
	private HttpResponse restResponse;

	protected Boolean handleResponse(SensorStationRestRequest request, HttpResponse response) {
		this.sensorStationRestRequest = request;
		this.restResponse = response;
		this.statusCode = restResponse.getStatusLine().getStatusCode();
		this.entity = restResponse.getEntity();
		try {
			if(entity != null) {
				this.body = EntityUtils.toString(entity);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Everything went well.
		if(statusCode == 200) {
			//>>> If it was a real-time data request to a specific connected sensor.
			if(this.sensorStationRestRequest.getClass() == SensorStationRealTimeDataRequest.class) {
				//  ---------- Data Validation ----------
				DataValidator dataReadingValidator = new DataValidator(body);
				try {
					dataReadingValidator.validateRealTimeData();
				} catch (NumberFormatException e) {
					System.out.println(e.getMessage());
					// Return since we don't want the invalid data reading to be used.
					return false;
				}
			}
			//>>> If it was a set-loacation PUT request.
			else if(this.sensorStationRestRequest.getClass() == SensorStationSetLocationRequest.class) {
				//...
			}
			else {
				//>>> All the rest requests (e.g. GET location, GET datetime).
				if(this.sensorStationRestRequest.getMethod() == "GET") {
					return true;
				}
				else {
					//>>> We do not support other HTTP request methods.
					//>>> SensorStationRestRequest was used incorrectly (this should never happen).
					return false;
				}
			}
		}
		else {
			// HTTP request failed, try again.
			return false;
		}
		return true;
	}

    protected void postHandling(SensorStationActivity conSensActivity, Boolean success) {
		if(success) { 
			if(sensorStationRestRequest.getClass() == SensorStationRealTimeDataRequest.class) {
				conSensActivity.getmResultText().scrollTo(0, 0);
				conSensActivity.getmResultText().setText(body);
				System.out.println("real time data request handled!");
			}
			else if(sensorStationRestRequest.getClass() == SensorStationSetLocationRequest.class) {
				conSensActivity.getmResultText().scrollTo(0, 0);
				conSensActivity.getmResultText().setText("The location was successfully changed!");
				System.out.println("Set Location PUT request handled!");
			}
			else {
				conSensActivity.getmResultText().scrollTo(0, 0);
				conSensActivity.getmResultText().setText(body);
				System.out.println("Generic GET request handled!");
			}
		}
		else {
		// ============================= FAILURE SCENARIO ===============================
			if(sensorStationRestRequest.getClass() == SensorStationRealTimeDataRequest.class) {
				System.out.println("Request failed. Trying again...");
				// Recursively instantiate & execute SensorStationRealTimeDataRequest 
				// until the data reading is valid.
				// (Casting to subclass in order to access the subclass-only attributes).
				SensorStationRealTimeDataRequest oldRequest = new SensorStationRealTimeDataRequest((SensorStationRealTimeDataRequest) sensorStationRestRequest);
				new SensorStationRestRequestTask(conSensActivity).execute(oldRequest);
			}
		}
    }
}
