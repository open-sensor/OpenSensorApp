package com.nmoumoulidis.opensensor.view;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.MainUIController;
import com.nmoumoulidis.opensensor.model.DataContainer;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.restInterface.BatchDataRestReqRunnable;
import com.nmoumoulidis.opensensor.restInterface.SensorListReqRunnable;
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultBatchDataRequest;
import com.nmoumoulidis.opensensor.restInterface.requests.DefaultSensorListRequest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

	private Button mGoToConSensActivityBtn;
	private Button mGoToPhnSensActivityBtn;
	private Button mGoToMapViewActivityBtn;
	
	private MainUIController mMainUIController;
	private SensorTracker mSensorTracker;
	private DataContainer mDataContainer;
	
	private boolean sensorListObtained = false;

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
		
		// Create sensor and sensor data containers/managers.
		mSensorTracker = new SensorTracker();
		mDataContainer = new DataContainer();
		
		initializationRequests();
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	private void initializationRequests() {
		// Perform initialization request for dynamic
		// identification of the available sensor list.
	   	DefaultSensorListRequest sensorListRequest = new DefaultSensorListRequest("slug", "81");
	   	SensorListReqRunnable sensorListRequestTask = 
				new SensorListReqRunnable(this, sensorListRequest);
	   	new Thread(sensorListRequestTask).start();

		// Perform request to receive all the
		// available persistently stored data on the sensor.
		DefaultBatchDataRequest batchDataRequest = new DefaultBatchDataRequest("slug", "81");
		BatchDataRestReqRunnable batchReqRunnable = 
				new BatchDataRestReqRunnable(this, batchDataRequest);
		new Thread(batchReqRunnable).start();
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

	public DataContainer getmDataContainer() {
		return mDataContainer;
	}
    
}
