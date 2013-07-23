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

import com.nmoumoulidis.opensensor.model.DataValidator;
import com.nmoumoulidis.opensensor.model.DatabaseHelper;
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultBatchDataRequest;

import android.app.IntentService;
import android.content.Intent;

public class NetworkDataService extends IntentService 
{
	public static final String ACTION_BATH_REQ_FINISHED = "com.nmoumoulidis.opensensor.BATCH_REQUEST_FINISHED";
	public static final String BATCH_DATA_OUT = "BATCH_DATA";
	private HttpGet httpGet;
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpResponse response;
	
	private int statusCode;

	private HttpEntity entity;
	private String body;
	private ArrayList<HashMap<String,String>> newBatchData;
	private DefaultBatchDataRequest batchDataRequest;
	private DatabaseHelper databaseHelper;
	
	public NetworkDataService() {
		super("NetworkDataService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
		batchDataRequest = new DefaultBatchDataRequest();
		
		performRequest();
		if(response == null) {
			return;
		}
		handleResponse();
		sendIntentResponse();
	}

	private void performRequest() {
		try {
			httpGet = new HttpGet(batchDataRequest.getBaseUrl() + batchDataRequest.getRelativeUrl());
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
			newBatchData = batchDataValidator.validateBatchData();

			databaseHelper = new DatabaseHelper(this);
			databaseHelper.deleteAllBatchData();
			databaseHelper.insertBatchData(newBatchData);

			System.out.println("BATCH DATA response handled!");
			System.out.println("Data Loss %: "+batchDataValidator.getDataLossPercentage());
			
			// TODO  AT THIS POINT, SEND ALL DATA TO THE 'BIG SERVER'
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

	private void sendIntentResponse() {
		Intent intentResponse = new Intent();
		intentResponse.setAction(ACTION_BATH_REQ_FINISHED);
		intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
		intentResponse.putExtra(BATCH_DATA_OUT, newBatchData);
		sendBroadcast(intentResponse);
	}
	  
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(databaseHelper != null) {
			databaseHelper.close();
		}
	}
}
