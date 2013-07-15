package com.nmoumoulidis.opensensor.Controller;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.nmoumoulidis.opensensor.View.MainActivity;

import android.os.AsyncTask;
import android.view.View;

public class RestRequestTask extends AsyncTask<String, Integer, RestResponseHandler> {

	private MainActivity mainActivity;
	
	private HttpClient httpClient;
	private HttpContext localContext;
	private HttpGet httpGet;
	private HttpPut httpPut;
	private HttpResponse response;

	private RestResponseHandler localHandler;
	private String baseUrl;

	public RestRequestTask(MainActivity mainActivity, String hostname) {
		super();
		this.mainActivity = mainActivity;
		this.httpClient = new DefaultHttpClient();
		this.localContext = new BasicHttpContext();
		this.baseUrl = "http://"+hostname+"/senseapi";
	}

	@Override
	protected RestResponseHandler doInBackground(String... params) {
		Request request = new Request(params);
		try {
			publishProgress(15); //------ progress bar update ------
			if(request.getMethod().equals("GET")) {
				httpGet = new HttpGet(baseUrl + request.getRelativeUrl());
				httpGet.setHeader("Accept", request.getAccept());
				response = httpClient.execute(httpGet, localContext);
			}
			else if (request.getMethod().equals("PUT")) {
				httpPut = new HttpPut(baseUrl + request.getRelativeUrl());
				httpPut.setHeader("Accept", request.getAccept());
				
				// add the data entity
				StringEntity sEntity = new StringEntity(request.getData());
				httpPut.setEntity(sEntity);
				
				response = httpClient.execute(httpPut, localContext);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		publishProgress(70); //------ progress bar update ------
		publishProgress(80); //------ progress bar update ------
		localHandler = new RestResponseHandler(response, request);
		publishProgress(90); //------ progress bar update ------        
        publishProgress(100); //------ progress bar update ------
		return localHandler;
	}
	
	@Override
    protected void onPreExecute() {
      super.onPreExecute();
      mainActivity.getmProgressBar().setVisibility( View.VISIBLE);
    }
	
	@Override
	protected void onProgressUpdate(Integer... progress) {
		 super.onProgressUpdate( progress[0]);
	      mainActivity.getmProgressBar().setProgress(progress[0]);
	}
	
	@Override
    protected void onPostExecute(RestResponseHandler responseHandler) {
		responseHandler.handleResponse(mainActivity);
    	mainActivity.getmProgressBar().setVisibility( View.INVISIBLE);
    	mainActivity.getmProgressBar().setProgress(0);
    }
}
