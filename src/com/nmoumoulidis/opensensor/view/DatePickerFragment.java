package com.nmoumoulidis.opensensor.view;

import java.util.Calendar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment 
{
	private ConnectedSensorActivity conSensActivity;
	private String FromOrTo;
	
	public DatePickerFragment(ConnectedSensorActivity conSensActivity, String fromorto) {
		this.conSensActivity = conSensActivity;
		this.FromOrTo = fromorto;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		if(FromOrTo.equals("from")) {
			return new DatePickerDialog(getActivity(), conSensActivity.getDateFromListener(), year, month, day);
		}
		else if(FromOrTo.equals("to")) {
			return new DatePickerDialog(getActivity(), conSensActivity.getDateToListener(), year, month, day);
		}
		return null;
	}
}