package com.appdevin.sgtraffic.Class;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.appdevin.sgtraffic.Map;
import com.appdevin.sgtraffic.R;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * Created by JEYAVISHNU on 19/6/18.
 */

public class LocationInfo {
    //Parameter to get Current Location

    private LocationListener locationListener;


    private Double Lat=0.0;
    private Double Lng=0.0;


    public void setLat(Double lat) {
        Lat = lat;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }


    public Double Lat() {
        return Lat;
    }

    public Double Lng() {
        return Lng;
    }
}





