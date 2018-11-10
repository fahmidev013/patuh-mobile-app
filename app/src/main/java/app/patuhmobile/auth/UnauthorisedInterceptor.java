package app.patuhmobile.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import app.patuhmobile.App;
import app.patuhmobile.auth.events.AuthenticationErrorEvent;
import okhttp3.Interceptor;
import okhttp3.Response;



public class UnauthorisedInterceptor implements Interceptor {

    @Inject EventBus eventBus;

    public UnauthorisedInterceptor(Context context) {
        App.get(context).component().inject(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            Log.d("Unauthorized", "401 Detected. Go to register");
            //eventBus.post(new AuthenticationErrorEvent());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    eventBus.post(new AuthenticationErrorEvent());
                }
            });
        }
        return response;
    }
}