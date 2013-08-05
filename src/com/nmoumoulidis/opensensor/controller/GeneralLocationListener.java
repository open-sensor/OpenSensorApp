package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.view.MainActivity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class GeneralLocationListener implements LocationListener 
{
	private MainActivity mainActivity;
	
	public GeneralLocationListener(MainActivity mActivity) {
		this.mainActivity = mActivity;
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		if(loc != null) {
			mainActivity.makeToast(loc);
		}
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(mainActivity, "Gps Disabled", Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(mainActivity, "Gps Enabled", Toast.LENGTH_SHORT ).show();
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
