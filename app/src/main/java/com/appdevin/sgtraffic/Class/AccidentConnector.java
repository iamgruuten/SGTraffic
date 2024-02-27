package com.appdevin.sgtraffic.Class;

public class AccidentConnector {
    //Variables
    public String Address;
    public double LATITUDE;
    public double LONGTITUDE;
    public String BirthD;
    public String AccidentType;
    public String Licensenumber;
    public String Description;
    public String Key;

    public AccidentConnector(String accidentType, String description) {
        this.AccidentType = accidentType;
        this.Description = description;
    }

    public String getAddress() {
        return Address;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public double getLONGTITUDE() {
        return LONGTITUDE;
    }

    public String getBirthD() {
        return BirthD;
    }

    public String getAccidentType() {
        return AccidentType;
    }

    public String getLicensenumber() {
        return Licensenumber;
    }

    public String getDescription() {
        return Description;
    }

    public String getKey() {
        return Key;
    }
}
