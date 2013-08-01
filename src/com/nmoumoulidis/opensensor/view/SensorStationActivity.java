package com.nmoumoulidis.opensensor.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.SensorStationUIController;
import com.nmoumoulidis.opensensor.controller.GeneralDatePickerListenerFrom;
import com.nmoumoulidis.opensensor.controller.GeneralDatePickerListenerTo;
import com.nmoumoulidis.opensensor.controller.GeneralSpinnerListener;
import com.nmoumoulidis.opensensor.model.DatabaseHelper;
import com.nmoumoulidis.opensensor.model.SensorTracker;
import com.nmoumoulidis.opensensor.model.processing.SensorStationSearchQueryBuilder;

public class SensorStationActivity extends FragmentActivity
{
	// Real-time UI elements...
	private TextView mInfoText;
	private TextView mLabelText;
	private TextView mResultText;
	private LinearLayout layout;
	private Button[] buttonArray;
	private ArrayList<String> buttonNames;
	
	// Search data options UI elements...
	private Button backToRealTimeButton;
	private TextView spinnerLabel;
	private Spinner dataSpinner;
	private TextView dateRangeLabel;
	private Button pickDateFromButton;
	private Button pickDateToButton;
	private Button searchButton;
	private TextView noResultsTextView;
	private ListView historyDataListView;
	private SensorStationDataListViewAdapter listAdapter;
	private ArrayList<Cursor> cursorsUsed;

	private GeneralSpinnerListener spinnerListener;
	private GeneralSpinnerAdapter spinnerAdapter;
	
	private Button mGoToHistoryBtn;
	private TextView mNoHistoryLabel;
	
	private SensorStationUIController mConSensUiController;
	private static GeneralDatePickerListenerFrom dateFromListener;
	private static GeneralDatePickerListenerTo dateToListener;
	private SensorTracker mSensorTracker;
	private DatabaseHelper dbHelper;
	private SensorStationSearchQueryBuilder queryBuilder;

