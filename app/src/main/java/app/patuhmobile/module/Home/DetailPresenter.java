package app.patuhmobile.module.Home;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import app.patuhmobile.auth.events.AuthenticationErrorEvent;
import app.patuhmobile.core.Presenter;
import app.patuhmobile.module.Home.DetailView;

/**
 * Created by Fahmi Hakim on 03/06/2018.
 * for SERA
 */

public class DetailPresenter implements Presenter<DetailView> {


    private EventBus eventBus;
    private DetailView view;

    @Inject()
    public DetailPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(DetailView view) {
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
