package com.appdevin.sgtraffic.Class;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by JEYAVISHNU on 16/6/18.
 */

public class Users {
    public String Name="";
    public String Email="";
    private String Password="";
    private String Nric="";
    private String BirthD="";
    public String UID="";

public Users(){

}
    public Users(String Name, String Email, String Password, String Nric, String BirthD) {
        this.Name = Name;
        this.Email = Email;
        this.Password = Password;
        this.Nric = Nric;
        this.BirthD=BirthD;
    }


}

