package com.nmoumoulidis.opensensor.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.nmoumoulidis.opensensor.view.SensorStationActivity;
import com.nmoumoulidis.opensensor.view.MainActivity;
import com.nmoumoulidis.opensensor.view.ServerActivity;
import com.nmoumoulidis.opensensor.view.PhoneSensActivity;

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
			intent.putExtra("is_sensor_connected", mMainActivity.isWifiSensorConnected());
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
	}
}