package com.nmoumoulidis.opensensor.view;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConnSensDatePickerFragment extends DialogFragment 
{
	private String FromOrTo;
	
	public ConnSensDatePickerFragment() {
		
	}

	public static final ConnSensDatePickerFragment newInstance(String fromOrTo) {
		ConnSensDatePickerFragment fragment = new ConnSensDatePickerFragment();
		Bundle bundle = new Bundle(1);
		bundle.putString("FROM_OR_TO", fromOrTo);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		this.FromOrTo = getArguments().getString("FROM_OR_TO");
		
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		if(FromOrTo.equals("from")) {
			return new DatePickerDialog(getActivity(), ConnectedSensorActivity.getDateFromListener(), year, month, day);
		}
		else if(FromOrTo.equals("to")) {
			return new DatePickerDialog(getActivity(), ConnectedSensorActivity.getDateToListener(), year, month, day);
		}
		return null;
	}
}