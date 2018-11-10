package app.patuhmobile.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Fahmi Hakim on 19/06/2018.
 * for SERA
 */

public class ResponeApiModel extends BaseObservable implements Serializable {

    private String message, messageCode;
    private boolean status;


    public ResponeApiModel(boolean status, String message, String messageCode) {
        this.status = status;
        this.message = message;
        this.messageCode = messageCode;
    }


    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
