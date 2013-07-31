package com.nmoumoulidis.opensensor.controller;

import com.nmoumoulidis.opensensor.view.ServerActivity;

import android.view.View;
import android.view.View.OnClickListener;

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
			System.out.println(mServerActivity.getQueryBuilder().getURLQueryPart());
		}
		else if(v == mServerActivity.getClearSearchFiltersButton()) {
			mServerActivity.getQueryBuilder().clearSearchFilters();
		}
	}
	
}
