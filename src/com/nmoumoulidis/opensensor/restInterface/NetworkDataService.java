package com.nmoumoulidis.opensensor.restInterface;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NetworkDataService extends Service 
{
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent i) {
		return null;
	}

	@Override
	public void onDestroy() {
		System.out.println("Service DESTROYED...");
	}
}
