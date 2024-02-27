package com.appdevin.sgtraffic.Class;

/**
 * Created by JEYAVISHNU on 19/6/18.
 */

public class NewUsers {

    //Variables
    public String Name;
    public String Email;
    public  String Birthday;
    public String Password;
    public String NRIC;
   public String Phone_Number;



    public NewUsers() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    //Constructor
    public NewUsers(String Name, String Email, String Birthday, String Password, String NRIC,String Phone_Number) {
        this.Name = Name;
        this.Email = Email;
        this.Birthday = Birthday;
        this.Password = Password;
        this.NRIC = NRIC;
        this.Phone_Number=Phone_Number;
    }


}
