package com.nmoumoulidis.opensensor.model;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class SearchDataListViewAdapter 
{
	private ConnectedSensorActivity conSensActivity;
	
	public SearchDataListViewAdapter(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
	}
	
	public void populateListView(Cursor cursor) {
		String[] from = new String[] { DatabaseHelper.KEY_DATE, 
										DatabaseHelper.KEY_LOCATION, 
										DatabaseHelper.KEY_SENSOR_TYPE,
										DatabaseHelper.KEY_DATA_VALUE };
		int[] to = new int[] { R.id.date_column,
								R.id.location_column,
								R.id.sensor_type_column,
								R.id.sensor_value_column };
		
		 SimpleCursorAdapter adapter = new SimpleCursorAdapter(conSensActivity,
			        R.layout.relative_list_layout, cursor, from, to, 0);
		 conSensActivity.getHistoryDataListView().setAdapter(adapter);
	}
}