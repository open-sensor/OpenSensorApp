package com.nmoumoulidis.opensensor.Controller;

import com.nmoumoulidis.opensensor.View.MainActivity;

import android.view.View;
import android.view.View.OnClickListener;

public class MainUIController implements OnClickListener {

	private MainActivity mMainActivity;
	
	public MainUIController(MainActivity mainActivity) {
		super();
		this.mMainActivity = mainActivity;
	}

	@Override
	public void onClick(View v) {
		if(v == mMainActivity.getmTempButton()) 
		{
			new RestRequestTask(mMainActivity, "slug:81")
			.execute("PUT", "/data/temp", "text/html", "...");
		}
		else if(v == mMainActivity.getmHumidButton())
		{
			new RestRequestTask(mMainActivity, "slug:81")
			.execute("GET", "/data", "application/json");
		}
		else if(v == mMainActivity.getmLightButton())
		{
			new RestRequestTask(mMainActivity, "slug:81")
			.execute("GET", "/sensorlist", "application/json");
		}
	}

}
