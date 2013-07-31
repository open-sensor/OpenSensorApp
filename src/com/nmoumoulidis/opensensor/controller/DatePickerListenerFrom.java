package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.model.processing.DateManager;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;
import com.nmoumoulidis.opensensor.view.ConnSensDatePickerFragment;
import com.nmoumoulidis.opensensor.view.ServerActivity;
import com.nmoumoulidis.opensensor.view.ServerDatePickerFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

public class DatePickerListenerFrom implements DatePickerDialog.OnDateSetListener, OnClickListener 
{
	private ConnectedSensorActivity conSensActivity;
	private ServerActivity serverActivity = null;
	
	public DatePickerListenerFrom(Activity activity) {
		if(activity.getClass() == ConnectedSensorActivity.class) {
			this.conSensActivity = (ConnectedSensorActivity) activity;
		}
		else if(activity.getClass() == ServerActivity.class){
			this.serverActivity = (ServerActivity) activity;
		}
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		String modifiedDate = DateManager.fixDatePickerFormat(year, monthOfYear, dayOfMonth);
		if(this.conSensActivity != null) {
			conSensActivity.getQueryBuilder().setDateFromSearch(modifiedDate);
		}
		if(this.serverActivity != null) {
			serverActivity.getQueryBuilder().setDateFromSearch(modifiedDate);
		}
	}

	@Override
	public void onClick(View v) {
		if(this.conSensActivity != null && v == conSensActivity.getPickDateFromButton()) {
			ConnSensDatePickerFragment newFragment = ConnSensDatePickerFragment.newInstance("from");
			newFragment.show(conSensActivity.getSupportFragmentManager(), "connSensDatePickerFrom");
		}
		else if(this.serverActivity != null && v == serverActivity.getDateFromButton()) {
			ServerDatePickerFragment newFragment = ServerDatePickerFragment.newInstance("from");
			newFragment.show(serverActivity.getSupportFragmentManager(), "serverDatePickerFrom");
		}
	}
}