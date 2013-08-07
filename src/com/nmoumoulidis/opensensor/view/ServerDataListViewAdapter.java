package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.SimpleAdapter;

import com.nmoumoulidis.opensensor.R;

public class ServerDataListViewAdapter 
{
	private ServerActivity serverActivity;
	
	public ServerDataListViewAdapter(ServerActivity serverActivity) {
		this.serverActivity = serverActivity;
	}
	
	public void populateListView(ArrayList<HashMap<String,String>> data) {
		String[] from = new String[] { "date",
										"location",
										"sensor_name",
										"avg_value",
										"min_value",
										"max_value" };
		int[] to = new int[] { R.id.server_date_column,
								R.id.server_location_column,
								R.id.server_sensor_name_column,
								R.id.server_avg_column,
								R.id.server_min_column,
								R.id.server_max_column };
		
		SimpleAdapter adapter = new SimpleAdapter(serverActivity, data,
			        R.layout.server_result_layout, from, to);
		 serverActivity.getResultsListView().setAdapter(adapter);
	}
	
	public void detachAdapterFromListView() {
		serverActivity.getResultsListView().setAdapter(null);
	}
}
