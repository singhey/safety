package com.singhey.womenux.smsSenders;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationProviderApp {

    private Context context;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    SendSms sendSms;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitiude) {
        this.latitude = latitiude;
    }

    public boolean isLocationUpdate() {
        return locationUpdate;
    }

    public void setLocationUpdate(boolean locationUpdate) {
        this.locationUpdate = locationUpdate;
    }

    private String longitude;
    private String latitude;
    boolean locationUpdate = false;

    public LocationProviderApp(Context context, SendSms s) {
        this.context = context;
        this.sendSms = s;
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

            //get device location
            createLocationRequest();
            requestLocationCallback();
    }


    private void requestLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for(Location location: locationResult.getLocations()){

                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                }
                sendSms.setLocation(latitude, longitude);
                removeLocationFetcher();
            }
        };

    }

    private void removeLocationFetcher() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("MainActivity", "Permissions not granted");
            return;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }

    private void createLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);

    }


    public void getLocation() {
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("MainActivity", "Permissions not granted");
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
}
