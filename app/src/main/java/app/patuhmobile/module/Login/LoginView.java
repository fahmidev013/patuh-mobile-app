package app.patuhmobile.module.Login;

import android.app.Activity;

import com.google.gson.Gson;

import java.io.Serializable;

import app.patuhmobile.core.PresenterView;

/**
 * Created by Fahmi Hakim on 24/05/2018.
 * for SERA
 */

public interface LoginView<T> extends PresenterView {
    void showToast(String mesg);
    void showProgress();
    void hideProgress();
    void changeActivity(Class<T> activity);
}
