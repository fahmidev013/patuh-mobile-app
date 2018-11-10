package app.patuhmobile.di;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Reynaldi on 12/1/2016.
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return this.activity;
    }
}
