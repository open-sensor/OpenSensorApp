package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.nmoumoulidis.opensensor.model.processing.DataValidator;
import com.nmoumoulidis.opensensor.restInterface.requests.RestRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.sensorstation.DefaultSetLocationRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.sensorstation.RealTimeDataRequest;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class RestResponseHandlerTask extends AsyncTask<Object, Integer, Boolean>
{
	private HttpEntity entity;
	private int statusCode;
	private String body;
	private RestRequest restRequest;
	private HttpResponse restResponse;
	
	private ConnectedSensorActivity mConSensActivity;
	
	public RestResponseHandlerTask (ConnectedSensorActivity conSensActivity)  
	{
		this.mConSensActivity = conSensActivity;
	}

	@Override
	protected Boolean doInBackground(Object... object) {
		this.restRequest = (RestRequest) object[0];
		this.restResponse = (HttpResponse) object[1];
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
			if(this.restRequest.getClass() == RealTimeDataRequest.class) {
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
			else if(this.restRequest.getClass() == DefaultSetLocationRequest.class) {
				//...
			}
			else {
				//>>> All the rest requests (e.g. GET location, GET datetime).
				if(this.restRequest.getMethod() == "GET") {
					return true;
				}
				else {
					//>>> We do not support other HTTP request methods.
					//>>> RestRequest was used incorrectly (this should never happen).
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
	
	@Override
    protected void onPostExecute(Boolean success) {
		if(success) { 
			if(restRequest.getClass() == RealTimeDataRequest.class) {
				mConSensActivity.getmResultText().scrollTo(0, 0);
				mConSensActivity.getmResultText().setText(body);
				System.out.println("real time data request handled!");
			}
			else if(restRequest.getClass() == DefaultSetLocationRequest.class) {
				mConSensActivity.getmResultText().scrollTo(0, 0);
				mConSensActivity.getmResultText().setText("The location was successfully changed!");
				System.out.println("Set Location PUT request handled!");
			}
			else {
				mConSensActivity.getmResultText().scrollTo(0, 0);
				mConSensActivity.getmResultText().setText(body);
				System.out.println("Generic GET request handled!");
			}
		}
		else {
		// ============================= FAILURE SCENARIO ===============================
			if(restRequest.getClass() == RealTimeDataRequest.class) {
				System.out.println("Request failed. Trying again...");
				// Recursively instantiate & execute RealTimeDataRequest 
				// until the data reading is valid.
				// (Casting to subclass in order to access the subclass-only attributes).
				RealTimeDataRequest oldRequest = new RealTimeDataRequest((RealTimeDataRequest) restRequest);
				new RestRequestTask(mConSensActivity).execute(oldRequest);
			}
		}
    }
}
