package com.appdevin.sgtraffic.Class;

/**
 * Created by JEYAVISHNU on 4/7/18.
 */

public class GetAccidentData {
    //Variables
    public String Address;
    public double LATITUDE;
    public double LONGTITUDE;
    public String BirthD;
    public String AccidentType;
    public String Licensenumber;
    public String Description;
    public String Key;

    public GetAccidentData(){

    }

    //Constructor
    public GetAccidentData(String Address, double LATITUDE, double LONGTITUDE, String BirthD,String AccidentType,String Licensenumber,String Description,String Key) {
        this.Address = Address;
        this.LATITUDE = LATITUDE;
        this.LONGTITUDE = LONGTITUDE;
        this.BirthD=BirthD;
        this.AccidentType=AccidentType;
        this.Licensenumber=Licensenumber;
        this.Description=Description;
        this.Key=Key;

    }



}