	private boolean sensorListObtained = false;
	private boolean wifiSensorConnected = false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_station);
		
		cursorsUsed = new ArrayList<Cursor>();
		
		// ----------------- Get Extras From Intent -----------------
		Bundle b = this.getIntent().getExtras();
		if(b!=null){
			mSensorTracker = b.getParcelable("sensor_tracker");
			wifiSensorConnected = b.getBoolean("is_sensor_connected");
		}	
		if(mSensorTracker != null)
			sensorListObtained = true;

		// The main (parent) layout.
		layout = (LinearLayout)findViewById(R.id.con_sens_layout);
		
		// ------------- Set Up Real-Time UI elements -------------
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

		mConSensUiController = new SensorStationUIController(this);
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
		
		// ------------- Set Up Search Options UI elements -------------
		dataSpinner = (Spinner) findViewById(R.id.sensor_spinner);
		dateRangeLabel = (TextView) findViewById(R.id.date_range_label);
		pickDateFromButton = (Button) findViewById(R.id.date_pick_from_btn);
		pickDateToButton = (Button) findViewById(R.id.date_pick_to_btn);
		searchButton = (Button) findViewById(R.id.show_search_results_btn);
		noResultsTextView = (TextView) findViewById(R.id.no_results_textview);
		historyDataListView = (ListView) findViewById(R.id.list);
		backToRealTimeButton = (Button) findViewById(R.id.go_to_realtime_data_btn);
		spinnerLabel = (TextView) findViewById(R.id.sensor_spinner_labeltext);

		spinnerListener = new GeneralSpinnerListener();
		spinnerAdapter = new GeneralSpinnerAdapter(this);
		spinnerAdapter.populateSpinner();
		dataSpinner.setOnItemSelectedListener(spinnerListener);
		
		searchButton.setOnClickListener(mConSensUiController);
		backToRealTimeButton.setOnClickListener(mConSensUiController);
		
		dateFromListener = new GeneralDatePickerListenerFrom(this);
		dateToListener = new GeneralDatePickerListenerTo(this);
		pickDateFromButton.setOnClickListener(dateFromListener);
		pickDateToButton.setOnClickListener(dateToListener);

		queryBuilder = new SensorStationSearchQueryBuilder(this);
		listAdapter = new SensorStationDataListViewAdapter(this);
		
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
					new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			buttonArray[i].setLayoutParams(btnParams);
			btnParams.setMargins(0, convertDPtoPX(5), 0, convertDPtoPX(5));
			buttonArray[i].setOnClickListener(mConSensUiController);
			layout.addView(buttonArray[i], i+2);
		}
	}
	
	public void setRealTimeUIVisible(boolean visibility) {
		if(visibility == false) {
			mInfoText.setVisibility(View.GONE);
			mLabelText.setVisibility(View.GONE);
			mResultText.setVisibility(View.GONE);
			mGoToHistoryBtn.setVisibility(View.GONE);
			for(int i=0 ; i<buttonArray.length ; i++) {
				buttonArray[i].setVisibility(View.GONE);
			}
			backToRealTimeButton.setVisibility(View.VISIBLE);
			spinnerLabel.setVisibility(View.VISIBLE);
			dataSpinner.setVisibility(View.VISIBLE);
			dateRangeLabel.setVisibility(View.VISIBLE);
			pickDateFromButton.setVisibility(View.VISIBLE);
			pickDateToButton.setVisibility(View.VISIBLE);
			searchButton.setVisibility(View.VISIBLE);
			historyDataListView.setVisibility(View.VISIBLE);
		}
		else {
			mInfoText.setVisibility(View.VISIBLE);
			mLabelText.setVisibility(View.VISIBLE);
			mResultText.setVisibility(View.VISIBLE);
			mGoToHistoryBtn.setVisibility(View.VISIBLE);
			for(int i=0 ; i<buttonArray.length ; i++) {
				buttonArray[i].setVisibility(View.VISIBLE);
			}
			backToRealTimeButton.setVisibility(View.GONE);
			spinnerLabel.setVisibility(View.GONE);
			dataSpinner.setVisibility(View.GONE);
			dateRangeLabel.setVisibility(View.GONE);
			pickDateFromButton.setVisibility(View.GONE);
			pickDateToButton.setVisibility(View.GONE);
			searchButton.setVisibility(View.GONE);
			noResultsTextView.setVisibility(View.GONE);
			historyDataListView.setVisibility(View.GONE);
		}
	}

	private int convertDPtoPX(int dp) {
		Resources r = getResources();
		return (int)Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_sensor_station, menu);
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
	protected void onPause() {
		for(int i=0 ; i<this.cursorsUsed.size() ; i++) {
			cursorsUsed.get(i).close();
		}
		dbHelper.close();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		for(int i=0 ; i<this.cursorsUsed.size() ; i++) {
			cursorsUsed.get(i).close();
		}
		dbHelper.close();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		for(int i=0 ; i<this.cursorsUsed.size() ; i++) {
			cursorsUsed.get(i).close();
		}
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

	public boolean isSensorListObtained() {
		return sensorListObtained;
	}


	public void setSensorListObtained(boolean sensorListObtained) {
		this.sensorListObtained = sensorListObtained;
	}

	public SensorTracker getmSensorTracker() {
		return mSensorTracker;
	}
	
	public Button getBackToRealTimeButton() {
		return backToRealTimeButton;
	}

	public Spinner getDataSpinner() {
		return dataSpinner;
	}

	public Button getPickDateFromButton() {
		return pickDateFromButton;
	}

	public Button getPickDateToButton() {
		return pickDateToButton;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public ListView getHistoryDataListView() {
		return historyDataListView;
	}

	public SensorStationSearchQueryBuilder getQueryBuilder() {
		return queryBuilder;
	}
	
	public GeneralSpinnerAdapter getSpinnerAdapter() {
		return spinnerAdapter;
	}
	
	public static GeneralDatePickerListenerFrom getDateFromListener() {
		return dateFromListener;
	}

	public static GeneralDatePickerListenerTo getDateToListener() {
		return dateToListener;
	}

	public SensorStationDataListViewAdapter getListAdapter() {
		return listAdapter;
	}

	public void addUsedCursor(Cursor usedCursor) {
		this.cursorsUsed.add(usedCursor);
	}

	public TextView getNoResultsTextView() {
		return noResultsTextView;
	}
}
