package com.nmoumoulidis.opensensor.controller;

import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;

import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.SensorStationRestRequestTask;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationRealTimeDataRequest;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;

/**
 * Button listener for the 
 * SensorStation activity {@link SensorStationActivity}.
 * @author Nikos Moumoulidis
 * 
 */
public class SensorStationUIController implements OnClickListener 
{
	private SensorStationActivity mConSensActivity;
	private Button[] btnArray;
	private SensorTracker sensorTrack;
	
	public SensorStationUIController(SensorStationActivity conSensActivity) {
		super();
		this.mConSensActivity = conSensActivity;
		this.btnArray = conSensActivity.getButtonArray();
		this.sensorTrack = conSensActivity.getmSensorTracker();
	}
	
	public void setBtnArray(Button[] btnArray) {
		this.btnArray = btnArray;
	}

	@Override
	public void onClick(View v) {
		if(v == mConSensActivity.getmGoToHistoryBtn()) {
			mConSensActivity.setRealTimeUIVisible(false);
		}
		else if(v == mConSensActivity.getBackToRealTimeButton()) {
			mConSensActivity.setRealTimeUIVisible(true);
		}
		else if(v == mConSensActivity.getSearchButton()) {
			mConSensActivity.getNoResultsTextView().setVisibility(View.GONE);
			mConSensActivity.getListAdapter().detachAdapterFromListView();
			
			String from = mConSensActivity.getQueryBuilder().getDateFrom();
			String to = mConSensActivity.getQueryBuilder().getDateTo();
			String sensor = mConSensActivity.getQueryBuilder().getSensorToSearch();
			Cursor newCursor = mConSensActivity.getDbHelper().getDetailedQueryCursor(sensor, from, to);
			if(newCursor == null) {
				mConSensActivity.getNoResultsTextView().setVisibility(View.VISIBLE);
			}
			else {
				Cursor usedCursor = mConSensActivity.getListAdapter().populateListView(newCursor);
				mConSensActivity.addUsedCursor(usedCursor);
			}
		}
		
		try {
			for(int i=0 ; i<btnArray.length ; i++) {
				if(v == btnArray[i]) 
				{	
					String sensorCommand = sensorTrack.findSensorByName((String) btnArray[i].getText());
					SensorStationRealTimeDataRequest dataRequest = 
							new SensorStationRealTimeDataRequest(sensorCommand, mConSensActivity);
					new SensorStationRestRequestTask(mConSensActivity).execute(dataRequest);
					
					mConSensActivity.getmLabelText().setText(btnArray[i].getText() + ": ");
				}
			}
		}
		catch (InvalidSensorException isE) {
			isE.printStackTrace();
		}
		catch (NonAvailSensorException nasE) {
			nasE.printStackTrace();
		}
	}
}
