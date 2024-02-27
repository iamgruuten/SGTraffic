package com.appdevin.sgtraffic.Class;


import android.location.Location;
import android.util.Log;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CSpeed extends Location {

    private boolean bUseMetricUnits = true;
    private double Clatitude; //Current location
    private double Clongitude;

    private double Llatitude; //Last Known location
    private double Llongitude;

    private static double nDistance; //Travelling distance

    private static long TCurrent = 0; //Timer Current
    private static long Tlastknow = 0; //Timer LastKnown
    private static long timer; //
    private static double Try = 0.0;

    public CSpeed(Location location) {
        this(location, true);
    }

    public CSpeed(Location location, boolean bUseMetricUnits) {
        // TODO Auto-generated constructor stub
        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
        Clatitude = location.getLatitude();
        Clongitude = location.getLongitude();
        TCurrent = location.getTime();
    }

    public Double callout()
    {
        return Try;
    }


    public boolean getUseMetricUnits() {
        return this.bUseMetricUnits;
    }

    public void setUseMetricunits(boolean bUseMetricUntis) {
        this.bUseMetricUnits = bUseMetricUntis;
    }

    @Override
    public float distanceTo(Location dest) {
        // TODO Auto-generated method stub
        float nDistance = super.distanceTo(dest);
        if (!this.getUseMetricUnits()) {
            //Convert meters to feet
            nDistance = nDistance * 3.28083989501312f;
        }
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        // TODO Auto-generated method stub
        float nAccuracy = super.getAccuracy();
        if (!this.getUseMetricUnits()) {
            //Convert meters to feet
            nAccuracy = nAccuracy * 3.28083989501312f;
        }
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        // TODO Auto-generated method stub
        double nAltitude = super.getAltitude();
        if (!this.getUseMetricUnits()) {
            //Convert meters to feet
            nAltitude = nAltitude * 3.28083989501312d;
        }
        return nAltitude;
    }

    public double getSeed() {
        // TODO Auto-generated method stub
        Llongitude = Clongitude;
        Llatitude = Clatitude;
        Tlastknow = TCurrent;
        Location LastKLocation = new Location("");
        LastKLocation.setLatitude(Llatitude);
        LastKLocation.setLongitude(Llongitude);

        Clongitude = super.getLongitude();
        Clatitude = super.getLatitude();
        TCurrent = super.getTime();
        Location crntLocation = new Location("");
        crntLocation.setLatitude(Clatitude);
        crntLocation.setLongitude(Clongitude);

        if(crntLocation != LastKLocation) {
            nDistance = crntLocation.distanceTo(LastKLocation);
            timer = (TCurrent - Tlastknow);

            DateFormat Cformat = new SimpleDateFormat("ss");
            Cformat.setTimeZone(TimeZone.getTimeZone("Singapore"));
            String Current = Cformat.format(timer);
            Log.d("Test1", Current);

            if (nDistance != 0) {
                Try = nDistance / Integer.parseInt(Current);
                return Try;
            } else {
                Try = 0.0;
                return (float) Try;
            }
        } else{
            Try = 0.0;
            return (float) Try;
        }


    }
}