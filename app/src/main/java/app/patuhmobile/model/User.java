package app.patuhmobile.model;

import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import app.patuhmobile.di.ActivityScope;
import app.patuhmobile.di.ApplicationScope;

/**
 * Created by Fahmi Hakim on 24/05/2018.
 * for SERA
 */

@ActivityScope
public class User extends BaseObservable implements Serializable {

    private String userId;
    private String userName;
    private String loginType;
    private UserProfile userProfile;
    private String picProfile;
    private double points;

    public User(String userId, String userName, String loginType, UserProfile userProfile, String picProfile, double point) {
        this.userId = userId;
        this.userName = userName;
        this.loginType = loginType;
        this.picProfile = picProfile;
        this.userProfile = userProfile;
        this.points = point;
    }


    @Bindable
    public double getPoints() {
        return (points != 0.0 ? points : 0.0);
    }

    @Bindable
    public String getUserId() {
        return (userId != null ? userId : "131313");
    }

    @Bindable
    public String getUserName() {
        return (userName != null ? userName : "Yaya");
    }

    @Bindable
    public String getLoginType() {
        return (loginType != null ? loginType : "Admin");
    }

    @Bindable
    public UserProfile getUserProfile() {
        return userProfile;
    }


    @Bindable
    public String getPicProfile() {
        return (picProfile != null ? picProfile : "Admin");
    }

}
