package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

public class DatePickerListenerTo implements DatePickerDialog.OnDateSetListener, OnClickListener
{
	private ConnectedSensorActivity conSensActivity;
	
	public DatePickerListenerTo(ConnectedSensorActivity conSensActivity) {
		this.conSensActivity = conSensActivity;
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		conSensActivity.getQueryBuilder().setDateToSearch(year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onClick(View v) {
		if(v == conSensActivity.getPickDateToButton()) {
			DialogFragment newFragment = new DatePickerFragment(conSensActivity, "to");
			newFragment.show(conSensActivity.getSupportFragmentManager(), "datePickerTo");
		}
	}
}
