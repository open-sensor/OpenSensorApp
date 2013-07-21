package com.nmoumoulidis.opensensor.controller;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;

import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.RestRequestTask;
import com.nmoumoulidis.opensensor.restInterface.requests.RealTimeDataRequest;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

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
		if(mConSensActivity.isSensorListObtained()) {
			try {
				for(int i=0 ; i<btnArray.length ; i++) {
					if(v == btnArray[i]) 
					{
						String sensorCommand = sensorTrack.findSensorByName((String) btnArray[i].getText());
						RealTimeDataRequest dataRequest = 
								new RealTimeDataRequest("slug", "81", sensorCommand, mConSensActivity);
						new RestRequestTask(mConSensActivity).execute(dataRequest);
						
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
		else {
			System.out.println("Something went very wrong: " +
					"sensor list initialization failed.");
		}
	}
}
