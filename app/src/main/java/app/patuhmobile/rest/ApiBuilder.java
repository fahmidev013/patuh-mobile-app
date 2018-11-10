package app.patuhmobile.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import app.patuhmobile.App;
import app.patuhmobile.BuildConfig;
import app.patuhmobile.auth.UnauthorisedInterceptor;
import app.patuhmobile.utils.StringUtils;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import static com.google.common.net.HttpHeaders.AUTHORIZATION;

public class ApiBuilder {
    public static Gson constructDefaultGson() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        return gson;
    }

    public static Retrofit buildStandardApiClient(final App app) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(150, TimeUnit.SECONDS);
        builder.connectTimeout(100, TimeUnit.SECONDS);
        builder.writeTimeout(100, TimeUnit.SECONDS);

        //builder.certificatePinner(new CertificatePinner.Builder().add("*.androidadvance.com", "sha256/RqzElicVPA6LkKm9HblOvNOUqWmD+4zNXcRb+WjcaAE=")
        //    .add("*.xxxxxx.com", "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=")
        //    .add("*.xxxxxxx.com", "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=")
        //    .add("*.xxxxxxx.com", "sha256/VjLZe/p3W/PJnd6lL8JVNBCGQBZynFLdZSTIqcO0SJ8=")
        //    .build());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(interceptor);
        }

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        try {
            Cache cache = new Cache(app.getCacheDir(), cacheSize);
            builder.cache(cache);
        } catch (Exception e){}

        //builder.addInterceptor(new UnauthorisedInterceptor(app));
        OkHttpClient client = builder.build();

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(app.getConfig().getApiBaseUrl())
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(constructDefaultGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

        return retrofit;
    }

    public static Retrofit buildSecureResourceApiClient(final App app) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        //builder.certificatePinner(new CertificatePinner.Builder().add("*.androidadvance.com", "sha256/RqzElicVPA6LkKm9HblOvNOUqWmD+4zNXcRb+WjcaAE=")
        //    .add("*.xxxxxx.com", "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=")
        //    .add("*.xxxxxxx.com", "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=")
        //    .add("*.xxxxxxx.com", "sha256/VjLZe/p3W/PJnd6lL8JVNBCGQBZynFLdZSTIqcO0SJ8=")
        //    .build());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(interceptor);
        }

        builder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                Log.d("Http", "401 - refreshing token");

                String newAccessToken = ""; //app.component().provideAuthService().refreshToken();
                Log.d("Http", "newAccessToken Value: " + newAccessToken);

                return StringUtils.isStringNullOrEmpty(newAccessToken)
                        ? null
                        : response.request().newBuilder()
                        //.header(AUTHORIZATION, "Bearer " + newAccessToken)
                        .build();
            }
        });

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        //.header(AUTHORIZATION, "Bearer " + app.getAuthInfo().getAccessToken())
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        //Extra Headers

        //builder.addNetworkInterceptor().add(chain -> {
        //  Request request = chain.request().newBuilder().addHeader("Authorization", authToken).build();
        //  return chain.proceed(request);
        //});

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(app.getCacheDir(), cacheSize);
        builder.cache(cache);

        builder.addInterceptor(new UnauthorisedInterceptor(app));
        OkHttpClient client = builder.build();

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(app.getConfig().getApiResourceUrl())
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(constructDefaultGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

        return retrofit;
    }
}
