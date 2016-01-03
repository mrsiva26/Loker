package com.example.loker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AppLocationService extends Service implements LocationListener {
	Context cc;
	protected LocationManager locationManager;
	Location location;
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000*2;
	public AppLocationService(Context context) {
		cc=context;
		locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
	}
	@Override
	public void onCreate() {
		Toast.makeText(getBaseContext(), "Congrats! MyService Created", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(getBaseContext(), "My Service Started", Toast.LENGTH_LONG).show();
		//Note: You can start a new thread and use it for long background processing from here.
	}
	
	public Location getLocation(String provider) {
		if (locationManager.isProviderEnabled(provider)) {
			locationManager.requestLocationUpdates(provider,MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE,this);
			if (locationManager != null) {
				location = locationManager.getLastKnownLocation(provider);
				return location;
			}
		}
		return null;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onLocationChanged(Location nwLocation) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}