package app.patuhmobile.di;

import org.greenrobot.eventbus.EventBus;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.auth.UnauthorisedInterceptor;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.module.Register.RegisterService;
import dagger.Component;


@ApplicationScope
@Component(modules = { AppModule.class })
public interface AppComponent {

    //-------- PROVIDE
    App provideApp();
    AppConfig provideAppConfig();



    //-------- Utils
    EventBus provideEventBus();

    //-------- Classes
    AuthInfo provideAuthInfo();
    RegisterService provideRegisterService();
    AuthService provideAuthService();


    //--------- INJECT
    void inject(App app);
    void inject(UnauthorisedInterceptor interceptor);

}
