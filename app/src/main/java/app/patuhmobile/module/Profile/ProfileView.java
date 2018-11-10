package app.patuhmobile.module.Profile;

import app.patuhmobile.core.PresenterView;

/**
 * Created by Fahmi Hakim on 24/05/2018.
 * for SERA
 */

public interface ProfileView<T> extends PresenterView {

    void showToast(String mesg);
    void showProgress();
    void hideProgress();
    void changeActivity(Class<T> activity);

}
