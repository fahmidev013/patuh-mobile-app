package app.patuhmobile.model;

import android.content.Context;
import android.content.SharedPreferences;

import app.patuhmobile.AppConfig;
import app.patuhmobile.utils.StringUtils;

/**
 * Created by reynaldi on 11/17/16.
 */

public class UserAuthInfo {
    private SharedPreferences authPref;
    private String userId;
    private String username;
    private String loginType;
    private String picProfile;
    private String email;
    private String hp;
    private String accessToken;
    private String refreshToken;

    public UserAuthInfo(Context context) {
        authPref = context.getSharedPreferences(AppConfig.USER_AUTH_INFO, Context.MODE_PRIVATE);
        loadFromCache();
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getEmail() {
        return email;
    }

    public String getHp() {
        return hp;
    }

    public String getPicProfile() {
        return picProfile;
    }

    private void loadFromCache() {
        if (authPref != null && !StringUtils.isStringNullOrEmpty(authPref.getString("userId", null))) {
            this.userId = authPref.getString("userId", null);
            this.username = authPref.getString("username", null);
            this.loginType = authPref.getString("loginType", null);
            this.loginType = authPref.getString("picProfile", null);
            this.loginType = authPref.getString("picProfile", null);
            this.loginType = authPref.getString("picProfile", null);
        } else {
            reset();
        }
    }

    private void saveToCache() {
        SharedPreferences.Editor editor = authPref.edit();
        editor.putString("userId", this.userId);
        editor.putString("username", this.username);
        editor.putString("loginType", this.loginType);//picProfile
        editor.putString("picProfile", this.loginType);
        editor.commit();
    }

    private void removeFromCache() {
        SharedPreferences.Editor editor = authPref.edit();
        editor.remove("userId");
        editor.remove("username");
        editor.remove("loginType");
        editor.remove("picProfile");
        editor.commit();
    }

    private void reset() {
        this.userId = null;
        this.username = null;
        this.loginType = null;
        this.picProfile = null;
    }

    public void resetAndRemoveFromCache() {
        reset();
        removeFromCache();
    }

    public void updateFromLoginResponse(User response) {
        this.userId = response.getUserId();
        this.username = response.getUserName();
        this.loginType = response.getLoginType();
        this.picProfile = response.getLoginType();
        saveToCache();
    }
}
