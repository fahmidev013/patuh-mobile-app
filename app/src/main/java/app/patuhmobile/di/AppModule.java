package app.patuhmobile.di;

import org.greenrobot.eventbus.EventBus;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.auth.AuthInfo;
import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @ApplicationScope
    public App provideApp() {
        return this.app;
    }

    //-------- Utils

    @Provides @ApplicationScope
    public AppConfig provideAppConfig() {
        return new AppConfig();
    }

    @Provides
    @ApplicationScope
    public EventBus provideEventBus() {
        return new EventBus();
    }


    //-------- Classes

    @Provides @ApplicationScope
    public AuthInfo provideAuthInfo() {
        return new AuthInfo(this.app);
    }



}