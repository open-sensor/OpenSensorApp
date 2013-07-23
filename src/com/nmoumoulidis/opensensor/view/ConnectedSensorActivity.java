package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.ConSensUIController;
import com.nmoumoulidis.opensensor.model.DataContainer;
import com.nmoumoulidis.opensensor.model.DatabaseHelper;
import com.nmoumoulidis.opensensor.model.SensorTracker;


public class ConnectedSensorActivity extends Activity 
{
	private TextView mInfoText;
	private TextView mLabelText;
	private TextView mResultText;
	private LinearLayout layout;
	private Button[] buttonArray;
	private ArrayList<String> buttonNames;
	
	private Button mGoToHistoryBtn;
	private TextView mNoHistoryLabel;
	
	private ConSensUIController mConSensUiController;
	private SensorTracker mSensorTracker;
	private DataContainer mDataContainer;
	private DatabaseHelper dbHelper;

	private boolean sensorListObtained = false;
	private boolean wifiSensorConnected = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected_sensor);
		
		Bundle b = this.getIntent().getExtras();
		if(b!=null){
			mSensorTracker = b.getParcelable("sensor_tracker");
			wifiSensorConnected = b.getBoolean("is_sensor_connected");
		}	
		if(mSensorTracker != null)
			sensorListObtained = true;

		layout = (LinearLayout)findViewById(R.id.con_sens_layout);
		mInfoText = (TextView) findViewById(R.id.info_label);
		mLabelText = (TextView) findViewById(R.id.result_label);
		mNoHistoryLabel = (TextView) findViewById(R.id.no_data_history_label);
		mGoToHistoryBtn = (Button) findViewById(R.id.go_to_history_btn);
		mResultText = (TextView) findViewById(R.id.result_text);
		
		if(wifiSensorConnected == false) {
			mInfoText.setText("There is no WiFi-connected sensor station at the moment!");
			mLabelText.setVisibility(View.GONE);
			mResultText.setVisibility(View.GONE);
		}

		buttonNames = mSensorTracker.getConnectedSensorNames();

		mConSensUiController = new ConSensUIController(this);
		dynamicButtonCreation();
		mConSensUiController.setBtnArray(buttonArray);

		dbHelper = new DatabaseHelper(this);
		// If there are no history data, notify the user, else show button.
		if(dbHelper.getDataCount() == 0) {
			mNoHistoryLabel.setVisibility(View.VISIBLE);
		}
		else {
			mGoToHistoryBtn.setVisibility(View.VISIBLE);
			mGoToHistoryBtn.setOnClickListener(mConSensUiController);
		}

		mResultText.setMovementMethod(new ScrollingMovementMethod());

		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private void dynamicButtonCreation() {	
		buttonArray = new Button[buttonNames.size()];
		for(int i=0 ; i<buttonNames.size(); i++) {
			buttonArray[i] = new Button(this);
			buttonArray[i].setText(buttonNames.get(i));
			LinearLayout.LayoutParams btnParams = 
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			buttonArray[i].setLayoutParams(btnParams);
			btnParams.setMargins(0, convertDPtoPX(5), 0, convertDPtoPX(5));
			buttonArray[i].setOnClickListener(mConSensUiController);
			layout.addView(buttonArray[i], i+1);
		}
	}

	private int convertDPtoPX(int dp) {
		Resources r = getResources();
		return (int)Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		dbHelper = new DatabaseHelper(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		dbHelper.close();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		dbHelper.close();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}

	public Button getmGoToHistoryBtn() {
		return mGoToHistoryBtn;
	}
	
	public DatabaseHelper getDbHelper() {
		return dbHelper;
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
