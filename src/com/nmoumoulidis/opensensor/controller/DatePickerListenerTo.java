package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.model.processing.DateManager;
import com.nmoumoulidis.opensensor.view.ConnectedSensorActivity;
import com.nmoumoulidis.opensensor.view.DatePickerFragment;

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
		String modifiedDate = DateManager.fixDatePickerFormat(year, monthOfYear, dayOfMonth);
		conSensActivity.getQueryBuilder().setDateToSearch(modifiedDate);
	}

	@Override
	public void onClick(View v) {
		if(v == conSensActivity.getPickDateToButton()) {
			DialogFragment newFragment = new DatePickerFragment(conSensActivity, "to");
			newFragment.show(conSensActivity.getSupportFragmentManager(), "datePickerTo");
		}
	}
}
