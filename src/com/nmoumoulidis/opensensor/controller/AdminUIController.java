package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.R;
import com.nmoumoulidis.opensensor.restInterface.SensorStationRestRequestTask;
import com.nmoumoulidis.opensensor.restInterface.requests.SensorStationSetLocationRequest;
import com.nmoumoulidis.opensensor.view.AdminActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Button as well as textfield (EditText) listener for the 
 * Admin activity {@link AdminActivity}.
 * @author Nikos Moumoulidis
 *
 */
public class AdminUIController implements OnClickListener, OnEditorActionListener
{
	private AdminActivity mAdminActivity;
	
	public AdminUIController(AdminActivity adminActivity) {
		this.mAdminActivity = adminActivity;
	}
	
	@Override
	public void onClick(View v) {
		if(v == mAdminActivity.getmSignInButton()) {
			mAdminActivity.attemptLogin();
		}
		else if(v == mAdminActivity.getmSetLocationButton()) {
			mAdminActivity.findLocation();
			if(mAdminActivity.isLocationFound() == true
					&& mAdminActivity.isLocationSet() == false) {				
				// Fire up the REST request
				SensorStationSetLocationRequest request = new 
						SensorStationSetLocationRequest(mAdminActivity.getLocation());
				new SensorStationRestRequestTask(mAdminActivity).execute(request);
			}
		}
		else if(v == mAdminActivity.getmSignOutButton()) {
			mAdminActivity.hideLoginScreen(false);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int id, KeyEvent event) {
		if (id == R.id.login || id == EditorInfo.IME_NULL) {
			mAdminActivity.attemptLogin();
			return true;
		}
		return false;
	}

}
