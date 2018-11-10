package app.patuhmobile.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by Fahmi Hakim on 24/06/2018.
 * for SERA
 */

public class UserProfile extends BaseObservable implements Serializable {


    private String UserID, DOB, Location, PhoneNo, Email;
    private double PointReward;


    public UserProfile(String userID, String DOB, String location, String phoneNo, String email, double point) {
        UserID = userID;
        this.DOB = DOB;
        Location = location;
        PhoneNo = phoneNo;
        Email = email;
        PointReward = point;
    }

    @Bindable
    public double getPointReward() {
        return PointReward;
    }

    @Bindable
    public String getUserID() {
        return UserID;
    }

    @Bindable
    public String getDOB() {
        return DOB;
    }

    @Bindable
    public String getLocation() {
        return Location;
    }

    @Bindable
    public String getPhoneNo() {
        return PhoneNo;
    }

    @Bindable
    public String getEmail() {
        return Email;
    }
}
