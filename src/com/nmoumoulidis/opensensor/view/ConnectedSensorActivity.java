package com.nmoumoulidis.opensensor.view;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.ConSensUIController;
import com.nmoumoulidis.opensensor.model.DataContainer;
import com.nmoumoulidis.opensensor.model.SensorTracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.annotation.SuppressLint;
import android.os.Build;


public class ConnectedSensorActivity extends Activity 
{
	private TextView mResultText;
	private Button mTempButton;
	private Button mHumidButton;
	private Button mLightButton;
	
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

		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mResultText = (TextView) findViewById(R.id.result_text);
		mResultText.setMovementMethod(new ScrollingMovementMethod());
		mTempButton = (Button) findViewById(R.id.temp_button);
		mHumidButton = (Button) findViewById(R.id.humid_button);
		mLightButton = (Button) findViewById(R.id.light_button);
		
		mConSensUiController = new ConSensUIController(this);
		mTempButton.setOnClickListener(mConSensUiController);
		mHumidButton.setOnClickListener(mConSensUiController);
		mLightButton.setOnClickListener(mConSensUiController);
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
	
	public TextView getmResultText() {
		return mResultText;
	}


	public Button getmTempButton() {
		return mTempButton;
	}


	public Button getmHumidButton() {
		return mHumidButton;
	}


	public Button getmLightButton() {
		return mLightButton;
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
