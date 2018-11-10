package app.patuhmobile.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import app.patuhmobile.model.User;
import app.patuhmobile.utils.StringUtils;


public class AuthInfo extends BaseObservable{
    private SharedPreferences authPref;
    private String userId;
    private String username;
    private String loginType;
    private String mail, hp, dob, lokasi;
    private String picprofile;
    private String accessToken;
    private String refreshToken;
    private double PointReward;

    public AuthInfo(Context context) {
        authPref = context.getSharedPreferences("AUTH_INFO", Context.MODE_PRIVATE);
        loadFromCache();
    }

    @Bindable
    public String getUserId() {
        return userId;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    @Bindable
    public String getDob() {
        return dob;
    }

    @Bindable
    public String getLokasi() {
        return lokasi;
    }

    @Bindable
    public String getMail() {
        return mail;
    }

    @Bindable
    public String getHp() {
        return hp;
    }

    @Bindable
    public double getPointReward() {
        return PointReward;
    }

    @Bindable
    public String getPicprofile() {
        return picprofile;
    }

    public void setPicprofile(String picprofile) {
        this.picprofile = picprofile;
        saveImagePathProfile();
    }

    @Bindable
    public String getLoginType() {
        return loginType;
    }

    /*public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }*/

    private void loadFromCache() {
        if (authPref != null && !StringUtils.isStringNullOrEmpty(authPref.getString("userId", null))) {
            this.userId = authPref.getString("userId", null);
            this.username = authPref.getString("username", null);
            this.loginType = authPref.getString("loginType", null);
            this.mail = authPref.getString("mail", null);
            this.picprofile = authPref.getString("picprofile", null);
            this.hp = authPref.getString("hp", null);
            this.dob = authPref.getString("dob", null);
            this.lokasi = authPref.getString("lokasi", null);
            this.PointReward = Double.parseDouble(authPref.getString("points", null));
            /*this.accessToken = authPref.getString("accessToken", null);
            this.refreshToken = authPref.getString("refreshToken", null);*/
        } else {
            reset();
        }
    }

    private void saveToCache() {
        SharedPreferences.Editor editor = authPref.edit();
        editor.putString("userId", this.userId);
        editor.putString("username", this.username);
        editor.putString("loginType", this.loginType);
        editor.putString("mail", this.mail);
        editor.putString("hp", this.hp);
        editor.putString("dob", this.dob);
        editor.putString("lokasi", this.lokasi);
        editor.putString("points", String.valueOf(this.PointReward));
        /*editor.putString("accessToken", this.accessToken);
        editor.putString("refreshToken", this.refreshToken);*/
        editor.commit();
    }

    private void removeFromCache() {
        SharedPreferences.Editor editor = authPref.edit();
        editor.remove("userId");
        editor.remove("username");
        editor.remove("loginType");
        editor.remove("mail");
        editor.remove("hp");
        editor.remove("dob");
        editor.remove("lokasi");
        editor.remove("picprofile");
        editor.remove("points");
        /*editor.remove("accessToken");
        editor.remove("refreshToken");*/
        editor.commit();
    }

    public void saveImagePathProfile(){
        SharedPreferences.Editor editor = authPref.edit();
        editor.putString("picprofile", this.picprofile);
        editor.commit();
    }

    private void delImagePathProfile(){
        /*SharedPreferences.Editor editor = authPref.edit();

        editor.commit();*/
    }

    private void reset() {
        this.userId = null;
        this.username = null;
        this.loginType = null;
        this.mail = null;
        this.hp = null;
        this.dob = null;
        this.lokasi = null;
        this.picprofile = null;
        /*this.accessToken = null;
        this.refreshToken = null;*/
    }

    public void resetAndRemoveFromCache() {
        reset();
        removeFromCache();
        //delImagePathProfile();
    }

    public void updateFromLoginResponse(User response) {
        this.userId = response.getUserId();
        this.username = response.getUserName();
        this.loginType = response.getLoginType();
        this.mail = response.getUserProfile().getEmail();
        this.hp = response.getUserProfile().getPhoneNo();
        this.dob = response.getUserProfile().getDOB();
        this.lokasi = response.getUserProfile().getLocation();
        this.PointReward = response.getUserProfile().getPointReward();
        /*this.accessToken = response.access_token.trim();
        this.refreshToken = response.refresh_token.trim();*/
        saveToCache();
    }


    public void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences.Editor editor = authPref.edit();
        editor.putBoolean("IsFirstTimeLaunch", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return authPref.getBoolean("IsFirstTimeLaunch", true);
    }

}
