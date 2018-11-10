package app.patuhmobile.core;

/**
 * Created by Reynaldi on 12/1/2016.
 */

public interface Presenter<P extends PresenterView> {
    void attachView(P view);
    void detachView();
}