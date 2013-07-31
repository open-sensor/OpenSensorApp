package com.nmoumoulidis.opensensor.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.DatePickerListenerFrom;
import com.nmoumoulidis.opensensor.controller.DatePickerListenerTo;
import com.nmoumoulidis.opensensor.controller.MySpinnerListener;
import com.nmoumoulidis.opensensor.controller.ServerUIController;
import com.nmoumoulidis.opensensor.model.processing.ServerSearchQueryBuilder;

public class ServerActivity extends FragmentActivity 
{
	private EditText locationEditText;
	private Spinner dataSpinner;
	private Button dateFromButton;
	private Button dateToButton;
	private Button searchButton;
	private Button clearSearchFiltersButton;
	private TextView noResultsTextView;
	private ListView resultsListView;
	
	private MySpinnerListener spinnerListener;
	private MySpinnerAdapter spinnerAdapter;
	private static DatePickerListenerFrom dateFromListener;
	private static DatePickerListenerTo dateToListener;
	private ServerUIController serverUiController;
	private ServerSearchQueryBuilder queryBuilder;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		
		locationEditText = (EditText) findViewById(R.id.server_edittext_location);
		dataSpinner = (Spinner) findViewById(R.id.server_spinner);
		dateFromButton = (Button) findViewById(R.id.server_date_pick_from_btn);
		dateToButton = (Button) findViewById(R.id.server_date_pick_to_btn);
		searchButton = (Button) findViewById(R.id.server_show_search_results_btn);
		clearSearchFiltersButton = (Button) findViewById(R.id.server_clear_search_filters);
		noResultsTextView = (TextView) findViewById(R.id.server_no_results_textview);
		resultsListView = (ListView) findViewById(R.id.server_list);
		
		dateFromListener = new DatePickerListenerFrom(this);
		dateToListener = new DatePickerListenerTo(this);
		dateFromButton.setOnClickListener(dateFromListener);
		dateToButton.setOnClickListener(dateToListener);
		
		spinnerListener = new MySpinnerListener();
		spinnerAdapter = new MySpinnerAdapter(this);
		spinnerAdapter.populateSpinner();
		dataSpinner.setOnItemSelectedListener(spinnerListener);
		serverUiController = new ServerUIController(this);
		searchButton.setOnClickListener(serverUiController);
		clearSearchFiltersButton.setOnClickListener(serverUiController);
		queryBuilder = new ServerSearchQueryBuilder(this);
		
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_view, menu);
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

	public Spinner getDataSpinner() {
		return dataSpinner;
	}
	
	public EditText getLocationEditText() {
		return locationEditText;
	}
	
	public static DatePickerListenerFrom getDateFromListener() {
		return dateFromListener;
	}

	public static DatePickerListenerTo getDateToListener() {
		return dateToListener;
	}

	public Button getDateFromButton() {
		return dateFromButton;
	}

	public Button getDateToButton() {
		return dateToButton;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public Button getClearSearchFiltersButton() {
		return clearSearchFiltersButton;
	}

	public TextView getNoResultsTextView() {
		return noResultsTextView;
	}

	public ListView getResultsListView() {
		return resultsListView;
	}

	public MySpinnerListener getSpinnerListener() {
		return spinnerListener;
	}

	public MySpinnerAdapter getSpinnerAdapter() {
		return spinnerAdapter;
	}

	public ServerSearchQueryBuilder getQueryBuilder() {
		return queryBuilder;
	}
}
