package com.nmoumoulidis.opensensor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class DataContainer
{
	ArrayList<HashMap<String,String>> data;
	
	public DataContainer() {
		data = new ArrayList<HashMap<String,String>>();
	}
	
	public void setData(ArrayList<HashMap<String,String>> newData) {
		this.data = newData;
		// Validate data for null keys and values, removing them.
		for (Iterator<HashMap<String,String>> iterator = data.iterator(); iterator.hasNext(); ) {
			HashMap<String,String> mapObject = iterator.next();
			if(mapObject.containsKey(null) || mapObject.containsValue(null)) {
				iterator.remove();
			}
		}
	}

	// TODO Persistent data storage using SQLite...

	// Utility method for data display testing.
	public String dataToString() {
		String dataString="";
		for(int i=0 ; i<data.size() ; i++) {
			dataString += JSONParser.NODE_DATETIME+": ";
			dataString += data.get(i).get(JSONParser.NODE_DATETIME)+"\n";
			dataString += JSONParser.NODE_LOCATION+": ";
			dataString += data.get(i).get(JSONParser.NODE_LOCATION)+"\n";
			dataString += JSONParser.NODE_TEMP+": ";
			dataString += data.get(i).get(JSONParser.NODE_TEMP)+"\n";
			dataString += JSONParser.NODE_HUMID+": ";
			dataString += data.get(i).get(JSONParser.NODE_HUMID)+"\n";
			dataString += JSONParser.NODE_LIGHT+": ";
			dataString += data.get(i).get(JSONParser.NODE_LIGHT)+"\n\n";
		}
		return dataString;
	}
}
