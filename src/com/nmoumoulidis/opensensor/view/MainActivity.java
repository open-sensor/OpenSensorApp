package com.nmoumoulidis.opensensor.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Button;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.MainUIController;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.BatchDataRetrieveService;
import com.nmoumoulidis.opensensor.restInterface.SensorListRestRequestRunnable;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationSensorListRequest;

public class MainActivity extends FragmentActivity {

	private Button mGoToConSensActivityBtn;
	private Button mGoToPhnSensActivityBtn;
	private Button mGoToMapViewActivityBtn;
	private Button mGoToAdminActivityBtn;

	private MainUIController mMainUIController;
	private SensorTracker mSensorTracker;
	
	private boolean sensorListObtained = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGoToConSensActivityBtn = (Button) findViewById(R.id.go_to_con_sens_btn);
		mGoToPhnSensActivityBtn = (Button) findViewById(R.id.go_to_phn_sens_btn);
		mGoToMapViewActivityBtn = (Button) findViewById(R.id.go_to_map_view_btn);
		mGoToAdminActivityBtn = (Button) findViewById(R.id.go_to_admin_btn);

		mMainUIController = new MainUIController(this);
		mGoToConSensActivityBtn.setOnClickListener(mMainUIController);
		mGoToPhnSensActivityBtn.setOnClickListener(mMainUIController);
		mGoToMapViewActivityBtn.setOnClickListener(mMainUIController);
		mGoToAdminActivityBtn.setOnClickListener(mMainUIController);

		mSensorTracker = new SensorTracker();

		retrieveBatchData();	
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

	@Override
	protected void onStart() {
		super.onStart();
		if(sensorListObtained == false) {
			System.out.println("Trying to retrieve sensor list...");
			retrieveSensorList();
		}
	}

	@Override
    protected void onDestroy() {
		super.onDestroy();
	}
	
	private void retrieveSensorList() {
		// Perform initialization request for dynamic
		// identification of the available sensor list.
	   	SensorStationSensorListRequest sensorListRequest = new SensorStationSensorListRequest();
	   	SensorListRestRequestRunnable sensorListRequestTask = 
				new SensorListRestRequestRunnable(this, sensorListRequest);
	   	new Thread(sensorListRequestTask).start();
	}

	private void retrieveBatchData() {
   		// Perform request within a new IntentService to receive all the
		// available persistently stored data on the sensor
   		// anmd attempt to send them to the RESTful server...
	   	Intent batchDataRequestIntent = new Intent(this, BatchDataRetrieveService.class);
	   	startService(batchDataRequestIntent);
	}
	
	public boolean isSensorListObtained() {
		return sensorListObtained;
	}

	public void setSensorListObtained(boolean wifiSensorConnected) {
		this.sensorListObtained = wifiSensorConnected;
		if(sensorListObtained == false) {
			System.out.println("SENSORLIST request failed...");
		}
	}
	
	public Button getmGoToConSensActivityBtn() {
		return mGoToConSensActivityBtn;
	}

	public Button getmGoToPhnSensActivityBtn() {
		return mGoToPhnSensActivityBtn;
	}
	
	public Button getmGoToAdminActivityBtn() {
		return mGoToAdminActivityBtn;
	}

	public Button getmGoToMapViewActivityBtn() {
		return mGoToMapViewActivityBtn;
	}

    public SensorTracker getmSensorTracker() {
		return mSensorTracker;
	}
}
