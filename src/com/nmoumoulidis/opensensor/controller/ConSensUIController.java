package com.nmoumoulidis.opensensor.controller;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nmoumoulidis.opensensor.model.DateManager;
import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;

import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.RestRequestTask;
import com.nmoumoulidis.opensensor.restInterface.requests.RealTimeDataRequest;
import com.nmoumoulidis.opensensor.view.BatchDataViewActivity;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;
import com.nmoumoulidis.opensensor.view.MapViewActivity;

public class ConSensUIController implements OnClickListener 
{
	private ConnectedSensorActivity mConSensActivity;
	private Button[] btnArray;
	private SensorTracker sensorTrack;
	
	public ConSensUIController(ConnectedSensorActivity conSensActivity) {
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
	//		Intent intent = new Intent(mConSensActivity, BatchDataViewActivity.class);
	//		mConSensActivity.startActivity(intent);
			mConSensActivity.getDbHelper().printAllBatchData();
			System.out.println("-------------------------");
		}
		if(mConSensActivity.isSensorListObtained()) {
	//		try {
				for(int i=0 ; i<btnArray.length ; i++) {
					if(v == btnArray[i]) 
					{
						
						mConSensActivity.getDbHelper().getTodaysData();
						System.out.println("-------------------------");
						//	mConSensActivity.getDbHelper().printAllBatchData();
			/*			String sensorCommand = sensorTrack.findSensorByName((String) btnArray[i].getText());
						RealTimeDataRequest dataRequest = 
								new RealTimeDataRequest(sensorCommand, mConSensActivity);
						new RestRequestTask(mConSensActivity).execute(dataRequest);
						
						mConSensActivity.getmLabelText().setText(btnArray[i].getText() + ": "); */
					}
				}
/*			}
			catch (InvalidSensorException isE) {
				isE.printStackTrace();
			}
			catch (NonAvailSensorException nasE) {
				nasE.printStackTrace();
			} */
		}
		else { // Something went very wrong...
			System.out.println("ERROR: sensor list initialization failed...");
		}
	}
}
