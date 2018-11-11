package app.patuhmobile.rest;

import java.util.ArrayList;
import java.util.List;

import app.patuhmobile.App;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.model.Komentar;
import app.patuhmobile.model.Kupon;
import app.patuhmobile.model.Like;
import app.patuhmobile.model.Point;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.model.TukarKupon;
import app.patuhmobile.model.User;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Fahmi Hakim on 24/05/2018.
 * for SERA
 */

public interface RestAPI {

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("Login")
    Observable<User> postLogin(@Field("userId") String username, @Field("password") String password);

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("Comment")
    Observable<ResponeApiModel> postComments(@Field("userId") String userid, @Field("ArticleId") String artikelId, @Field("Comment") String komentar);

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("ArticleReport")
    Observable<ResponeApiModel> laporKOnten(@Field("ArticleId") String artikelId, @Field("ReportCategory") String ReportCategory, @Field("ReportDesc") String ReportDesc, @Field("cStatus") String cStatus, @Field("cCreated") String cCreated, @Field("cLastUpdate") String cLastUpdate);

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("Like")
    Observable<ResponeApiModel> postLikes(@Field("UserId") String userid, @Field("ArticleId") String artikelId, @Field("Status") String status);


    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("UserProfile") //String userId, String password, String username, String email, String phone, String location, String birthday, String photo
    Observable<Void> postRegister(@Field("userId") String userid, @Field("password") String password, @Field("userName") String username, @Field("email") String email, @Field("phoneNo") String phone, @Field("location") String location, @Field("birthday") String bday, @Field("pic") String pic);



    @Multipart
    @POST("UserProfile")
    Call<ResponeApiModel> postProfileImage(@Part MultipartBody.Part image, @Part("userId") RequestBody userid, @Part("password") RequestBody password, @Part("userName") RequestBody username, @Part("email") RequestBody email, @Part("phoneNo") RequestBody phone, @Part("location") RequestBody location, @Part("birthday") RequestBody bday);

    @Multipart
    @POST("Article")
    Call<ResponeApiModel> postArticle(@Part MultipartBody.Part image,
                                      @Part("userId") RequestBody userid,
                                      @Part("category") RequestBody kategory,
                                      @Part("title") RequestBody judul,
                                      @Part("story") RequestBody certia,
                                      @Part("latitude") RequestBody latitude,
                                      @Part("longitude") RequestBody longitude,
                                      @Part("location") RequestBody location,
                                      @Part("Id") RequestBody id);



    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("Coupon")
    Observable<ResponeApiModel> postCoupon(@Field("msCouponId") String kuponId, @Field("UserId") String userId);

    @GET("Coupon")
    Observable<ArrayList<Kupon>> getAllKupon();

    @GET("Coupon")
    Observable<List<TukarKupon>> getKuponByUser(@Query("userId") String uid);

    @GET("RewardPoint")
    Observable<Point> getPoint(@Query("userId") String uid);

    @GET("article")
    Observable<Artikel> getArticle(@Query("id") String id);


    @GET("article")
    Observable<ArrayList<Artikel>> getAllArticle();

    @GET("news")
    Observable<ArrayList<Artikel>> getAllNews();

    @GET("Comment")
    Observable<ArrayList<Komentar>> getAllComments(@Query("ArticleId") String artikelId);

    @GET("Like")
    Observable<ArrayList<Like>> getLike(@Query("ArticleId") String artikelId, @Query("UserId") String userId);


    class Factory {
        public static RestAPI build(App app) {
            Retrofit client = ApiBuilder.buildStandardApiClient(app);
            return client.create(RestAPI.class);
        }
    }


}
