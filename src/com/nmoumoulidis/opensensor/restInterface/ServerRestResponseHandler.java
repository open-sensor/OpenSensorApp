package com.nmoumoulidis.opensensor.restInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.view.View;

import com.nmoumoulidis.opensensor.model.processing.JSONParser;
import com.nmoumoulidis.opensensor.view.ServerActivity;

public class ServerRestResponseHandler 
{
	private HttpEntity entity;
	private int statusCode;
	private String body;
	private HttpResponse restResponse;
	private ArrayList<HashMap<String,String>> data;
	private JSONParser jsonParser;
	public static final String NO_DATA = "no_data";
	public static final String INVALID_JSON = "invalid_json";
	public static final String SERVER_ERROR = "server_error";
	public static final String SERVER_NOT_REACHABLE = "server_not_reachable";
	
	private String failureReason;

	protected Boolean handleResponse(HttpResponse response) {
		this.restResponse = response;
		this.statusCode = restResponse.getStatusLine().getStatusCode();
		this.entity = restResponse.getEntity();
		try {
			if(entity != null) {
				this.body = EntityUtils.toString(entity);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(statusCode == 200) {
			try {
				jsonParser = new JSONParser(body);
				data = jsonParser.parseServerDataFromJSONToListOfMaps();
				if(data == null) {
					failureReason = NO_DATA;
					return false;
				}
				else {
					return true;
				}
			} catch (JSONException e) {
				failureReason = INVALID_JSON;
				System.out.println(" ----- INVALID JSON FROM SERVER ------ ");
				return false;
			}	
		}
		else {
			// This is really bad...
			failureReason = SERVER_ERROR;
			System.out.println(" ----- SERVER ERROR ------ ");
			return false;
		}
	}
	
    protected void postHandling(ServerActivity serverActivity, Boolean success) {
		if(success) {
			
			// Filter data based on nearby locations given by user...
			data = serverActivity.getGeocoder().filterDataByNearbyLocationsGivenByUser(data, serverActivity.getQueryBuilder().getLocation());
			if(data == null || data.size() == 0){
				serverActivity.getServerErrorInfo().setVisibility(View.VISIBLE);
				serverActivity.getServerErrorInfo().setText("No sensor data exist near the location you provided.");
				return;
			}
			
			// Replace location data coordinates with human-readable addresses...
			data = serverActivity.getGeocoder().replaceCoordsWithAddress(data);
			
			serverActivity.showSearchOptionsAgain(false);
			serverActivity.getShowSearchOptionsButton().setVisibility(View.VISIBLE);
			serverActivity.getListViewAdapter().populateListView(data);
			System.out.println("Server GET request handled successfully!");
			// Populate SimpleCursorAdapter..., show the list view... etc...
		}
		else {
			if(failureReason.equals(NO_DATA)) {
				serverActivity.getServerErrorInfo().setVisibility(View.VISIBLE);
				serverActivity.getServerErrorInfo().setText("Your search returned no relevant data.");
			}
			else if(failureReason.equals(SERVER_NOT_REACHABLE)) {
				serverActivity.getServerErrorInfo().setVisibility(View.VISIBLE);
				serverActivity.getServerErrorInfo().setText("Server unreachable. Please check internet connectivity...");
			}
			else {
				serverActivity.getServerErrorInfo().setVisibility(View.VISIBLE);
				serverActivity.getServerErrorInfo().setText("We're sorry, something went wrong. You can try again.");
			}
			System.out.println("Request to server failed... ");	
		}
    }
    
	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
}