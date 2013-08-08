package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.model.processing.DateManager;
import com.nmoumoulidis.opensensor.view.SensorStationActivity;
import com.nmoumoulidis.opensensor.view.SensorStationDatePickerFragment;
import com.nmoumoulidis.opensensor.view.ServerActivity;
import com.nmoumoulidis.opensensor.view.ServerDatePickerFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

/**
 * Listener class, implements listening for date-picker buttons and date selection
 * buttons for date-picker fragments. Associated with the "Date To" dates.
 * @author Nikos Moumoulidis
 *
 */
public class GeneralDatePickerListenerTo implements DatePickerDialog.OnDateSetListener, OnClickListener
{
	private SensorStationActivity conSensActivity = null;
	private ServerActivity serverActivity = null;
	
	public GeneralDatePickerListenerTo(Activity activity) {
		if(activity.getClass() == SensorStationActivity.class) {
			this.conSensActivity = (SensorStationActivity) activity;
		}
		else if(activity.getClass() == ServerActivity.class){
			this.serverActivity = (ServerActivity) activity;
		}
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		String modifiedDate = DateManager.fixDatePickerFormat(year, monthOfYear, dayOfMonth, "to");
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
			SensorStationDatePickerFragment newFragment = SensorStationDatePickerFragment.newInstance("to");
			newFragment.show(conSensActivity.getSupportFragmentManager(), "connSensDatePickerTo");
		}
		else if(this.serverActivity != null && v == serverActivity.getDateToButton()) {
			ServerDatePickerFragment newFragment = ServerDatePickerFragment.newInstance("to");
			newFragment.show(serverActivity.getSupportFragmentManager(), "serverDatePickerTo");
		}
	}
}
