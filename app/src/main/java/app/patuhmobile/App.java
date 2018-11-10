package app.patuhmobile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.events.AuthenticationErrorEvent;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.di.AppComponent;
import app.patuhmobile.di.AppModule;
import app.patuhmobile.di.DaggerAppComponent;
import app.patuhmobile.utils.TypefaceUtil;

/**
 * Created by Fahmi Hakim on 22/05/2018.
 * for SERA
 */

public class App extends Application {

    private AppComponent component;
    private io.reactivex.Scheduler defaultSubscribeScheduler;

    @Inject
    AppConfig appConfig;

    @Inject
    AuthInfo authInfo;

    @Inject
    EventBus eventBus;

    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        component.inject(this);
        eventBus.register(this);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/HelveticaNeue.otf");
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public AppComponent component() {
        return component;
    }

    public AuthInfo getAuthInfo() {
        return this.authInfo;
    }

    public AppConfig getConfig() {
        return this.appConfig;
    }

    public io.reactivex.Scheduler getSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = io.reactivex.schedulers.Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    public SharedPreferences getSharedPreference(String name, int mode) {
        return this.getSharedPreferences(name, mode);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public void onTerminate() {
        eventBus.unregister(this);
        super.onTerminate();
    }

    @Subscribe
    public void onEvent(AuthenticationErrorEvent event) {
        Log.d("Unauthorized", "Redirect to Signin Activity!");
    }

}
