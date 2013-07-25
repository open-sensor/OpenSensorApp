package com.nmoumoulidis.opensensor.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Button;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.MainUIController;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.NetworkDataService;
import com.nmoumoulidis.opensensor.restInterface.SensorListReqRunnable;
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultSensorListRequest;

public class MainActivity extends FragmentActivity {

	private Button mGoToConSensActivityBtn;
	private Button mGoToPhnSensActivityBtn;
	private Button mGoToMapViewActivityBtn;
	
	private MainUIController mMainUIController;
	private SensorTracker mSensorTracker;
	
	private boolean sensorListObtained = false;
	private boolean wifiSensorConnected = true;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGoToConSensActivityBtn = (Button) findViewById(R.id.go_to_con_sens_btn);
		mGoToPhnSensActivityBtn = (Button) findViewById(R.id.go_to_phn_sens_btn);
		mGoToMapViewActivityBtn = (Button) findViewById(R.id.go_to_map_view_btn);

		mMainUIController = new MainUIController(this);
		mGoToConSensActivityBtn.setOnClickListener(mMainUIController);
		mGoToPhnSensActivityBtn.setOnClickListener(mMainUIController);
		mGoToMapViewActivityBtn.setOnClickListener(mMainUIController);

		mSensorTracker = new SensorTracker();

		initializationRequests();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
    protected void onDestroy() {
		super.onDestroy();
	}

	private void initializationRequests() {
		// Perform initialization request for dynamic
		// identification of the available sensor list.
	   	DefaultSensorListRequest sensorListRequest = new DefaultSensorListRequest();
	   	SensorListReqRunnable sensorListRequestTask = 
				new SensorListReqRunnable(this, sensorListRequest);
	   	new Thread(sensorListRequestTask).start();

	   	if(wifiSensorConnected == true) {
	   	// Perform request within a new IntentService to receive all the
			// available persistently stored data on the sensor.
		   	Intent batchDataRequestIntent = new Intent(this, NetworkDataService.class);
		   	startService(batchDataRequestIntent);
	   	}
	}
	
	public boolean isWifiSensorConnected() {
		return wifiSensorConnected;
	}

	public void setWifiSensorConnected(boolean wifiSensorConnected) {
		this.wifiSensorConnected = wifiSensorConnected;
	}

	public Button getmGoToConSensActivityBtn() {
		return mGoToConSensActivityBtn;
	}

	public Button getmGoToPhnSensActivityBtn() {
		return mGoToPhnSensActivityBtn;
	}

	public Button getmGoToMapViewActivityBtn() {
		return mGoToMapViewActivityBtn;
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
