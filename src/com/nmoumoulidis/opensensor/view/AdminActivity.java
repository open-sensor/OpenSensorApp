package com.nmoumoulidis.opensensor.view;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.controller.AdminLocationListener;
import com.nmoumoulidis.opensensor.controller.AdminUIController;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

/**
 * UI class for the Administrator Settings feature. Aside from setting up the UI and
 * performing the authentication against the admin/admin credentials, it provides the UI
 * and functionality for firing up an intent to open the standard Android location settings
 * activity for the user to enable if they are not enabled already. Also provides logic for
 * checking if a location has been acquired through the Wi-Fi network triangulation method
 * provided by the OS.
 * @author Nikos Moumoulidis
 */
public class AdminActivity extends Activity {

	private static final String CREDENTIALS = "admin:admin";
	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	// ---------- Login-related attributes -------------
	private ScrollView loginForm;
	private String mUsername;
	private String mPassword;
	private EditText mUsernameView;
	private EditText mPasswordView;
	private Button mSignInButton;
	
	// --------- Set location - related attributes -----------
	private LinearLayout setLocationUI;
	private TextView mSetLocationInfo;
	private Button mSignOutButton;
	private Button mSetLocationButton;
	private TextView mSetLocationFeedback;
	
	private AdminUIController viewController;
	private LocationManager locManager;
	private AlertDialog alert;
	private AdminLocationListener locListener;
	private ProgressDialog mSearchingLocationDialog;
	private boolean isLocationFound = false;
	private boolean isLocationSet = false;
	private String location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get location updates...
		locListener = new AdminLocationListener(this);
		locManager = (LocationManager) this.getSystemService(AdminActivity.LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0l, 0f, locListener);
		
		
		// Set up loading animation...
		mSearchingLocationDialog = new ProgressDialog(this);
        mSearchingLocationDialog.setMessage("Acquiring your current location...");
        mSearchingLocationDialog.setCancelable(false);
		
		setContentView(R.layout.activity_admin);
		setupActionBar();


		// Set up the login form.
		loginForm = (ScrollView) findViewById(R.id.login_form);
		mUsernameView = (EditText) findViewById(R.id.username);
		mPasswordView = (EditText) findViewById(R.id.password);
		mSignInButton = (Button) findViewById(R.id.sign_in_button);
		setLocationUI = (LinearLayout) findViewById(R.id.set_location_ui);
		
		// Set up the hidden UI.
		mSetLocationInfo = (TextView) findViewById(R.id.set_location_info);
		mSignOutButton = (Button) findViewById(R.id.sign_out_btn);
		mSetLocationButton = (Button) findViewById(R.id.set_location_btn);
		mSetLocationFeedback = (TextView) findViewById(R.id.set_location_feedback);
		
		viewController = new AdminUIController(this);
		mPasswordView.setOnEditorActionListener(viewController);
		mSignInButton.setOnClickListener(viewController);
		mSetLocationButton.setOnClickListener(viewController);
		mSignOutButton.setOnClickListener(viewController);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(this.alert != null) {
			this.alert.dismiss();
		}
		if(this.mSearchingLocationDialog != null) {
			this.mSearchingLocationDialog.dismiss();
		}
		locManager.removeUpdates(locListener);
	}
	
	/**
	 * Called by the button listener to perform the appropriate UI actions.
	 * (show loading animation, text feedback to the user, or take him to the
	 * Android location settings to enable wi-fi triangulation locating.
	 */
	public void findLocation() {
		if(!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			showAlertEnableWiFiNetworkLocationSetting();
		}
		else {
			if(isLocationSet == false) {
				if(isLocationFound == false) {
					if(!mSearchingLocationDialog.isShowing()) {
						mSearchingLocationDialog.show();
					}
				}
			}
			else {
				this.mSetLocationFeedback.setText("The location has already been set.");
			}
		}	
	}
	
	private void showAlertEnableWiFiNetworkLocationSetting() {
		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Your 'Use Wireless Networks' location setting seems to be disabled, do you want to enable it?");
		    builder.setCancelable(false);
		    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, final int id) {
		                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		               }
		           });
		    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, final int id) {
		                    dialog.cancel();
		                    finish();
		               }
		           });
		    alert = builder.create();
		    alert.show();
	}
	
	public void hideLoginScreen(boolean hide) {
		if(hide == true) {
			loginForm.setVisibility(View.GONE);
			setLocationUI.setVisibility(View.VISIBLE);
		}
		else {
			loginForm.setVisibility(View.VISIBLE);
			setLocationUI.setVisibility(View.GONE);
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_admin, menu);
		return true;
	}

	/**
	 * Attempts to sign in the account specified by the login form.
	 * If there are form errors (missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for non-empty username.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	public boolean isLocationSet() {
		return isLocationSet;
	}

	public void setLocationSet(boolean isLocationSet) {
		this.isLocationSet = isLocationSet;
	}
	
	public ProgressDialog getmSearchingLocationDialog() {
		return mSearchingLocationDialog;
	}

	public LocationManager getLocManager() {
		return locManager;
	}
	
	public void setLocation(String loc) {
		this.location = loc;
	}
	
	public String getLocation() {
		return location;
	}
	
	public boolean isLocationFound() {
		return isLocationFound;
	}

	public void setLocationFound(boolean isLocationFound) {
		this.isLocationFound = isLocationFound;
	}
	
	public EditText getmUsernameView() {
		return mUsernameView;
	}

	public EditText getmPasswordView() {
		return mPasswordView;
	}

	public Button getmSignInButton() {
		return mSignInButton;
	}
	
	public UserLoginTask getmAuthTask() {
		return mAuthTask;
	}

	public ScrollView getLoginForm() {
		return loginForm;
	}

	public String getmUsername() {
		return mUsername;
	}

	public String getmPassword() {
		return mPassword;
	}

	public LinearLayout getSetLocationUI() {
		return setLocationUI;
	}

	public TextView getmSetLocationInfo() {
		return mSetLocationInfo;
	}

	public Button getmSignOutButton() {
		return mSignOutButton;
	}

	public Button getmSetLocationButton() {
		return mSetLocationButton;
	}

	public TextView getmSetLocationFeedback() {
		return mSetLocationFeedback;
	}

	public AdminUIController getViewController() {
		return viewController;
	}

	/**
	 * Represents an asynchronous login/registration task
	 * used to authenticate the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			String[] pieces = CREDENTIALS.split(":");
			if (pieces[0].equals(mUsername)) {
				// Account exists, return true if the password matches.
				return pieces[1].equals(mPassword);
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			if (success) {
				hideLoginScreen(true);
			} else {
				mPasswordView.setError("Incorrect password and/or username.");
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
		}
	}
}
