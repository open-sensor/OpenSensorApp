package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.ConSensUIController;
import com.nmoumoulidis.opensensor.model.DataContainer;
import com.nmoumoulidis.opensensor.model.SensorTracker;


public class ConnectedSensorActivity extends Activity 
{
	private TextView mLabelText;
	private TextView mResultText;
	private LinearLayout layout;
	private Button[] buttonArray;
	private ArrayList<String> buttonNames;
	
	private ConSensUIController mConSensUiController;
	private SensorTracker mSensorTracker;
	private DataContainer mDataContainer;
	
	private boolean sensorListObtained = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected_sensor);
		
		Bundle b = this.getIntent().getExtras();
		if(b!=null)
			mSensorTracker = b.getParcelable("sensor_tracker");
		if(mSensorTracker != null)
			sensorListObtained = true;

		
		layout = (LinearLayout)findViewById(R.id.con_sens_layout);
		buttonNames = mSensorTracker.getConnectedSensorNames();
		
		mConSensUiController = new ConSensUIController(this);
		dynamicButtonCreation();
		mConSensUiController.setBtnArray(buttonArray);
		mLabelText = (TextView) findViewById(R.id.result_label);
		mResultText = (TextView) findViewById(R.id.result_text);
		mResultText.setMovementMethod(new ScrollingMovementMethod());
		
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		System.out.println("Creating new Instance of Con Sens Activity");
	}
	
	private void dynamicButtonCreation() {	
		buttonArray = new Button[buttonNames.size()];
		for(int i=0 ; i<buttonNames.size(); i++) {
			buttonArray[i] = new Button(this);
			buttonArray[i].setText(buttonNames.get(i));
			LinearLayout.LayoutParams btnParams = 
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			buttonArray[i].setLayoutParams(btnParams);
			btnParams.setMargins(0, 5, 0, 5);
			buttonArray[i].setOnClickListener(mConSensUiController);
			layout.addView(buttonArray[i], i+1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connected_sensor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public TextView getmLabelText() {
		return mLabelText;
	}

	public Button[] getButtonArray() {
		return buttonArray;
	}

	public TextView getmResultText() {
		return mResultText;
	}

	public DataContainer getmDataContainer() {
		return mDataContainer;
	}
	
	public boolean isSensorListObtained() {
		return sensorListObtained;
	}


	public void setSensorListObtained(boolean sensorListObtained) {
		this.sensorListObtained = sensorListObtained;
	}

	public SensorTracker getmSensorTracker() {
		return mSensorTracker;
	}
}
