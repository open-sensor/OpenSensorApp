package com.nmoumoulidis.opensensor.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.nmoumoulidis.opensensor.view.AdminActivity;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;
import com.nmoumoulidis.opensensor.view.MainActivity;
import com.nmoumoulidis.opensensor.view.ServerActivity;
import com.nmoumoulidis.opensensor.view.PhoneSensActivity;

/**
 * Button listener for the main activity {@link MainActivity}.
 * Fires up new Activity based on the button clicked.
 * @author Nikos Moumoulidis
 *
 */
public class MainUIController implements OnClickListener {

	private MainActivity mMainActivity;
	
	public MainUIController(MainActivity mainActivity) {
		super();
		this.mMainActivity = mainActivity;
	}

	@Override
	public void onClick(View v) {
		if(v == mMainActivity.getmGoToConSensActivityBtn()) 
		{
			Intent intent = new Intent(mMainActivity, SensorStationActivity.class);
			
			Bundle bndl = new Bundle();
			bndl.putParcelable("sensor_tracker", mMainActivity.getmSensorTracker());
			intent.putExtras(bndl);
			intent.putExtra("is_sensorlist_obtained", mMainActivity.isSensorListObtained());
			mMainActivity.startActivity(intent);
		}
		else if(v == mMainActivity.getmGoToPhnSensActivityBtn())
		{
			Intent intent = new Intent(mMainActivity, PhoneSensActivity.class);
			mMainActivity.startActivity(intent);
		}
		else if(v == mMainActivity.getmGoToMapViewActivityBtn())
		{
			Intent intent = new Intent(mMainActivity, ServerActivity.class);
			mMainActivity.startActivity(intent);
		}
		else if(v == mMainActivity.getmGoToAdminActivityBtn())
		{
			Intent intent = new Intent(mMainActivity, AdminActivity.class);
			mMainActivity.startActivity(intent);
		}
	}
}