package app.patuhmobile.auth.events;

import android.content.Intent;

/**
 * Created by Fahmi Hakim on 15/07/2018.
 * for SERA
 */

public class SuccessEvent {

    int resultCode, requestCode;
    Intent mdata;

    public SuccessEvent(int reqCode, int resultCOde, Intent data) {

        requestCode = reqCode;
        this.resultCode = resultCOde;
        mdata = data;
    }


    public int getRequestCode() {
        return requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public Intent getMdata() {
        return mdata;
    }
}
