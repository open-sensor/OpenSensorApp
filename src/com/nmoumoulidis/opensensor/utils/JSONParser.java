package com.nmoumoulidis.opensensor.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser 
{
	private String stringJSON = null;
	private JSONObject mainJsonObj = null;
	private JSONArray readings = null;
	private HashMap<String, String> dataMap = null;
	private ArrayList<HashMap<String, String>> dataList = null;
	
	public static final String NODE_DATETIME = "datetime";
	public static final String NODE_LOCATION = "location";
	public static final String NODE_TEMP = "temp";
	public static final String NODE_HUMID = "humid";
	public static final String NODE_LIGHT = "light";

	public JSONParser(String json) {
			this.stringJSON = json;
	}
	
	public ArrayList<HashMap<String, String>> parseData() {
		try {
			readings = new JSONArray(this.stringJSON);
			dataList = new ArrayList<HashMap<String, String>>();
			
			for(int i=0 ; i<readings.length() ; i++) {
				JSONObject obj = readings.getJSONObject(i);			
				try {
					// Exception thrown here would be due to invalid JSON node name.
					String datetime = obj.getString(NODE_DATETIME);
					String location = obj.getString(NODE_LOCATION);
					String temp = obj.getString(NODE_TEMP);
					String humid = obj.getString(NODE_HUMID);
					String light = obj.getString(NODE_LIGHT);
					
					// Validate that sensor values are numbers.
					// Exception thrown here would be due to invalid (trash) data
					//(i.e. a temperature value which is not a number).
					double tempTemp = obj.getDouble(NODE_TEMP);
					double tempHumid = obj.getDouble(NODE_HUMID);
					double tempLight = obj.getDouble(NODE_LIGHT); 
					
					// If data are valid, create a hashmap.
					dataMap = new HashMap<String, String>();
					dataMap.put(NODE_DATETIME, datetime);
					dataMap.put(NODE_LOCATION, location);
					dataMap.put(NODE_TEMP, temp);
					dataMap.put(NODE_HUMID, humid);
					dataMap.put(NODE_LIGHT, light);
				}
				catch (JSONException invalidNodeExc) {
					System.out.println(invalidNodeExc.getMessage());
					// If exception due to reading validation is thrown,
					// we skip this reading.
					continue;
				}
				dataList.add(dataMap); // Add the map object to the list.
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public ArrayList<String> parseSensorList() {
		ArrayList<String> sensorList = new ArrayList<String>();
		try {
			JSONArray jsonArray = new JSONArray(this.stringJSON);
			for(int i=0 ; i<jsonArray.length() ; i++) {
				sensorList.add(jsonArray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sensorList;
	}
}
