package com.nmoumoulidis.opensensor.Controller;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.text.Html;

import com.nmoumoulidis.opensensor.Model.Request;
import com.nmoumoulidis.opensensor.View.MainActivity;
import com.nmoumoulidis.opensensor.utils.JSONParser;

public class RestResponseHandler 
{
	private HttpEntity entity;
	
	private int statusCode;
	private String contentType;
	private String statusReasonPhrase;
	private String body;
	private Request request;
	
	public RestResponseHandler(HttpResponse response, Request request) {
		this.request = request;
		this.statusCode = response.getStatusLine().getStatusCode();
		this.statusReasonPhrase = response.getStatusLine().getReasonPhrase();
		this.contentType = response.getLastHeader("Content-Type").getValue();
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
	}

	public void handleResponse(MainActivity mainActivity) {
		if(statusCode == 200) {
			if(request.getMethod() == "PUT") {
				mainActivity.getmResultText().setText("The location was successfully changed!");
			}
			else {
				if(request.getAccept() == "application/json") {
					JSONParser jParser = new JSONParser(body);
					
					if(request.getRelativeUrl() == "/sensorlist") {
						ArrayList<String> sensorList = jParser.parseSensorList();
						mainActivity.getmSensorTracker().setWifiConnectedSensorList(sensorList);
						
						ArrayList<String> newSensorList = mainActivity.
								getmSensorTracker().getWifiConnectedSensorList();
						String availableSensorsString="";
						for(int i=0 ; i<newSensorList.size() ; i++) {
							availableSensorsString += newSensorList.get(i)+" ";
						}
						mainActivity.getmResultText().scrollTo(0, 0);
						mainActivity.getmResultText().setText("sensorlist: "+availableSensorsString);
					}
					else if(request.getRelativeUrl() == "/data"){
						mainActivity.getmDataContainer().setData(jParser.parseData());
						String dataString  = mainActivity.getmDataContainer().dataToString();
						mainActivity.getmResultText().scrollTo(0, 0);
						mainActivity.getmResultText().setText("DATA!: "+dataString);
					}
				}
				else {
					mainActivity.getmResultText().setText(body);
				}
			}
		}
		else if(statusCode == 204) {
			mainActivity.getmResultText().setText(statusCode + " " +statusReasonPhrase);
		}
		else {
			mainActivity.getmResultText().setText(Html.fromHtml(body));
		}
	}
}
