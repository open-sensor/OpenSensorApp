package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.nmoumoulidis.opensensor.model.DatabaseHelper;
import com.nmoumoulidis.opensensor.model.processing.DataValidator;
import com.nmoumoulidis.opensensor.restInterface.requests.sensorstation.SensorStationBatchDataRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.server.ServerPostRestRequest;

import android.app.IntentService;
import android.content.Intent;

public class BatchDataRetrieveService extends IntentService
{
	public static final String ACTION_BATH_REQ_FINISHED 
								= "com.nmoumoulidis.opensensor.BATCH_REQUEST_FINISHED";
	public static final String BATCH_DATA_OUT = "BATCH_DATA";
	private HttpGet httpGet;
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpResponse response;
	
	private int statusCode;

	private HttpEntity entity;
	private String body;
	private ArrayList<HashMap<String,String>> newBatchData;
	private SensorStationBatchDataRequest batchDataRequest;
	private DatabaseHelper databaseHelper;
	
	public BatchDataRetrieveService() {
		super("BatchDataRetrieveService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
		batchDataRequest = new SensorStationBatchDataRequest();
		
		performRequest();
		if(response == null) {
			return;
		}
		handleResponse();
	}

	private void performRequest() {
		try {
			httpGet = new HttpGet(batchDataRequest.getBaseUrl() 
					+ batchDataRequest.getRelativeUrl());
			httpGet.setHeader("Accept", batchDataRequest.getAccept());
			response = httpClient.execute(httpGet, localContext);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace(); // Cannot find host name...
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleResponse() {
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
			//  ---------- Data Validation & Usage ----------
			// Trash data are automatically removed by the validator.
			// The data are ready to be stored/used.
			DataValidator batchDataValidator = new DataValidator(body);
			
			try {
				newBatchData = batchDataValidator.validateBatchData();
			} catch (JSONException e) {
				System.out.println("Error: Corrupt Batch Data...");
				return;
			}
			
			databaseHelper = new DatabaseHelper(this);
			databaseHelper.deleteAllBatchData();
			databaseHelper.insertBatchData(newBatchData);

			System.out.println("BATCH DATA response handled!");
			System.out.println("Data Loss %: "+batchDataValidator.getDataLossPercentage());
			System.out.println("BATCH DATA now is being sent to the server!");
			
			
			// Transform the data in (validated) JSON format again...
			String data = batchDataValidator.getValidatedBatchDataAsJSONString();
			// Send them to the server...
			ServerPostRestRequest postRequest = new ServerPostRestRequest(data);
			BatchDataSendToServerServiceHelper serviceHelper 
										= new BatchDataSendToServerServiceHelper(postRequest);
			serviceHelper.performRequest();
			serviceHelper.handleResponse();
		}
		// <<< There was no batch data to receive at this time...
		else if(statusCode == 204) {
			System.out.println("There is no BATCH DATA to fetch...");
		}
		// <<< HTTP request failed -> Retry
		else {
			System.out.println("HTTP request failed. Retrying...");
			this.performRequest();
			this.handleResponse();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(databaseHelper != null) {
			databaseHelper.close();
		}
	}
}
