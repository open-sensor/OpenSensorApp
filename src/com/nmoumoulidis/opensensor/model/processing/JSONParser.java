package com.nmoumoulidis.opensensor.model.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nmoumoulidis.opensensor.model.SensorDictionary;

/**
 * Utility class. Provides transformations between JSON strings and Java data structures,
 * as well as performs JSON/data validation by removing the invalid values...
 * @author Nikos Moumoulidis
 *
 */
public class JSONParser 
{
	private String stringJSON = null;
	private JSONArray readings = null;
	private HashMap<String, String> dataMap = null;
	private ArrayList<HashMap<String, String>> dataList = null;
	private DataValidator dataValidator;
	private JSONObject topLevelServerJSONObject = null;
	private JSONArray mainDataJSONArray = null;
	
	public static final String NODE_DATETIME = "datetime";
	public static final String NODE_LOCATION = "location";
	
	private ArrayList<String> keyList = null;

	public JSONParser(String json, DataValidator validator) {
			this.stringJSON = json;
			this.dataValidator = validator;
			this.keyList = new ArrayList<String>();
	}
	
	public JSONParser(String json) {
		this.stringJSON = json;
	}

	public ArrayList<HashMap<String, String>> validateDataFromSensorStation() throws JSONException {
		try {
			dataList = new ArrayList<HashMap<String, String>>();
			readings = new JSONArray(this.stringJSON);
			
			for(int i=0 ; i<readings.length() ; i++) {
				JSONObject obj = readings.getJSONObject(i);
				
				// In order to track data loss...
				this.dataValidator.incrementAllData();
				
				// Run through the object's keys and validate them.
				Iterator<String> iter = (Iterator<String>)obj.keys();
				boolean areKeysValid = true;
				while(iter.hasNext()) {
					String key = iter.next();
					if(!key.equals("datetime") && !key.equals("location")) {
						if(!SensorDictionary.isValidSensor(key)) {
							areKeysValid = false;
						}
					}
				}
				
				// If the keys of the object are valid...
				if(areKeysValid) {
					// If its the first object: add all the keys in the keyList.
					if(i == 0) {
						iter = (Iterator<String>)obj.keys();
						while(iter.hasNext()) {
							String key = iter.next();
							if(!key.equals("datetime") && !key.equals("location")) {
								keyList.add(key);
							}
						}
					}

					// Parse through them
					try {
						String datetime = obj.getString(NODE_DATETIME);
						String location = obj.getString(NODE_LOCATION);
						for(int j=0 ; j<keyList.size() ; j++) {
							if(obj.getString(keyList.get(j)).equals(null)
									|| obj.getString(keyList.get(j)).equals("")) {
								throw new JSONException("empty or null data value");
							}
							// Validate that sensor values are numbers.
							// Exception thrown here would be due to invalid (trash) data
							//(i.e. a temperature value which is not a number).
							double tempVariable = obj.getDouble(keyList.get(j));
						}
						
						// If data are valid, create a hashmap and add them to it.
						dataMap = new HashMap<String, String>();
						dataMap.put(NODE_DATETIME, datetime);
						dataMap.put(NODE_LOCATION, location);
						for(int j=0 ; j<keyList.size() ; j++) {
							dataMap.put(keyList.get(j), obj.getString(keyList.get(j)));
						}
					}
					catch (JSONException invalidNodeExc) {
						System.out.println(invalidNodeExc.getMessage());
						
						// In order to track data loss...
						this.dataValidator.incrementDatalost();
						// Exception due to reading validation is thrown, we skip this reading.
						continue;
					}
					dataList.add(dataMap); // Add the map object to the list.
				}
				else {
					// In order to track data loss...
					this.dataValidator.incrementDatalost();
					// keys are not valid, we skip this reading.
					continue;
				}
			}
		} catch (JSONException e) {
			throw e;
		}
		return dataList;
	}
	
	public String transformSensorStationDataBackToJSON(ArrayList<HashMap<String, String>> dataList) {
		ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		JSONObject newObj;
		for(int i=0 ; i<dataList.size() ; i++) {
			newObj = new JSONObject(dataList.get(i));
			jsonObjectList.add(newObj);
		}
		
		JSONArray jsArray = new JSONArray(jsonObjectList);
		return jsArray.toString();
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
	
	public ArrayList<HashMap<String, String>> parseServerDataFromJSONToListOfMaps() throws JSONException {
			dataList = new ArrayList<HashMap<String, String>>();
			JSONObject tempObject;
			HashMap<String,String> map;
			topLevelServerJSONObject = new JSONObject(this.stringJSON);
			if(topLevelServerJSONObject.get("resultsEmpty").equals("false")) {	
				mainDataJSONArray = topLevelServerJSONObject.getJSONArray("data");
				for(int i=0 ; i<mainDataJSONArray.length() ; i++) {
					try {
						tempObject = mainDataJSONArray.getJSONObject(i);
						map = new HashMap<String,String>();
						map.put("date", tempObject.get("date").toString());
						map.put("location", tempObject.get("location").toString());
						String userFriendlyName = SensorDictionary.validSensorNames.get(tempObject.get("sensor_name").toString());
						map.put("sensor_name", userFriendlyName);
						map.put("avg_value", tempObject.get("avg_value").toString());
						map.put("min_value", tempObject.get("min_value").toString());
						map.put("max_value", tempObject.get("max_value").toString());
						dataList.add(map);
					} catch (JSONException e) {
						// We skip this data object...
						continue;
					}
				}
				return dataList;
			}
			else {
				System.out.println("JSONException Captured...");
				return null;
			}
	}

}
