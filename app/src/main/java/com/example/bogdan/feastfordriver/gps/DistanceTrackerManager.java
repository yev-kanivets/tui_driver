package com.example.bogdan.feastfordriver.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.bogdan.feastfordriver.util.Prefs;

public class DistanceTrackerManager implements LocationListener {

    private static final String TAG = "DistanceTrackerManager";

    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;

    private LocationManager locationManager;
    private boolean tracking;
    private double latitude;
    private double longitude;

    public DistanceTrackerManager(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        latitude = Prefs.get().readLatitude();
        longitude = Prefs.get().readLongitude();
    }

    public void startTracking() {
        tracking = true;

        @SuppressWarnings("MissingPermission")
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            onLocationChanged(location);
        }

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    public void stopTracking() {
        tracking = false;
        latitude = 0;
        longitude = 0;
        locationManager.removeUpdates(this);

        Prefs.get().writeLatitude(0);
        Prefs.get().writeLongitude(0);
    }

    // Location listener implementation

    @Nullable
    public DistanceTrackerState getDistanceTrackerState() {
        return new DistanceTrackerState(tracking, latitude, longitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Prefs.get().writeLatitude(latitude);
        Prefs.get().writeLongitude(longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
