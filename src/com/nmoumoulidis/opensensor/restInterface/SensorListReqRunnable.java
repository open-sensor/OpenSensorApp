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

import com.nmoumoulidis.opensensor.model.DataValidator;
import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultSensorListRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.RestRequest;
import com.nmoumoulidis.opensensor.view.MainActivity;

public class SensorListReqRunnable implements Runnable 
{
	private MainActivity mainActivity;
	private RestRequest newRequest;
	private HttpGet httpGet;
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpResponse response;
	
	private int statusCode;

	private HttpEntity entity;
	private String body;
	
	private ArrayList<String> newSensorList;
	
	public SensorListReqRunnable(MainActivity mainActivity, RestRequest newRequest) {
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
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace(); // Cannot find host name...
		} catch (IOException e) {
			e.printStackTrace();
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
				DefaultSensorListRequest oldRequest = new DefaultSensorListRequest(newRequest);
				SensorListReqRunnable anotherReqRunnable = new SensorListReqRunnable(mainActivity, oldRequest);
			   	new Thread(anotherReqRunnable).start();
			}
		}
		// <<< HTTP request failed -> Retry
		else {
			// Recursively instantiate & execute a new Thread that performs 
			// a new sensor list request, until the sensor list is valid.
			DefaultSensorListRequest oldRequest = new DefaultSensorListRequest(newRequest);
			SensorListReqRunnable anotherReqRunnable = new SensorListReqRunnable(mainActivity, oldRequest);
		   	new Thread(anotherReqRunnable).start();
		}
	}
}
