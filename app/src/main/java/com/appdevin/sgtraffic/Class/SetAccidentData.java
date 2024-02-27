package com.appdevin.sgtraffic.Class;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetAccidentData {

    //Variables
    public String Address;
    public double LATITUDE;
    public double LONGTITUDE;
    public String BirthD;
    public String AccidentType;
    public String Licensenumber;
    public String Description;
    public String Key;

    public SetAccidentData(){

    }

    //Constructor
    public SetAccidentData(String Address, double LATITUDE, double LONGTITUDE, String BirthD,String AccidentType,String Licensenumber,String Description,String Key) {
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