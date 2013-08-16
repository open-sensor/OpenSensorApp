package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.net.SocketException;
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
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationBatchDataRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.ServerPostRestRequest;

import android.app.IntentService;
import android.content.Intent;

/** 
 * A key class of the system.
 * Service that is fired up upon the app's launch and runs in the background. It does 
 * not stop upon the app being closed by the user or destroyed by the OS.
 * Everything running within this type of Android Service, runs on its own thread.
 * It only stops when the data have reached their final destination successfully
 * (the remote OpenSensor Server). Connects to the OpenSensor Station,
 * performs a REST request that retrieves batch data in JSON format. Upon handling the 
 * response, the data is validated, stored locally on the app's SQLite DB, and then sent
 * to the remote server using another REST request with the help 
 * of {@link BatchDataSendToServerServiceHelper}. The final POST REST reuqest is performed
 * persistently. That is, a loop is used that will only stop when the remote server is
 * reachable and the data have been sent successfully. 
 * @author Nikos Moumoulidis
 *
 */
public class BatchDataRetrieveService extends IntentService
{
	public static final String ACTION_BATH_REQ_FINISHED 
								= "com.nmoumoulidis.opensensor.BATCH_REQUEST_FINISHED";
	public static final String BATCH_DATA_OUT = "BATCH_DATA";
	public static boolean isRunning = false;
	
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
		isRunning = true;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("Intent for IntentService Fired!...");
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
		batchDataRequest = new SensorStationBatchDataRequest();
		databaseHelper = new DatabaseHelper(this);

		performRequest();
		if(response == null) {
			return;
		}
		handleResponse();
	}

	/**
	 * HTTP GET REST request.
	 */
	private void performRequest() {
		try {
			httpGet = new HttpGet(batchDataRequest.getBaseUrl() 
					+ batchDataRequest.getRelativeUrl());
			httpGet.setHeader("Accept", batchDataRequest.getAccept());
			response = httpClient.execute(httpGet, localContext);
		} catch (ClientProtocolException e) {
			System.out.println("Batch data request failed...");
			return;
		} catch (UnknownHostException e) {
			System.out.println("Batch data request failed...");
			return;
		} catch (SocketException e) {
			System.out.println("Batch data request failed...");
			return;
		} catch (IOException e) {
			System.out.println("Batch data request failed...");
			return;
		}
	}

	/**
	 * HTTP response handling.
	 */
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
			System.out.println("START data validation...");
			DataValidator batchDataValidator = new DataValidator(body);
			
			try {
				newBatchData = batchDataValidator.validateBatchDataFromSensorStation();
			} catch (JSONException e) {
				System.out.println("Error: Corrupt Batch Data...");
				return;
			}
			System.out.println("Data validation STOPPED...");
			
			System.out.println("BATCH DATA response handled!");
			System.out.println("Data Loss %: "+batchDataValidator.getDataLossPercentage());
			System.out.println("BATCH DATA now is being sent to the server!");
			// Transform the data in (validated) JSON format again...
			String data = batchDataValidator.getValidatedBatchDataAsJSONString();
			// Send them to the server...
			ServerPostRestRequest postRequest = new ServerPostRestRequest(data);
			BatchDataSendToServerServiceHelper serviceHelper 
										= new BatchDataSendToServerServiceHelper(postRequest);
			boolean sentToServerOk;
			do {
				sentToServerOk = serviceHelper.performRequest();
			}while(!sentToServerOk);
			serviceHelper.handleResponse();
			
			System.out.println("START insert to database...");
			databaseHelper.insertBatchData(newBatchData);
			System.out.println("Insert to database STOPPED...");
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
