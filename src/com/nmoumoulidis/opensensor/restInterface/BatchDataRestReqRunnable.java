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
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultBatchDataRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.RestRequest;
import com.nmoumoulidis.opensensor.view.MainActivity;

public class BatchDataRestReqRunnable implements Runnable 
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
	private ArrayList<HashMap<String,String>> newBatchData;
	
	public BatchDataRestReqRunnable(MainActivity mainActivity, RestRequest newRequest) {
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
			//  ---------- Data Validation & Usage ----------
			// Trash data are automatically removed by the validator.
			// The data are ready to be stored/used.
			DataValidator batchDataValidator = new DataValidator(body);
			newBatchData = batchDataValidator.validateBatchData();
			mainActivity.getmDataContainer().setData(newBatchData);
			
			System.out.println("BATCH DATA response handled!");
			
			// TODO  AT THIS POINT, SEND ALL DATA TO THE 'BIG SERVER'
		}
		// <<< There was no batch data to receive at this time...
		else if(statusCode == 204) {
			
		}
		// <<< HTTP request failed -> Retry
		else {
			DefaultBatchDataRequest anotherBatchReq = new DefaultBatchDataRequest("slug", "81");
			BatchDataRestReqRunnable anotherBatchRunnable = 
					new BatchDataRestReqRunnable(mainActivity, anotherBatchReq);
			new Thread(anotherBatchRunnable).start();
		}
	}
}
