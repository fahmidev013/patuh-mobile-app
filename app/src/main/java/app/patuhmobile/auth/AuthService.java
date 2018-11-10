package app.patuhmobile.auth;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import app.patuhmobile.App;
import app.patuhmobile.auth.events.LoginSuccessEvent;
import app.patuhmobile.auth.events.LogoutSuccessEvent;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.model.Komentar;
import app.patuhmobile.model.Kupon;
import app.patuhmobile.model.Like;
import app.patuhmobile.model.Point;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.model.TukarKupon;
import app.patuhmobile.model.User;
import app.patuhmobile.rest.RestAPI;
import app.patuhmobile.utils.StringUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthService {
    public interface LoginCallback {
        void onSuccess();
        void onFail(Throwable e);
    }



    public interface ArtikelCallback {
        void onSuccess(ArrayList<Artikel> artikelList);
        void onFail(Throwable e);
    }


    public interface TukarKuponCallback {
        void onSuccess(List<TukarKupon> tukarKuponList);
        void onFail(Throwable e);
    }

    public interface KuponCallback {
        void onSuccess(List<Kupon> kuponList);
        void onFail(Throwable e);
    }

    public interface PointCallback {
        void onSuccess(Point point);
        void onFail(Throwable e);
    }

    public interface KomentarCallback {
        void onSuccess(ArrayList<Komentar> komentars);
        void onFail(Throwable e);
    }

    public interface LikeCallback {
        void onSuccess(ArrayList<Like> like);
        void onFail(Throwable e);
    }

    public interface SignupCallback {
        void onSuccess(ResponeApiModel responeRegisterModel);
        void onFail(Throwable e);
    }

    public interface DataCallback {
        void onSuccess(ResponeApiModel responeRegisterModel);
        void onFail(Throwable e);
    }

    private App app;
    private AuthInfo authInfo;
    private EventBus eventBus;

    @Inject
    public AuthService(App app, AuthInfo authInfo,
                       EventBus eventBus) {
        this.app = app;
        this.authInfo = authInfo;
        this.eventBus = eventBus;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public EventBus getEventBus() { return eventBus; }

    public boolean isLoggedIn() {
        return !StringUtils.isStringNullOrEmpty(authInfo.getUserId());
    }


    public void login(String username, String password, final LoginCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.postLogin(username, password)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(@NonNull User user) {
                        if (!user.getUserName().equalsIgnoreCase("")){
                            onLoginSuccess(user);
                            callback.onSuccess();
                        } else {
                            callback.onFail(new Throwable());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void onLoginSuccess(User response) {
        //Toast.makeText(app, response.toString(), Toast.LENGTH_LONG).show();
        authInfo.updateFromLoginResponse(response);
        eventBus.post(new LoginSuccessEvent());
    }



    public void logout() {
        authInfo.resetAndRemoveFromCache();
        eventBus.post(new LogoutSuccessEvent());
    }

    public void register(String userId, String password, String username, String email, String phone, String location, String birthday, File filePaht, final SignupCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        //File file = new File(filePaht);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), filePaht);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", filePaht.getName(), reqFile);
        RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody pass = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody uname = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody mail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody hp = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody loc = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody bday = RequestBody.create(MediaType.parse("text/plain"), birthday);
        api.postProfileImage(body, uid, pass, uname, mail, hp, loc, bday)
                .enqueue(new Callback<ResponeApiModel>() {
                             @Override
                             public void onResponse(Call<ResponeApiModel> call, Response<ResponeApiModel> response) {
                                 ResponeApiModel serverResponse = response.body();
                                 if (response.isSuccessful()) {
                                     if (serverResponse.getMessageCode().equalsIgnoreCase("S")) {
                                         callback.onSuccess(serverResponse);
                                         //Toast.makeText(app.getBaseContext(), "SUKSES", Toast.LENGTH_LONG).show();

                                         //DO AS PER YOUR REQUIREMENT
                                     } else {
                                         callback.onSuccess(serverResponse);
                                     }
                                 } else {
                                     if (response.code() == 100) {
                                         Gson gson = new GsonBuilder().create();
                                         ResponeApiModel mError = new ResponeApiModel(false, null, null);
                                         try {
                                             mError = gson.fromJson(response.errorBody().string(), ResponeApiModel.class);
                                             if(mError.getMessage().equals("Wallet  not enough."))
                                             {
                                                 if(mError.getMessage().equals(""))
                                                 {
                                                     mError.setMessage("Error");
                                                 }

                                                 //ToDo we can handle here

                                             }
                                         } catch (IOException e) {
                                             // handle failure to read error
                                         }
                                     }
                                     else {
                                         Gson gson = new GsonBuilder().create();
                                         ResponeApiModel mError = new ResponeApiModel(true, null, null);
                                         try {
                                             mError = gson.fromJson(response.errorBody().string(), ResponeApiModel.class);
                                             if(mError.getMessage().equals("Wallet  not enough."))
                                             {
                                                 if(mError.getMessage().equals(""))
                                                 {
                                                     mError.setMessage("Error");
                                                 }

                                                 //ToDo we can handle here

                                             }
                                         } catch (IOException e) {
                                             // handle failure to read error
                                         }
                                         //ToDo we can handle here(  OR ELSE )

                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponeApiModel> call, Throwable t) {

                             }
                         });
        /*api.postProfileImage(body, uid, pass, uname, mail, hp, loc, bday)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(app.getSubscribeScheduler())
                .subscribe(new DefaultObserver<ResponeApiModel>() {
                               @Override
                               public void onNext(@NonNull ResponeApiModel responeRegisterModel) {
                                   callback.onSuccess(responeRegisterModel);
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {
                                   callback.onFail(e);
                               }

                               @Override
                               public void onComplete() {

                               }
                           });*/
                /*.subscribe(new DisposableObserver<ResponeApiModel>() {
                    @Override
                    public void onNext(@NonNull ResponeApiModel responeRegisterModel) {
                        callback.onSuccess(responeRegisterModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

/*    public String refreshToken() throws IOException {
        RestAPI api = RestAPI.Factory.build(this.app);
        User response = api
                .refreshToken("piri-c-android", this.authInfo.getRefreshToken(), "refresh_token")
                .execute().body();
        if (response != null) {
            authInfo.updateFromLoginResponse(response);
            return response.access_token;
        }

        return null;
    }*/

    public void unggahCerita(String id, String category, String title, String story, String latitude, String longitude, String userId, String location, File filePaht, final DataCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        //File file = new File(filePaht);
        MultipartBody.Part body = null;
        try {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), filePaht);
            body = MultipartBody.Part.createFormData("upload", filePaht.getName(), reqFile);
        } catch (Exception e) {}
        RequestBody uid = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody cat = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody judul = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody cerita = RequestBody.create(MediaType.parse("text/plain"), story);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), latitude);
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), longitude);
        RequestBody loc = RequestBody.create(MediaType.parse("text/plain"), location);
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), id);
        api.postArticle(body, uid, cat, judul, cerita, lat, lng, loc, idUser)
                .enqueue(new Callback<ResponeApiModel>() {
                    @Override
                    public void onResponse(Call<ResponeApiModel> call, Response<ResponeApiModel> response) {
                        ResponeApiModel serverResponse = response.body();
                        if (response.isSuccessful()) {
                            if (serverResponse.getMessageCode().equalsIgnoreCase("S")) {
                                callback.onSuccess(serverResponse);
                                //Toast.makeText(app.getBaseContext(), "SUKSES", Toast.LENGTH_LONG).show();

                                //DO AS PER YOUR REQUIREMENT
                            }
                        } else {
                            if (response.code() == 100) {
                                Gson gson = new GsonBuilder().create();
                                ResponeApiModel mError = new ResponeApiModel(false, null, null);
                                try {
                                    mError = gson.fromJson(response.errorBody().string(), ResponeApiModel.class);
                                    if(mError.getMessage().equals("Wallet  not enough."))
                                    {
                                        if(mError.getMessage().equals(""))
                                        {
                                            mError.setMessage("Error");
                                        }

                                        //ToDo we can handle here

                                    }
                                } catch (IOException e) {
                                    // handle failure to read error
                                }
                            }
                            else {
                                Gson gson = new GsonBuilder().create();
                                ResponeApiModel mError = new ResponeApiModel(true, null, null);
                                try {
                                    mError = gson.fromJson(response.errorBody().string(), ResponeApiModel.class);
                                    if(mError.getMessage().equals("Wallet  not enough."))
                                    {
                                        if(mError.getMessage().equals(""))
                                        {
                                            mError.setMessage("Error");
                                        }

                                        //ToDo we can handle here

                                    }
                                } catch (IOException e) {
                                    // handle failure to read error
                                }
                                //ToDo we can handle here(  OR ELSE )

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeApiModel> call, Throwable t) {

                    }
                });
    }


    public void laporKonten(String artikelId, String desc, final DataCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.laporKOnten(artikelId, null,desc, null, null, null)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponeApiModel>() {
                    @Override
                    public void onNext(@NonNull ResponeApiModel responeRegisterModel) {
                        if (responeRegisterModel.getMessageCode().equalsIgnoreCase("S")){
                            callback.onSuccess(responeRegisterModel);
                        } else {
                            callback.onFail(new Throwable());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.e("Komen Error", String.valueOf(e));
                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void unggahKomentar(String userId, String artikelId, String komentar, final DataCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.postComments(userId, artikelId, komentar)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponeApiModel>() {
                    @Override
                    public void onNext(@NonNull ResponeApiModel responeRegisterModel) {
                        if (responeRegisterModel.getMessageCode().equalsIgnoreCase("S")){
                            callback.onSuccess(responeRegisterModel);
                        } else {
                            callback.onFail(new Throwable());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.e("Komen Error", String.valueOf(e));
                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void unggahLike(String userId, String artikelId, String status, final DataCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.postLikes(artikelId, userId, status)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponeApiModel>() {
                    @Override
                    public void onNext(@NonNull ResponeApiModel responeRegisterModel) {
                        if (responeRegisterModel.getMessageCode().equalsIgnoreCase("S")){
                            callback.onSuccess(responeRegisterModel);
                        } else {
                            callback.onFail(new Throwable());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getAllArticle(final ArtikelCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getAllArticle()
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Artikel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Artikel> artikels) {
                        callback.onSuccess(artikels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getAllNews(final ArtikelCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getAllNews()
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Artikel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Artikel> artikels) {
                        callback.onSuccess(artikels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getPoints(String userId, final PointCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getPoint(userId)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Point>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Point point) {
                        callback.onSuccess(point);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getAllKupon(final AuthService.KuponCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getAllKupon()
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Kupon>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Kupon> kuponList) {
                        callback.onSuccess(kuponList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void tukarKupon(String uid, final AuthService.TukarKuponCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getKuponByUser(uid)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TukarKupon>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<TukarKupon> tukarKuponList) {
                        callback.onSuccess(tukarKuponList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getAllComments(String artikelId, final KomentarCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getAllComments(artikelId)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Komentar>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Komentar> komentars) {
                        callback.onSuccess(komentars);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getLikeStatus(String artikelId, String userId,  final LikeCallback callback) {
        RestAPI api = RestAPI.Factory.build(this.app);
        api.getLike(artikelId, userId)
                .subscribeOn(app.getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Like>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Like> like) {
                        callback.onSuccess(like);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        callback.onFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
