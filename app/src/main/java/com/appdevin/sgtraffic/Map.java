package com.appdevin.sgtraffic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.appdevin.sgtraffic.Class.LocationInfo;
import com.appdevin.sgtraffic.Class.OneMap;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


public class Map extends AppCompatActivity {
    //To intialize the Map
    private MapView mapView;

    //Parameter to get Current Location
    private LocationManager locationManger;
    private LocationListener locationListener;

    //Calling the Class
    LocationInfo Locate;

    CameraPosition position;
    MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoibWFwc3Byb2plY3QiLCJhIjoiY2ppankwb2N5MWpzdzN4bzg5YTY0bGYxeiJ9.fElQmSms5g3foO-SOcsc_Q");
        setContentView(R.layout.activity_map);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Setting map parameters
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl("https://maps-json.onemap.sg/Default.json");


        GetMap(1.358479,103.815201);
        //Get Current Location
        GetCurrentLocation();

MapboxMap.OnCameraMoveListener ZoomLimit=new MapboxMap.OnCameraMoveListener() {
    @Override
    public void onCameraMove() {

        float maxZoom = 10.0f;
        if (position.zoom> maxZoom)
         map.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
    }
};


        //Intializing
        Locate = new LocationInfo();

        //Calling a class to get token
        OneMap Token = new OneMap();
      //  Token.Auth(this,this);


    }



    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    //--------------------------------Method-----------------------------------//

    //To set map on
    public void GetMap(final Double Lat,final Double Lng) {


        mapView = (MapView) findViewById(R.id.mapView);

        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                //Setting the Location
                LatLng zoomLocation = new LatLng(Lat,Lng);


                // calling the Camera
                position = new CameraPosition.Builder()
                        .target(zoomLocation)
                        .zoom(12) // Zoom Setting
                        .build(); // Creates a CameraPosition from the builder
                //Creating animation on how the camera Zoom in
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 2000);
                //endregion

                //Remove mapbox attrition and logo
                mapboxMap.getUiSettings().setAttributionEnabled(false);
                mapboxMap.getUiSettings().setLogoEnabled(false);

                //Setting marker
//                MarkerOptions Indicate=new MarkerOptions().
//                        title("Current Location")
//                        .position(new LatLng(Lat,Lng));
//
//                mapboxMap.addMarker(Indicate);

            }
        });
    }

    //To get current Location
    public void GetCurrentLocation() {
        locationManger = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                Log.d("Location","Lat="+Locate.Lat()+"\nLon="+Locate.Lng());
                //Calling a method to set map
                GetMap(Locate.Lat(), Locate.Lng());
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
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        return;
        }
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //This is to request to enable location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }




}
