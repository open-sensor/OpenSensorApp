package com.nmoumoulidis.opensensor.View;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.Controller.MainUIController;
import com.nmoumoulidis.opensensor.Controller.RestRequestTask;
import com.nmoumoulidis.opensensor.Model.DataContainer;
import com.nmoumoulidis.opensensor.Model.SensorTracker;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mResultText;
	private TextView mResultLabel;
	private ProgressBar mProgressBar;
	private Button mTempButton;
	private Button mHumidButton;
	private Button mLightButton;
	
	private MainUIController mMainUIController;
	private SensorTracker mSensorTracker;
	private DataContainer mDataContainer;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mResultText = (TextView) findViewById(R.id.result_text);
        mResultText.setMovementMethod(new ScrollingMovementMethod());
        mResultLabel = (TextView) findViewById(R.id.result_label);
        mTempButton = (Button) findViewById(R.id.temp_button);
        mHumidButton = (Button) findViewById(R.id.humid_button);
        mLightButton = (Button) findViewById(R.id.light_button);
        
        mProgressBar = (ProgressBar) findViewById(R.id.prog_bar);
        
        mMainUIController = new MainUIController(this);
        mTempButton.setOnClickListener(mMainUIController);
        mHumidButton.setOnClickListener(mMainUIController);
        mLightButton.setOnClickListener(mMainUIController);
        
        mSensorTracker = new SensorTracker();
        mDataContainer = new DataContainer();
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	
        RestRequestTask sensorListRequest = new RestRequestTask(this, "slug:81");
        sensorListRequest.execute("GET", "/sensorlist", "application/json");
        
        // Wait 1 second for the first AsyncTask to complete.
        try {
			sensorListRequest.get(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
        
        // And then start the next one.
        RestRequestTask allDataRequest = new RestRequestTask(this, "slug:81");
        allDataRequest.execute("GET", "/data", "application/json");
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

	public TextView getmResultText() {
		return mResultText;
	}
	
	public TextView getmResultLabel() {
		return mResultLabel;
	}
	
    public ProgressBar getmProgressBar() {
		return mProgressBar;
	}
   
    public SensorTracker getmSensorTracker() {
		return mSensorTracker;
	}

	public DataContainer getmDataContainer() {
		return mDataContainer;
	}
    
}
