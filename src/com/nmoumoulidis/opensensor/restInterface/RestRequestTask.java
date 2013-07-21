package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

import com.nmoumoulidis.opensensor.restInterface.requests.DefaultBatchDataRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultSensorListRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.RestRequest;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class RestRequestTask extends AsyncTask<RestRequest, Integer, RestRequest> {

	private ConnectedSensorActivity mConSensActivity;
	
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpGet httpGet;
	private HttpPut httpPut;
	private HttpResponse response;

	public RestRequestTask(ConnectedSensorActivity conSensActivity) {
		super();
		this.mConSensActivity = conSensActivity;
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
	}

	@Override
	protected RestRequest doInBackground(RestRequest... request) {
		RestRequest newRequest = request[0];
		try {
			if(newRequest.getMethod().equals("GET")) {
				httpGet = new HttpGet(newRequest.getBaseUrl() + newRequest.getRelativeUrl());
				httpGet.setHeader("Accept", newRequest.getAccept());
				response = httpClient.execute(httpGet, localContext);
			}
			else if (newRequest.getMethod().equals("PUT")) {
				httpPut = new HttpPut(newRequest.getBaseUrl() + newRequest.getRelativeUrl());
				httpPut.setHeader("Accept", newRequest.getAccept());
				
				// add the data entity
				StringEntity sEntity = new StringEntity(newRequest.getData());
				httpPut.setEntity(sEntity);

				response = httpClient.execute(httpPut, localContext);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace(); // Cannot find host name...
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newRequest;
	}

	@Override
    protected void onPostExecute(RestRequest newRequest) {
		RestResponseHandlerTask newTask = new RestResponseHandlerTask(mConSensActivity);
		Object[] obj = {newRequest, response};
		newTask.execute(obj);
		// If this request was used from the initialization task, and the
		// initialization has not yet finished, notify that this task finished.
		if(newRequest.getClass() == DefaultSensorListRequest.class) {
			System.out.println("Finished 'Sensor List Request' Task execution...\n");
		}
		else if(newRequest.getClass() == DefaultBatchDataRequest.class) {
			System.out.println("Finished 'All Data Request' Task execution...\n");
		}
    }
}
