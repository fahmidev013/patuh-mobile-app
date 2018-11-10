package app.patuhmobile.module.Register;

import java.util.HashMap;

import javax.inject.Inject;

import app.patuhmobile.App;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by Fahmi Hakim on 24/05/2018.
 * for SERA
 */

public class RegisterService {
    private App app;

    @Inject
    public RegisterService(App app) {
        this.app = app;
    }

    /*public Observable<User> getUserSupportingData() {
        RestAPI api = RestAPI.Factory.build(this.app);
        return api.getUserData("", new HashMap<String, String>())
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread());
    }*/


}
