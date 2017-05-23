package com.example.b.expensewatcher.models;

/**
 * Created by B on 16-Mar-17.
 */

public class User {

    public String $id;
    public String userName;
    public String firstName;
    public String lastName;
    public String passcode;
    public String mobileNumber;

    public User()
    {}

    public String getUserName() { return userName; }
    public String getfirstName() { return firstName; }
    public String getlastName() { return lastName; }
    public String getmobileNumber() { return mobileNumber; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setfirstName(String firstName) { this.firstName = firstName; }
    public void setlastName(String lastName) { this.lastName = lastName; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public User(String userName, String firstName, String lastName, String passcode, String mobileNumber) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passcode = passcode;
        this.mobileNumber = mobileNumber;
    }
}
