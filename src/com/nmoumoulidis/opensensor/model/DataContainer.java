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
	}

	// Utility method for data display testing.
	public String dataToString() {
		String dataString="";
		for(int i=0 ; i<data.size() ; i++) {
			dataString += "datetime: "+ data.get(i).get("datetime") + "\n";
			dataString += "location: "+ data.get(i).get("location") + "\n";
		    Iterator it = data.get(i).entrySet().iterator();
		    while (it.hasNext()) {
		    	HashMap.Entry pairs = (HashMap.Entry)it.next();
		    	if(!pairs.getKey().equals("datetime") && !pairs.getKey().equals("location")) {
			        dataString += pairs.getKey() + ": " + pairs.getValue() + "\n";
		    	}
		    }
		}
		return dataString;
	}
}