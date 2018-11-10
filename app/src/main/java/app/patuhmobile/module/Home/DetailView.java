package app.patuhmobile.module.Home;

import app.patuhmobile.core.PresenterView;

/**
 * Created by Fahmi Hakim on 03/06/2018.
 * for SERA
 */

public interface DetailView extends PresenterView {
    void showToast(String mesg);
    void showProgress();
    void hideProgress();
}
