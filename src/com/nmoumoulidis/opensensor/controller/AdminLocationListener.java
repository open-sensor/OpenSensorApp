package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.view.AdminActivity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Location-provider listener class. Listens for location changes using
 * the Android OS Location provider for Wi-Fi network triangulation location tracking.
 * @author Nikos Moumoulidis
 *
 */
public class AdminLocationListener implements LocationListener 
{
	private AdminActivity mAdminActivity;
	
	public int howManyTimesLocationChanged = 0;
	
	public AdminLocationListener(AdminActivity adminActivity) {
		this.mAdminActivity = adminActivity;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		howManyTimesLocationChanged++;
		System.out.println("ON LOCATION CHANGED..."+howManyTimesLocationChanged);
		if(howManyTimesLocationChanged == 2) {
			mAdminActivity.getmSetLocationFeedback().setText("Location acquired. Press the " +
					"'Set Location' button again to set the location on the OpenSensor Station.");
			mAdminActivity.setLocation(location.getLatitude()+";"+location.getLongitude());
			mAdminActivity.setLocationFound(true);
			mAdminActivity.getLocManager().removeUpdates(this);
			if(mAdminActivity.getmSearchingLocationDialog().isShowing()) {
				mAdminActivity.getmSearchingLocationDialog().dismiss();
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		System.out.println("PROVIDER DISABLED...");
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		System.out.println("PROVIDER ENABLED...");
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		System.out.println("PROVIDER STATUS CHANGED...");
	}

}
