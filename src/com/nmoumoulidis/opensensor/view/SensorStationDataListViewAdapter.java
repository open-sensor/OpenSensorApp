package com.nmoumoulidis.opensensor.view;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.model.DatabaseHelper;

/**
 * UI utility class, used to populate data results in a structured way using
 * the powerful {@link SimpleCursorAdapter} Android class.
 * @author Nikos Moumoulidis
 *
 */
public class SensorStationDataListViewAdapter 
{
	private SensorStationActivity conSensActivity;
	
	public SensorStationDataListViewAdapter(SensorStationActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
	}
	
	public Cursor populateListView(Cursor cursor) {
		String[] from = new String[] { DatabaseHelper.KEY_DATE,
										DatabaseHelper.FUNC_AVG_VALUE,
										DatabaseHelper.FUNC_MIN_VALUE,
										DatabaseHelper.FUNC_MAX_VALUE };
		int[] to = new int[] { R.id.date_column,
								R.id.avg_column,
								R.id.min_column,
								R.id.max_column};
		
		 SimpleCursorAdapter adapter = new SimpleCursorAdapter(conSensActivity,
			        R.layout.sensor_station_result_layout, cursor, from, to, 0);
		 conSensActivity.getHistoryDataListView().setAdapter(adapter);
		 return cursor;
	}
	
	public void detachAdapterFromListView() {
		conSensActivity.getHistoryDataListView().setAdapter(null);
	}
}