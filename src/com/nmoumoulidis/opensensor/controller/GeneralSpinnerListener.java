package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.view.SensorStationActivity;
import com.nmoumoulidis.opensensor.view.ServerActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class GeneralSpinnerListener implements OnItemSelectedListener 
{
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int arg2, long arg3) {
		if(parent.getContext().getClass() == SensorStationActivity.class) {
			((SensorStationActivity) parent.getContext()).getQueryBuilder().setSensorToSearch(parent.getItemAtPosition(arg2).toString());
		}
		else if(parent.getContext().getClass() == ServerActivity.class) {
			((ServerActivity) parent.getContext()).getQueryBuilder().setSensorToSearch(parent.getItemAtPosition(arg2).toString());
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
