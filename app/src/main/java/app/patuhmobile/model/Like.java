package app.patuhmobile.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Fahmi Hakim on 13/08/2018.
 * for SERA
 */

public class Like extends BaseObservable implements Serializable{


    private String cStatus;

    public Like(String cStatus) {
        this.cStatus = cStatus;
    }

    public String getcStatus() {
        return cStatus;
    }
}
