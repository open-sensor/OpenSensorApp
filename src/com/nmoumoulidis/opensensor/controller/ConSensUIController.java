package com.nmoumoulidis.opensensor.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.nmoumoulidis.opensensor.model.InvalidSensorException;
import com.nmoumoulidis.opensensor.model.NonAvailSensorException;
import com.nmoumoulidis.opensensor.restInterface.RestRequestTask;
import com.nmoumoulidis.opensensor.restInterface.requests.RealTimeDataRequest;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

public class ConSensUIController implements OnClickListener 
{
	private ConnectedSensorActivity mConSensActivity;
	
	public ConSensUIController(ConnectedSensorActivity conSensActivity) {
		super();
		this.mConSensActivity = conSensActivity;
	}

	@Override
	public void onClick(View v) {
		if(mConSensActivity.isSensorListObtained()) {
			try {
				if(v == mConSensActivity.getmTempButton()) 
				{
					RealTimeDataRequest dataRequest = 
							new RealTimeDataRequest("slug", "81", "temp", mConSensActivity);
					new RestRequestTask(mConSensActivity).execute(dataRequest);
				}
				else if(v == mConSensActivity.getmHumidButton())
				{
					RealTimeDataRequest dataRequest = 
							new RealTimeDataRequest("slug", "81", "humid", mConSensActivity);
					new RestRequestTask(mConSensActivity).execute(dataRequest);
				}
				else if(v == mConSensActivity.getmLightButton())
				{
					RealTimeDataRequest dataRequest = 
							new RealTimeDataRequest("slug", "81", "light", mConSensActivity);
					new RestRequestTask(mConSensActivity).execute(dataRequest);
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
