package com.nmoumoulidis.opensensor.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.GeneralDatePickerListenerFrom;
import com.nmoumoulidis.opensensor.controller.GeneralDatePickerListenerTo;
import com.nmoumoulidis.opensensor.controller.GeneralSpinnerListener;
import com.nmoumoulidis.opensensor.controller.ServerUIController;
import com.nmoumoulidis.opensensor.model.processing.ServerSearchQueryBuilder;

public class ServerActivity extends FragmentActivity 
{
	private TextView mainInfoTextView;
	private TextView infoLocationLabel;
	private TextView infoSpinnerLabel;
	private TextView infoDateRangeLabel;
	private Button showSearchOptionsButton;
	private EditText locationEditText;
	private Spinner dataSpinner;
	private Button dateFromButton;
	private Button dateToButton;
	private Button searchButton;
	private Button clearSearchFiltersButton;
	private TextView noResultsTextView;
	private ListView resultsListView;
	private TextView serverErrorInfo;
	
	private GeneralSpinnerListener spinnerListener;
	private GeneralSpinnerAdapter spinnerAdapter;
	private static GeneralDatePickerListenerFrom dateFromListener;
	private static GeneralDatePickerListenerTo dateToListener;
	private ServerUIController serverUiController;
	private ServerSearchQueryBuilder queryBuilder;
	private ProgressBar progressBar;
	private ServerDataListViewAdapter listViewAdapter;

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
		progressBar = (ProgressBar) findViewById(R.id.prog_bar);
		showSearchOptionsButton = (Button) findViewById(R.id.search_again_btn);
		mainInfoTextView = (TextView) findViewById(R.id.server_browse_data_label);
		infoLocationLabel = (TextView) findViewById(R.id.server_location_labeltext);
		infoSpinnerLabel = (TextView) findViewById(R.id.server_spinner_labeltext);
		infoDateRangeLabel = (TextView) findViewById(R.id.server_date_range_label);
		serverErrorInfo = (TextView) findViewById(R.id.server_error_info);
		
		dateFromListener = new GeneralDatePickerListenerFrom(this);
		dateToListener = new GeneralDatePickerListenerTo(this);
		dateFromButton.setOnClickListener(dateFromListener);
		dateToButton.setOnClickListener(dateToListener);
		
		spinnerListener = new GeneralSpinnerListener();
		spinnerAdapter = new GeneralSpinnerAdapter(this);
		spinnerAdapter.populateSpinner();
		dataSpinner.setOnItemSelectedListener(spinnerListener);
		serverUiController = new ServerUIController(this);
		searchButton.setOnClickListener(serverUiController);
		clearSearchFiltersButton.setOnClickListener(serverUiController);
		showSearchOptionsButton.setOnClickListener(serverUiController);
		
		queryBuilder = new ServerSearchQueryBuilder(this);
		listViewAdapter = new ServerDataListViewAdapter(this);
		
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_server, menu);
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
	
	public void showSearchOptionsAgain(boolean show) {
		if(show) {
			locationEditText.setVisibility(View.VISIBLE);
			dataSpinner.setVisibility(View.VISIBLE);
			dateFromButton.setVisibility(View.VISIBLE);
			dateToButton.setVisibility(View.VISIBLE);
			searchButton.setVisibility(View.VISIBLE);
			clearSearchFiltersButton.setVisibility(View.VISIBLE);
			mainInfoTextView.setVisibility(View.VISIBLE);
			infoLocationLabel.setVisibility(View.VISIBLE);
			infoSpinnerLabel.setVisibility(View.VISIBLE);
			infoDateRangeLabel.setVisibility(View.VISIBLE);
			
			showSearchOptionsButton.setVisibility(View.GONE);
		}
		else {
			locationEditText.setVisibility(View.GONE);
			dataSpinner.setVisibility(View.GONE);
			dateFromButton.setVisibility(View.GONE);
			dateToButton.setVisibility(View.GONE);
			searchButton.setVisibility(View.GONE);
			clearSearchFiltersButton.setVisibility(View.GONE);
			mainInfoTextView.setVisibility(View.GONE);
			infoLocationLabel.setVisibility(View.GONE);
			infoSpinnerLabel.setVisibility(View.GONE);
			infoDateRangeLabel.setVisibility(View.GONE);
			
			showSearchOptionsButton.setVisibility(View.VISIBLE);
		}
	}

	public Spinner getDataSpinner() {
		return dataSpinner;
	}
	
	public EditText getLocationEditText() {
		return locationEditText;
	}
	
	public static GeneralDatePickerListenerFrom getDateFromListener() {
		return dateFromListener;
	}

	public static GeneralDatePickerListenerTo getDateToListener() {
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

	public TextView getServerErrorInfo() {
		return serverErrorInfo;
	}

	public ServerDataListViewAdapter getListViewAdapter() {
		return listViewAdapter;
	}

	public Button getClearSearchFiltersButton() {
		return clearSearchFiltersButton;
	}

	public Button getShowSearchOptionsButton() {
		return showSearchOptionsButton;
	}
	
	public TextView getNoResultsTextView() {
		return noResultsTextView;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public ListView getResultsListView() {
		return resultsListView;
	}

	public GeneralSpinnerListener getSpinnerListener() {
		return spinnerListener;
	}

	public GeneralSpinnerAdapter getSpinnerAdapter() {
		return spinnerAdapter;
	}

	public ServerSearchQueryBuilder getQueryBuilder() {
		return queryBuilder;
	}
}
