package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.restInterface.ServerRestRequestTask;
import com.nmoumoulidis.opensensor.restInterface.requests.ServerGetRestRequest;
import com.nmoumoulidis.opensensor.view.ServerActivity;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Button listener for the Server activity {@link ServerActivity}.
 * @author Nikos Moumoulidis
 *
 */
public class ServerUIController implements OnClickListener 
{
	private ServerActivity mServerActivity;
	
	public ServerUIController(ServerActivity serverActivity) {
		super();
		this.mServerActivity = serverActivity;
	}
	@Override
	public void onClick(View v) {
		if(v == mServerActivity.getSearchButton()) {
			mServerActivity.getServerErrorInfo().setVisibility(View.GONE);
			mServerActivity.getListViewAdapter().detachAdapterFromListView();
			// Create a GET request for the server using the
			// querybuilder's relative url.
			ServerGetRestRequest getRequest = 
					new ServerGetRestRequest(mServerActivity.getQueryBuilder().getURLQueryPart());
			// Perform a REST request to the server...
			new ServerRestRequestTask(mServerActivity).execute(getRequest);
		}
		else if(v == mServerActivity.getClearSearchFiltersButton()) {
			mServerActivity.getQueryBuilder().clearSearchFilters();
		}
		else if(v == mServerActivity.getShowSearchOptionsButton()) {
			mServerActivity.showSearchOptionsAgain(true);
		}
	}
}
