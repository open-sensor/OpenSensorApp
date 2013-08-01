package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.processing.DataValidator;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationRestRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationSensorListRequest;
import com.nmoumoulidis.opensensor.view.MainActivity;

public class SensorListRestRequestRunnable implements Runnable 
{
	private MainActivity mainActivity;
	private SensorStationRestRequest newRequest;
	private HttpGet httpGet;
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpResponse response;
	
	private int statusCode;

	private HttpEntity entity;
	private String body;
	
	private ArrayList<String> newSensorList;
	
	public SensorListRestRequestRunnable(MainActivity mainActivity, SensorStationRestRequest newRequest) {
		this.mainActivity = mainActivity;
		this.newRequest = newRequest;
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
	}

	@Override
	public void run() {
		// ========================= Perform the Request ================================ //
		try {
			httpGet = new HttpGet(newRequest.getBaseUrl() + newRequest.getRelativeUrl());
			httpGet.setHeader("Accept", newRequest.getAccept());
			response = httpClient.execute(httpGet, localContext);
		} catch (ClientProtocolException e) {
			mainActivity.setWifiSensorConnected(false);
			return;
		} catch (UnknownHostException e) {
			mainActivity.setWifiSensorConnected(false);
			return;
		} catch (IOException e) {
			mainActivity.setWifiSensorConnected(false);
			return;
		}

		if(response == null) {
			return;
		}
		
		// ========================= Handle the Response ================================ //
		this.statusCode = response.getStatusLine().getStatusCode();
		this.entity = response.getEntity();
		try {
			if(entity != null) {
				this.body = EntityUtils.toString(entity);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// <<< HTTP request succeeded -> process data
		if(statusCode == 200) {
			boolean success = true;
			//  ---------- Data Validation ----------
			DataValidator sensListValidator = new DataValidator(body);
			try {
				newSensorList = sensListValidator.validateSensorList();
			} catch (InvalidSensorException e) {
				System.out.println(e.getMessage());
				// Return since we don't want the invalid sensor list to be used.
				success = false;
			}
			
			if(success) {
				mainActivity.getmSensorTracker().setConnectedSensorList(newSensorList);
				mainActivity.setSensorListObtained(true);

				System.out.println("SENSOR LIST response handled!");
			}
			// <<< Validation failed -> Retry
			else {
				// Recursively instantiate & execute a new Thread that performs 
				// a new sensor list request, until the sensor list is valid.
				SensorStationSensorListRequest oldRequest = new SensorStationSensorListRequest((SensorStationSensorListRequest) newRequest);
				SensorListRestRequestRunnable anotherReqRunnable = new SensorListRestRequestRunnable(mainActivity, oldRequest);
			   	new Thread(anotherReqRunnable).start();
			}
		}
		// <<< HTTP request failed -> Retry
		else {
			// Recursively instantiate & execute a new Thread that performs 
			// a new sensor list request, until the sensor list is valid.
			SensorStationSensorListRequest oldRequest = new SensorStationSensorListRequest((SensorStationSensorListRequest) newRequest);
			SensorListRestRequestRunnable anotherReqRunnable = new SensorListRestRequestRunnable(mainActivity, oldRequest);
		   	new Thread(anotherReqRunnable).start();
		}
	}
}
