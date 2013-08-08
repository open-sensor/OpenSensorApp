package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.net.SocketException;
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

import android.app.Activity;
import android.os.AsyncTask;

import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationRestRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationSetLocationRequest;
import com.nmoumoulidis.opensensor.view.AdminActivity;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;

/**
 * An Android AsyncTask for all the REST (GET and PUT) requests (except the batch-data request and 
 * sensor-list request) that are performed towards the OpenSensor Station.
 * All AsyncTasks are queued by the OS. The response is passed down to a {@link SensorStationRestResponseHandler}
 * object to handle.
 * @author Nikos Moumoulidis
 *
 */
public class SensorStationRestRequestTask extends AsyncTask<SensorStationRestRequest, Void, Boolean> {

	private SensorStationActivity mConSensActivity = null;
	private AdminActivity mAdminActivity = null;
	
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpGet httpGet;
	private HttpPut httpPut;
	private HttpResponse response;
	
	SensorStationRestResponseHandler responseHandler;

	public SensorStationRestRequestTask(Activity activity) {
		super();
		if(activity.getClass() == AdminActivity.class) {
			this.mAdminActivity = (AdminActivity) activity;
		}
		else if(activity.getClass() == SensorStationActivity.class) {
			this.mConSensActivity = (SensorStationActivity) activity;
		}
		
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
	}

	@Override
    protected void onPreExecute() {
		if(mAdminActivity != null) {
			mAdminActivity.getmSearchingLocationDialog().setMessage("Setting Location to the OpenSensor Station...");
			mAdminActivity.getmSearchingLocationDialog().show();
		}
		else if(mConSensActivity != null) {
			mConSensActivity.getRequestLoadingDialog().show();
		}
    }
	
	@Override
	protected Boolean doInBackground(SensorStationRestRequest... request) {
		SensorStationRestRequest newRequest = request[0];
		responseHandler = new SensorStationRestResponseHandler();
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
				StringEntity sEntity = new StringEntity(((SensorStationSetLocationRequest) newRequest).getData());
				httpPut.setEntity(sEntity);
				
				// To avoid the obscure Lighttpd server bug that gives HTTP status error code: 417
				// the expect-continue header is set to false (Lighttpd does not support it)...
				httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
				response = httpClient.execute(httpPut, localContext);
			}
		} catch (ClientProtocolException e) {
			responseHandler.setFailureReason(SensorStationRestResponseHandler.SENSOR_STATION_NOT_REACHABLE);
			System.out.println("ClientProtocolException caught");
		} catch (UnknownHostException e) {
			responseHandler.setFailureReason(SensorStationRestResponseHandler.SENSOR_STATION_NOT_REACHABLE);
			System.out.println("UnknownHostException caught");
			return false;
		} catch (SocketException e) {
			responseHandler.setFailureReason(SensorStationRestResponseHandler.SENSOR_STATION_NOT_REACHABLE);
			System.out.println("SocketException caught");
			return false;
		} catch (IOException e) {
			responseHandler.setFailureReason(SensorStationRestResponseHandler.SENSOR_STATION_NOT_REACHABLE);
			System.out.println("IOException caught");
			return false;
		}
		boolean successOrNot = responseHandler.handleResponse(newRequest, response);

		return Boolean.valueOf(successOrNot);
	}

	@Override
    protected void onPostExecute(Boolean successOrNot) {
		boolean handlingWasOk = successOrNot.booleanValue();
		if(mConSensActivity != null) {
			responseHandler.postHandling(mConSensActivity, handlingWasOk);
		}
		if(mAdminActivity != null) {
			responseHandler.postHandling(mAdminActivity, handlingWasOk);
		}
		
    }
}
