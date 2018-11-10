package app.patuhmobile.module.Home;

import android.os.Handler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import app.patuhmobile.auth.events.AuthenticationErrorEvent;
import app.patuhmobile.core.Presenter;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * Created by Fahmi Hakim on 23/05/2018.
 * for SERA
 */

public class HomePresenter implements Presenter<HomeView> {
    private EventBus eventBus;
    private HomeView view;

    @Inject()
    public HomePresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(HomeView view) {
        this.view = view;
        eventBus.register(this);
    }


    @Override
    public void detachView() {
        eventBus.unregister(this);
        this.view = null;
    }

    @Subscribe
    public void onEvent(AuthenticationErrorEvent event){
    }

}
