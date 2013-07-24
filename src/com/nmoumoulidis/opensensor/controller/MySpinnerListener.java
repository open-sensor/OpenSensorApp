package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MySpinnerListener implements OnItemSelectedListener 
{
	private ConnectedSensorActivity conSensActivity;
	
	public MySpinnerListener(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int arg2, long arg3) {
		conSensActivity.getQueryBuilder().setSensorToSearch(parent.getItemAtPosition(arg2).toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
