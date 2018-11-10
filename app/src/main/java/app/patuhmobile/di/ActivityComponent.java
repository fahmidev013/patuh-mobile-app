package app.patuhmobile.di;

import android.app.Activity;

import app.patuhmobile.core.BaseFragment;
import app.patuhmobile.module.Home.DetailActivity;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Home.HomeFragment;
import app.patuhmobile.module.Login.LoginActivity;
import app.patuhmobile.module.Profile.ProfileFragment;
import dagger.Component;


@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //------------- PROVIDE
    Activity provideActivity();


    //------------- INJECT
    void inject(HomeActivity activity);
    void inject(LoginActivity activity);
    void inject(DetailActivity activity);

    void inject(HomeFragment fragment);
    void inject(ProfileFragment fragment);

}
