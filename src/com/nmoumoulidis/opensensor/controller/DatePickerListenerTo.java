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

public class DatePickerListenerTo implements DatePickerDialog.OnDateSetListener, OnClickListener
{
	private ConnectedSensorActivity conSensActivity = null;
	private ServerActivity serverActivity = null;
	
	public DatePickerListenerTo(Activity activity) {
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
			conSensActivity.getQueryBuilder().setDateToSearch(modifiedDate);
		}
		if(this.serverActivity != null) {
			serverActivity.getQueryBuilder().setDateToSearch(modifiedDate);
		}
	}

	@Override
	public void onClick(View v) {
		if(this.conSensActivity != null && v == conSensActivity.getPickDateToButton()) {
			ConnSensDatePickerFragment newFragment = ConnSensDatePickerFragment.newInstance("to");
			newFragment.show(conSensActivity.getSupportFragmentManager(), "connSensDatePickerTo");
		}
		else if(this.serverActivity != null && v == serverActivity.getDateToButton()) {
			ServerDatePickerFragment newFragment = ServerDatePickerFragment.newInstance("to");
			newFragment.show(serverActivity.getSupportFragmentManager(), "serverDatePickerTo");
		}
	}
}
