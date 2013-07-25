package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.model.processing.DateManager;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;
import com.nmoumoulidis.opensensor.view.DatePickerFragment;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

public class DatePickerListenerFrom implements DatePickerDialog.OnDateSetListener, OnClickListener 
{
	private ConnectedSensorActivity conSensActivity;
	
	public DatePickerListenerFrom(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		String modifiedDate = DateManager.fixDatePickerFormat(year, monthOfYear, dayOfMonth);
		conSensActivity.getQueryBuilder().setDateFromSearch(modifiedDate);
	}

	@Override
	public void onClick(View v) {
		if(v == conSensActivity.getPickDateFromButton()) {
			DialogFragment newFragment = new DatePickerFragment(conSensActivity, "from");
			newFragment.show(conSensActivity.getSupportFragmentManager(), "datePickerFrom");
		}
	}
}