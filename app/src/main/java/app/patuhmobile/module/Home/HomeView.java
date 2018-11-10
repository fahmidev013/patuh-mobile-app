package app.patuhmobile.module.Home;

import app.patuhmobile.core.PresenterView;

/**
 * Created by Fahmi Hakim on 23/05/2018.
 * for SERA
 */

public interface HomeView extends PresenterView {
    void showToast(String mesg);
    void showProgress();
    void hideProgress();
}
